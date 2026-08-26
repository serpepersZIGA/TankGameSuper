package com.mygdx.game.MapFunction;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// Generates a new .mapt file in exactly the sparse command format hand-made
// maps already use (BuildAdd/BlockAdd/MapObject - see an existing map or
// .str structure for reference) - MapScan.MapInput loads whatever this
// produces the same way it loads a hand-authored map, no parser changes
// needed, and the result is a normal file you can keep, share or re-edit
// by hand afterward.
//
// The road network is a handful of hub points connected by a minimum
// spanning tree (so everything's reachable without a dense mesh), with one
// extra edge added for a loop; each connection is a fractal "midpoint
// displacement" path (repeatedly nudging each segment's midpoint sideways
// by a shrinking random amount) rather than a straight line, which is what
// gives it a winding, organic look instead of ruler-straight roads.
// Dirt patches use the same kind of noise-wobbled blob for their edges, so
// they don't read as perfect circles either.
public class ProceduralMapGenerator {
    private static final String[] BUILDINGS = {"BigBuildingWood1", "Building2"};
    private static final int BUILDING_CLEARANCE = 12;
    private static final int ROAD_WIDTH = 2;

    public static String generate(long seed, int width, int height){
        Random rand = new Random(seed);
        StringBuilder sb = new StringBuilder();
        sb.append("^Procedural").append(seed).append(";\n");
        sb.append("/x ").append(width).append(":y ").append(height).append(":;\n\n");

        boolean[][] road = new boolean[height][width];
        boolean[][] dirt = new boolean[height][width];

        int biomeCount = 2 + rand.nextInt(2);
        for (int b = 0; b < biomeCount; b++){
            int cx = margin(rand, width);
            int cy = margin(rand, height);
            int radius = 10 + rand.nextInt(12);
            paintBlob(dirt, width, height, cx, cy, radius);
        }

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

        emitRuns(sb, dirt, width, height, "Dirt");
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                if (road[y][x]) sb.append("BlockAdd:*Asphalt:x").append(x).append(":y").append(y).append(":;\n");
            }
        }

        List<int[]> placedBuildings = new ArrayList<>();
        int buildingTarget = 6 + rand.nextInt(6);
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

        int decorTarget = (width*height)/120;
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

    /** Generates and writes the map to disk, returning the path passed in. */
    public static String generateAndSave(long seed, int width, int height, String path) throws IOException {
        String content = generate(seed, width, height);
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        }
        return path;
    }

    private static int margin(Random rand, int size){
        int m = Math.max(size/10, 8);
        return m + rand.nextInt(Math.max(size-2*m, 1));
    }

    private static void paintBlob(boolean[][] grid, int width, int height, int cx, int cy, int radius){
        for (int y = Math.max(1, cy-radius); y < Math.min(height-1, cy+radius); y++){
            for (int x = Math.max(1, cx-radius); x < Math.min(width-1, cx+radius); x++){
                float dx = x-cx, dy = y-cy;
                float dist = (float) Math.sqrt(dx*dx+dy*dy);
                // per-cell wobble on the effective radius, so the patch edge
                // looks organic instead of a perfect circle
                float wobble = radius*0.25f*((float) Math.sin(x*0.3f+cy)+(float) Math.cos(y*0.3f+cx));
                if (dist < radius+wobble) grid[y][x] = true;
            }
        }
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

    private static void emitRuns(StringBuilder sb, boolean[][] grid, int width, int height, String blockName){
        for (int y = 0; y < height; y++){
            int runStart = -1;
            for (int x = 0; x <= width; x++){
                boolean set = x < width && grid[y][x];
                if (set && runStart < 0) runStart = x;
                else if (!set && runStart >= 0){
                    int len = x-runStart;
                    sb.append("BlockAdd:*").append(blockName).append(":x").append(runStart).append(":y").append(y)
                            .append(":X").append(len).append(":;\n");
                    runStart = -1;
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
