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
    // how far off the road a building sits - close enough that the short
    // dirt path ProceduralTerrainPainter traces to it reads as "just off the
    // road", not a trek across the map
    private static final int BUILDING_ROAD_OFFSET_MIN = 6;
    private static final int BUILDING_ROAD_OFFSET_MAX = 16;
    private static final int ROAD_WIDTH = 2;

    /** Default size for a freshly-generated map - see MapSelectScreen. */
    public static final int DEFAULT_SIZE = 260;

    public static String generateLayout(long seed, int width, int height){
        Random rand = new Random(seed+1);
        StringBuilder sb = new StringBuilder();
        sb.append("^Procedural").append(seed).append(";\n");
        sb.append("/x ").append(width).append(":y ").append(height).append(":;\n\n");

        boolean[][] road = computeRoadCells(seed, width, height);
        List<int[]> roadCells = collectRoadCells(road, width, height);

        // buildings used to require a big clearance FROM every road cell -
        // logical roads with nothing on them, but also no way to reach a
        // building except driving cross-country. Now each one sits a short
        // offset from a random point on the actual road network instead, so
        // ProceduralTerrainPainter can trace a short worn dirt path from it
        // back to that road - a real "turn off here" instead of nothing.
        List<int[]> placedBuildings = new ArrayList<>();
        int buildingTarget = 18 + rand.nextInt(15);
        int attempts = 0;
        while (placedBuildings.size() < buildingTarget && attempts < buildingTarget*30 && !roadCells.isEmpty()){
            attempts++;
            int[] roadCell = roadCells.get(rand.nextInt(roadCells.size()));
            float angle = rand.nextFloat()*(float)(Math.PI*2);
            int offset = BUILDING_ROAD_OFFSET_MIN + rand.nextInt(BUILDING_ROAD_OFFSET_MAX-BUILDING_ROAD_OFFSET_MIN);
            int x = roadCell[0] + Math.round((float) Math.cos(angle)*offset);
            int y = roadCell[1] + Math.round((float) Math.sin(angle)*offset);
            if (x < 4 || x >= width-4 || y < 4 || y >= height-4) continue;
            if (!clearOfRoad(road, width, height, x, y, 4)) continue;
            if (!clearOfBuildings(placedBuildings, x, y, BUILDING_CLEARANCE)) continue;
            String building = BUILDINGS[rand.nextInt(BUILDINGS.length)];
            int rotation = rand.nextInt(4);
            sb.append("BuildAdd:B ").append(building).append(":x").append(x).append(":y").append(y)
                    .append(":r").append(rotation).append(":;\n");
            placedBuildings.add(new int[]{x, y});
        }

        // was /150 - a 260x260 map only got ~450 decor objects scattered
        // across it, which read as an almost-empty map. This is still the
        // only decor asset there is (see pepper.json) - more variety needs
        // actual new art, not just a density change.
        int decorTarget = (width*height)/45;
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
        int hubCount = 4 + rand.nextInt(3);
        List<int[]> hubs = new ArrayList<>();
        for (int h = 0; h < hubCount; h++){
            hubs.add(pickHub(rand, width, height, hubs));
        }
        List<int[]> edges = minimumSpanningTree(hubs);
        // a lone bonus edge (the old approach) gives at most one accidental
        // loop - real road networks fork wherever two hubs just happen to be
        // near each other anyway. Any hub pair not already connected by the
        // MST, but not much farther apart than the longest MST edge already
        // is, gets a direct connection too - that's what actually produces a
        // proper 3+-way junction instead of a single tree with one extra
        // link tacked on.
        double longestMstEdge = 0;
        for (int[] edge : edges) longestMstEdge = Math.max(longestMstEdge, dist(hubs.get(edge[0]), hubs.get(edge[1])));
        Set<Long> connectedPairs = new HashSet<>();
        for (int[] edge : edges) connectedPairs.add(pairKey(edge[0], edge[1]));
        List<int[]> extraEdges = new ArrayList<>();
        for (int a = 0; a < hubs.size(); a++){
            for (int b = a+1; b < hubs.size(); b++){
                if (connectedPairs.contains(pairKey(a, b))) continue;
                if (dist(hubs.get(a), hubs.get(b)) <= longestMstEdge*1.35){
                    extraEdges.add(new int[]{a, b});
                }
            }
        }
        edges.addAll(extraEdges);
        for (int[] edge : edges){
            tracePath(road, width, height, hubs.get(edge[0]), hubs.get(edge[1]), rand);
        }
        return road;
    }

    private static long pairKey(int a, int b){
        int lo = Math.min(a, b), hi = Math.max(a, b);
        return ((long) lo << 32) | hi;
    }

    /** Every true cell in the road grid, for picking a random point along the road network. */
    private static List<int[]> collectRoadCells(boolean[][] road, int width, int height){
        List<int[]> cells = new ArrayList<>();
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                if (road[y][x]) cells.add(new int[]{x, y});
            }
        }
        return cells;
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
        tracePath(road, width, height, from, to, rand, ROAD_WIDTH, 0.5f);
    }

    // package-visible with a width/roughness knob so ProceduralTerrainPainter
    // can reuse the exact same fractal midpoint-displacement tracer for the
    // thin dirt paths that connect a building to the road network, instead
    // of a second copy of this logic
    static void tracePath(boolean[][] road, int width, int height, int[] from, int[] to, Random rand, int stampWidth, float roughnessStart){
        List<float[]> points = new ArrayList<>();
        points.add(new float[]{from[0], from[1]});
        points.add(new float[]{to[0], to[1]});
        float roughness = roughnessStart;
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
            stampSegment(road, width, height, points.get(i), points.get(i+1), stampWidth);
        }
    }

    private static void stampSegment(boolean[][] road, int width, int height, float[] a, float[] b, int stampWidth){
        float dx = b[0]-a[0], dy = b[1]-a[1];
        int steps = Math.max((int) Math.sqrt(dx*dx+dy*dy), 1);
        for (int s = 0; s <= steps; s++){
            float t = (float) s/steps;
            int cx = Math.round(a[0]+dx*t);
            int cy = Math.round(a[1]+dy*t);
            for (int oy = -stampWidth/2; oy <= stampWidth/2; oy++){
                for (int ox = -stampWidth/2; ox <= stampWidth/2; ox++){
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
