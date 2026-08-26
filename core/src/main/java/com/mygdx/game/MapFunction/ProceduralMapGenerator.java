package com.mygdx.game.MapFunction;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// Generates a new .mapt file with just the layout (buildings + decoration -
// see an existing map or .str structure for the sparse BuildAdd/MapObject
// command format hand-made maps already use). The actual ground - color,
// material, road surface - is NOT baked into this file at all: it's repainted
// by ProceduralTerrainPainter every time the map loads, straight onto the
// live BlockList2D, using nothing but noise keyed off the same seed. That
// split is what lets the ground be a smooth, seamless noise field instead of
// a grid of PNG tiles, while buildings/decor stay ordinary placed objects.
//
// computeRoadCells() is the one piece both this class and the painter need
// in exactly the same form: it always starts from a fresh Random(seed) and
// touches no other state, so calling it twice - once here (to keep
// buildings/decor off the road) and once from the painter (to know which
// cells are asphalt) - reproduces the identical path both times without
// having to save it anywhere.
//
// The road network itself is a handful of hub points connected by a minimum
// spanning tree (so everything's reachable without a dense mesh), with one
// extra edge added for a loop; each connection is a fractal "midpoint
// displacement" path (repeatedly nudging each segment's midpoint sideways by
// a shrinking random amount) rather than a straight line, for a winding,
// organic look instead of a ruler-straight road.
public class ProceduralMapGenerator {
    private static final String[] BUILDINGS = {"BigBuildingWood1", "Building2"};
    private static final int BUILDING_CLEARANCE = 12;
    private static final int ROAD_WIDTH = 2;

    /** Default size for a freshly-generated map - see MapSelectScreen. */
    public static final int DEFAULT_SIZE = 260;

    public static String generateLayout(long seed, int width, int height){
        Random rand = new Random(seed+1);
        StringBuilder sb = new StringBuilder();
        sb.append("^Procedural").append(seed).append(";\n");
        sb.append("/x ").append(width).append(":y ").append(height).append(":;\n\n");

        boolean[][] road = computeRoadCells(seed, width, height);

        List<int[]> placedBuildings = new ArrayList<>();
        int buildingTarget = 10 + rand.nextInt(10);
        int attempts = 0;
        while (placedBuildings.size() < buildingTarget && attempts < buildingTarget*20){
            attempts++;
            int x = margin(rand, width);
            int y = margin(rand, height);
            if (!clearOfRoad(road, width, height, x, y, BUILDING_CLEARANCE)) continue;
            if (!clearOfBuildings(placedBuildings, x, y, BUILDING_CLEARANCE)) continue;
            String building = BUILDINGS[rand.nextInt(BUILDINGS.length)];
            int rotation = rand.nextInt(4);
            sb.append("BuildAdd:B ").append(building).append(":x").append(x).append(":y").append(y)
                    .append(":r").append(rotation).append(":;\n");
            placedBuildings.add(new int[]{x, y});
        }

        int decorTarget = (width*height)/150;
        int decorAttempts = 0, decorPlaced = 0;
        while (decorPlaced < decorTarget && decorAttempts < decorTarget*10){
            decorAttempts++;
            int x = margin(rand, width);
            int y = margin(rand, height);
            if (road[y][x]) continue;
            if (!clearOfBuildings(placedBuildings, x, y, 6)) continue;
            sb.append("MapObject:o pepper:x").append(x).append(":y").append(y).append(":;\n");
            decorPlaced++;
        }

        return sb.toString();
    }

    /** Generates the layout and writes it to disk, returning the path passed in. */
    public static String generateLayoutAndSave(long seed, int width, int height, String path) throws IOException {
        String content = generateLayout(seed, width, height);
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        }
        return path;
    }

    /** The road cell grid for this seed/size - always deterministic, see class comment. */
    public static boolean[][] computeRoadCells(long seed, int width, int height){
        Random rand = new Random(seed);
        boolean[][] road = new boolean[height][width];
        int hubCount = 3 + rand.nextInt(2);
        List<int[]> hubs = new ArrayList<>();
        for (int h = 0; h < hubCount; h++){
            hubs.add(pickHub(rand, width, height, hubs));
        }
        List<int[]> edges = minimumSpanningTree(hubs);
        if (hubs.size() > 2) edges.add(new int[]{0, hubs.size()-1});
        for (int[] edge : edges){
            tracePath(road, width, height, hubs.get(edge[0]), hubs.get(edge[1]), rand);
        }
        return road;
    }

    private static int margin(Random rand, int size){
        int m = Math.max(size/10, 8);
        return m + rand.nextInt(Math.max(size-2*m, 1));
    }

    private static int[] pickHub(Random rand, int width, int height, List<int[]> existing){
        for (int attempt = 0; attempt < 50; attempt++){
            int x = margin(rand, width);
            int y = margin(rand, height);
            boolean farEnough = true;
            for (int[] h : existing){
                if (dist(h, new int[]{x, y}) < Math.min(width, height)*0.25) { farEnough = false; break; }
            }
            if (farEnough) return new int[]{x, y};
        }
        return new int[]{margin(rand, width), margin(rand, height)};
    }

    private static double dist(int[] a, int[] b){
        return Math.sqrt(Math.pow(a[0]-b[0], 2) + Math.pow(a[1]-b[1], 2));
    }

    private static List<int[]> minimumSpanningTree(List<int[]> hubs){
        List<int[]> edges = new ArrayList<>();
        if (hubs.size() < 2) return edges;
        Set<Integer> connected = new HashSet<>();
        connected.add(0);
        while (connected.size() < hubs.size()){
            int bestFrom = -1, bestTo = -1;
            double bestDist = Double.MAX_VALUE;
            for (int from : connected){
                for (int to = 0; to < hubs.size(); to++){
                    if (connected.contains(to)) continue;
                    double d = dist(hubs.get(from), hubs.get(to));
                    if (d < bestDist){ bestDist = d; bestFrom = from; bestTo = to; }
                }
            }
            edges.add(new int[]{bestFrom, bestTo});
            connected.add(bestTo);
        }
        return edges;
    }

    private static void tracePath(boolean[][] road, int width, int height, int[] from, int[] to, Random rand){
        List<float[]> points = new ArrayList<>();
        points.add(new float[]{from[0], from[1]});
        points.add(new float[]{to[0], to[1]});
        float roughness = 0.5f;
        for (int iteration = 0; iteration < 4; iteration++){
            List<float[]> next = new ArrayList<>();
            for (int i = 0; i < points.size()-1; i++){
                float[] a = points.get(i);
                float[] b = points.get(i+1);
                next.add(a);
                float mx = (a[0]+b[0])/2f, my = (a[1]+b[1])/2f;
                float dx = b[0]-a[0], dy = b[1]-a[1];
                float len = Math.max((float) Math.sqrt(dx*dx+dy*dy), 0.001f);
                float perpX = -dy/len, perpY = dx/len;
                float displace = (rand.nextFloat()*2f-1f) * len * roughness;
                next.add(new float[]{mx+perpX*displace, my+perpY*displace});
            }
            next.add(points.get(points.size()-1));
            points = next;
            roughness *= 0.55f;
        }
        for (int i = 0; i < points.size()-1; i++){
            stampSegment(road, width, height, points.get(i), points.get(i+1));
        }
    }

    private static void stampSegment(boolean[][] road, int width, int height, float[] a, float[] b){
        float dx = b[0]-a[0], dy = b[1]-a[1];
        int steps = Math.max((int) Math.sqrt(dx*dx+dy*dy), 1);
        for (int s = 0; s <= steps; s++){
            float t = (float) s/steps;
            int cx = Math.round(a[0]+dx*t);
            int cy = Math.round(a[1]+dy*t);
            for (int oy = -ROAD_WIDTH/2; oy <= ROAD_WIDTH/2; oy++){
                for (int ox = -ROAD_WIDTH/2; ox <= ROAD_WIDTH/2; ox++){
                    int px = cx+ox, py = cy+oy;
                    if (px >= 1 && px < width-1 && py >= 1 && py < height-1){
                        road[py][px] = true;
                    }
                }
            }
        }
    }

    private static boolean clearOfRoad(boolean[][] road, int width, int height, int x, int y, int clearance){
        for (int oy = -clearance; oy <= clearance; oy++){
            for (int ox = -clearance; ox <= clearance; ox++){
                int px = x+ox, py = y+oy;
                if (px < 0 || px >= width || py < 0 || py >= height) continue;
                if (road[py][px]) return false;
            }
        }
        return true;
    }

    private static boolean clearOfBuildings(List<int[]> placed, int x, int y, int clearance){
        for (int[] b : placed){
            if (Math.abs(b[0]-x) < clearance && Math.abs(b[1]-y) < clearance) return false;
        }
        return true;
    }
}
