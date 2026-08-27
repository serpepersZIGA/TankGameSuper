package com.mygdx.game.MapFunction;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.block.Block;
import com.mygdx.game.build.Building;
import com.mygdx.game.main.Main;

import java.util.Random;

// Paints ground color + movement effects directly onto the already-loaded
// BlockList2D, using nothing but continuous noise fields keyed off the same
// seed the layout generator used - no PNG tiles.
//
// A flat fill color per cell (the first version of this) still shows a hard
// edge at every cell boundary no matter how close the neighboring values
// are - any solid-color quad butted against another always has a visible
// seam if the colors differ at all, and they always differ by *something*.
// The fix is to sample color at each cell's 4 CORNERS instead of its
// center, and hand all 4 to the GPU as per-vertex colors on that cell's
// quad (see Block.update()) - the GPU then interpolates smoothly across the
// quad on its own, and since two adjacent cells share the exact same world
// coordinate at their common edge/corner, they get the exact same sampled
// color there, so the grid becomes one continuous gradient with no seam by
// construction, not just "close enough".
//
// Every cell's color/physics is a WEIGHTED BLEND across all candidate
// materials (temperature/moisture/roughness scores, each a smoothstep of a
// noise field), not a hard pick - that's what makes a biome boundary read
// as an organic gradient instead of a line. Road cells (from the same
// seeded path the layout generator traced - recomputed here from the same
// seed, not stored anywhere) are pure asphalt with no blend - a real road
// edge is reasonably sharp, unlike a biome-to-biome transition. Border and
// building-footprint cells are painted the same as everything else (a
// building's own sprite just draws over its footprint) so the map edge
// doesn't read as a leftover ring of the old default grass tile.
public class ProceduralTerrainPainter {
    // lower = bigger biome regions. Also lowered from an earlier pass since
    // biomes still read as a scattered mess - see cornerBlend for the other
    // half of that fix (rough/gravel used to be its own full biome-scale
    // axis independent of temperature/moisture, so it kept adding a third
    // kind of blend everywhere instead of just accenting one region).
    private static final float BIOME_FREQ = 0.00028f;
    private static final float DETAIL_FREQ = 0.006f;
    private static final float PATCH_FREQ = 0.0015f;
    private static final float GRAVEL_PATCH_FREQ = 0.001f;

    private static final TerrainMaterial[] MATERIALS = {
            TerrainMaterial.GRASS, TerrainMaterial.SNOW, TerrainMaterial.ICE,
            TerrainMaterial.SAND, TerrainMaterial.GRAVEL, TerrainMaterial.SWAMP, TerrainMaterial.PUDDLE
    };

    public static void paint(long seed, int width, int height){
        TerrainNoise noise = new TerrainNoise(seed);
        boolean[][] road = ProceduralMapGenerator.computeRoadCells(seed, width, height);
        // every building gets a short worn dirt path back to the nearest
        // road cell - reads as "turn off here" instead of a building that
        // just happens to sit near a road with no way to actually reach it
        boolean[][] path = computePathCells(seed, width, height, road);
        int blockSize = Main.width_block;
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Block block = Main.BlockList2D.get(y).get(x);
                TerrainMaterial surface = road[y][x] ? TerrainMaterial.ASPHALT
                        : (path[y][x] ? TerrainMaterial.PATH : null);
                float[] bl = cornerBlend(noise, x*blockSize, y*blockSize, surface);
                float[] tl = cornerBlend(noise, x*blockSize, (y+1)*blockSize, surface);
                float[] tr = cornerBlend(noise, (x+1)*blockSize, (y+1)*blockSize, surface);
                float[] br = cornerBlend(noise, (x+1)*blockSize, y*blockSize, surface);

                block.hasTerrainPaint = true;
                block.terrainColorBL = new Color(bl[0], bl[1], bl[2], 1f).toFloatBits();
                block.terrainColorTL = new Color(tl[0], tl[1], tl[2], 1f).toFloatBits();
                block.terrainColorTR = new Color(tr[0], tr[1], tr[2], 1f).toFloatBits();
                block.terrainColorBR = new Color(br[0], br[1], br[2], 1f).toFloatBits();

                float speed = (bl[3]+tl[3]+tr[3]+br[3])/4f;
                float friction = (bl[4]+tl[4]+tr[4]+br[4])/4f;
                block.terrainSpeedMultiplier = speed;
                block.terrainFrictionMultiplier = friction;
                // classified at the cell center regardless of road status -
                // a road through a cold region is still a cold region for
                // weather purposes, it just also happens to be paved.
                // Stored as continuous 0..1 factors (not a discrete pick) so
                // weather can cross-fade snow/rain smoothly across a biome
                // boundary instead of snapping the instant you cross a line.
                float[] climate = classifyClimate(noise, x*blockSize+blockSize*0.5f, y*blockSize+blockSize*0.5f);
                block.coldFactor = climate[0];
                block.aridFactor = climate[1];
                block.wetFactor = climate[2];
            }
        }
    }

    /** {coldFactor, aridFactor}, both 0 if out of bounds/unpainted. */
    public static float[] climateAt(float worldX, float worldY){
        int blockSize = Main.width_block;
        int cellX = (int) (worldX/blockSize);
        int cellY = (int) (worldY/blockSize);
        if (cellY < 0 || cellY >= Main.BlockList2D.size() || cellX < 0 || cellX >= Main.BlockList2D.get(cellY).size())
            return new float[]{0f, 0f};
        Block block = Main.BlockList2D.get(cellY).get(cellX);
        return new float[]{block.coldFactor, block.aridFactor};
    }

    /** One dirt-path cell grid per building in Main.BuildingList, each traced to its nearest road cell. */
    private static boolean[][] computePathCells(long seed, int width, int height, boolean[][] road){
        boolean[][] path = new boolean[height][width];
        Random rand = new Random(seed+2);
        for (Building building : Main.BuildingList){
            int[] from = {building.xMatrix, building.yMatrix};
            int[] nearestRoad = nearestRoadCell(road, width, height, from);
            if (nearestRoad == null) continue;
            // width 1 (a footpath, not a two-lane road) and much less
            // fractal wander than the main roads - a path to a specific
            // building reads as a deliberate shortcut, not a winding trail
            ProceduralMapGenerator.tracePath(path, width, height, from, nearestRoad, rand, 1, 0.2f);
        }
        return path;
    }

    private static int[] nearestRoadCell(boolean[][] road, int width, int height, int[] from){
        int bestX = -1, bestY = -1;
        long bestDist = Long.MAX_VALUE;
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                if (!road[y][x]) continue;
                long dx = x-from[0], dy = y-from[1];
                long d = dx*dx+dy*dy;
                if (d < bestDist){ bestDist = d; bestX = x; bestY = y; }
            }
        }
        return bestX < 0 ? null : new int[]{bestX, bestY};
    }

    /** Same temperature/moisture/moisture-patch noise as cornerBlend - {coldFactor, aridFactor, wetFactor}. */
    private static float[] classifyClimate(TerrainNoise noise, float wx, float wy){
        float temp = noise.fbm(wx, wy, 0, 3, BIOME_FREQ, 0.5f);
        float moisture = noise.fbm(wx, wy, 1, 3, BIOME_FREQ, 0.5f);
        float cold = smoothstep(-0.22f, -0.42f, temp);
        float hotDry = smoothstep(0.18f, 0.38f, temp) * smoothstep(0.15f, -0.15f, moisture);
        float wet = smoothstep(0.1f, 0.35f, moisture) * (1f-hotDry) * (1f-cold);
        return new float[]{cold, hotDry, wet};
    }

    /** {r, g, b, speedMultiplier, frictionMultiplier} at one exact world point. surface overrides the biome blend entirely (road/path), null for normal ground. */
    private static float[] cornerBlend(TerrainNoise noise, float wx, float wy, TerrainMaterial surface){
        if (surface != null) {
            Color c = surface.color;
            return new float[]{c.r, c.g, c.b, surface.speedMultiplier, surface.frictionMultiplier};
        }

        float temp = noise.fbm(wx, wy, 0, 3, BIOME_FREQ, 0.5f);
        float moisture = noise.fbm(wx, wy, 1, 3, BIOME_FREQ, 0.5f);
        // gravel used to be its own full biome-scale axis (same frequency
        // class as temp/moisture, independent of both) - that gave every
        // point on the map a THIRD independently-varying blend contributor,
        // so almost nowhere sat clearly inside one dominant material and it
        // all read as a constant scattered mix. Now it's a patchy accent
        // (same style as the ice/puddle patches below) confined to whatever
        // ground is left over once temperature/moisture claim their share -
        // gravel/rocky ground shows up as occasional patches within that
        // "neutral" ground instead of its own competing biome everywhere.
        float gravelPatch = noise.fbm(wx, wy, 2, 2, GRAVEL_PATCH_FREQ, 0.5f);
        float detail = noise.fbm(wx, wy, 3, 2, DETAIL_FREQ, 0.5f);
        float icePatch = noise.fbm(wx, wy, 4, 2, PATCH_FREQ, 0.5f);
        float puddlePatch = noise.fbm(wx, wy, 5, 2, PATCH_FREQ, 0.5f);

        // narrower transition bands than before so more of the map sits
        // clearly inside one dominant biome instead of a constant partial
        // blend of two or three candidates - boundaries are still a smooth
        // gradient, just a shorter one, so it doesn't read as a hard line.
        float cold = smoothstep(-0.22f, -0.42f, temp);
        float hotDry = smoothstep(0.18f, 0.38f, temp) * smoothstep(0.15f, -0.15f, moisture);
        // wet used to only depend on moisture/hotDry, not temperature - so a
        // moisture noise spike deep inside a cold zone (temp and moisture are
        // independent channels) could paint a swamp/puddle patch right in the
        // middle of a snow biome. Gating it by (1-cold) keeps moisture-driven
        // biomes out of cold regions, so each biome stays a single coherent
        // region instead of a mix.
        float wet = smoothstep(0.1f, 0.35f, moisture) * (1f-hotDry) * (1f-cold);
        float ice = cold * smoothstep(0.3f, 0.6f, icePatch);
        float puddle = wet * smoothstep(0.35f, 0.6f, puddlePatch);
        float snow = Math.max(0f, cold-ice);
        float swamp = Math.max(0f, wet-puddle);
        float neutral = Math.max(0f, 1f-cold-hotDry-wet);
        float rough = neutral * smoothstep(0.25f, 0.55f, gravelPatch);
        float grass = Math.max(0f, neutral-rough);

        float[] weights = {grass, snow, ice, hotDry, rough, swamp, puddle};

        float total = 0f, r = 0f, g = 0f, b = 0f, speed = 0f, friction = 0f;
        for (int i = 0; i < weights.length; i++){
            float w = weights[i];
            if (w <= 0f) continue;
            Color c = MATERIALS[i].color;
            r += c.r*w; g += c.g*w; b += c.b*w;
            speed += MATERIALS[i].speedMultiplier*w;
            friction += MATERIALS[i].frictionMultiplier*w;
            total += w;
        }
        if (total <= 0.0001f) {
            total = 1f;
            Color c = TerrainMaterial.GRASS.color;
            r = c.r; g = c.g; b = c.b;
            speed = 1f; friction = 1f;
        }
        r /= total; g /= total; b /= total; speed /= total; friction /= total;

        // a bit of continuous brightness variation so a biome's core doesn't
        // read as one flat color - kept small so it stays a texture, not a
        // mess of colors
        float shade = 1f + detail*0.08f;
        r = clamp01(r*shade); g = clamp01(g*shade); b = clamp01(b*shade);

        return new float[]{r, g, b, speed, friction};
    }

    private static float smoothstep(float edge0, float edge1, float x){
        float t = clamp01((x-edge0)/(edge1-edge0));
        return t*t*(3f-2f*t);
    }

    private static float clamp01(float v){
        return Math.max(0f, Math.min(1f, v));
    }
}
