package com.mygdx.game.MapFunction;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.block.Block;
import com.mygdx.game.main.Main;

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
    private static final float BIOME_FREQ = 0.00045f;
    private static final float DETAIL_FREQ = 0.006f;
    private static final float PATCH_FREQ = 0.0015f;

    private static final TerrainMaterial[] MATERIALS = {
            TerrainMaterial.GRASS, TerrainMaterial.SNOW, TerrainMaterial.ICE,
            TerrainMaterial.SAND, TerrainMaterial.GRAVEL, TerrainMaterial.SWAMP, TerrainMaterial.PUDDLE
    };

    public static void paint(long seed, int width, int height){
        TerrainNoise noise = new TerrainNoise(seed);
        boolean[][] road = ProceduralMapGenerator.computeRoadCells(seed, width, height);
        int blockSize = Main.width_block;
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Block block = Main.BlockList2D.get(y).get(x);
                boolean isRoad = road[y][x];
                float[] bl = cornerBlend(noise, x*blockSize, y*blockSize, isRoad);
                float[] tl = cornerBlend(noise, x*blockSize, (y+1)*blockSize, isRoad);
                float[] tr = cornerBlend(noise, (x+1)*blockSize, (y+1)*blockSize, isRoad);
                float[] br = cornerBlend(noise, (x+1)*blockSize, y*blockSize, isRoad);

                block.hasTerrainPaint = true;
                block.terrainColorBL = new Color(bl[0], bl[1], bl[2], 1f).toFloatBits();
                block.terrainColorTL = new Color(tl[0], tl[1], tl[2], 1f).toFloatBits();
                block.terrainColorTR = new Color(tr[0], tr[1], tr[2], 1f).toFloatBits();
                block.terrainColorBR = new Color(br[0], br[1], br[2], 1f).toFloatBits();

                float speed = (bl[3]+tl[3]+tr[3]+br[3])/4f;
                float friction = (bl[4]+tl[4]+tr[4]+br[4])/4f;
                block.terrainSpeedMultiplier = speed;
                block.terrainFrictionMultiplier = friction;
            }
        }
    }

    /** {r, g, b, speedMultiplier, frictionMultiplier} at one exact world point. */
    private static float[] cornerBlend(TerrainNoise noise, float wx, float wy, boolean road){
        if (road) {
            Color c = TerrainMaterial.ASPHALT.color;
            return new float[]{c.r, c.g, c.b, TerrainMaterial.ASPHALT.speedMultiplier, TerrainMaterial.ASPHALT.frictionMultiplier};
        }

        float temp = noise.fbm(wx, wy, 0, 3, BIOME_FREQ, 0.5f);
        float moisture = noise.fbm(wx, wy, 1, 3, BIOME_FREQ, 0.5f);
        float roughness = noise.fbm(wx, wy, 2, 2, BIOME_FREQ*1.7f, 0.5f);
        float detail = noise.fbm(wx, wy, 3, 2, DETAIL_FREQ, 0.5f);
        float icePatch = noise.fbm(wx, wy, 4, 2, PATCH_FREQ, 0.5f);
        float puddlePatch = noise.fbm(wx, wy, 5, 2, PATCH_FREQ, 0.5f);

        float cold = smoothstep(-0.15f, -0.5f, temp);
        float hotDry = smoothstep(0.15f, 0.45f, temp) * smoothstep(0.1f, -0.3f, moisture);
        float wet = smoothstep(0.05f, 0.4f, moisture) * (1f-hotDry);
        float rough = smoothstep(-0.1f, 0.35f, roughness) * (1f-cold) * (1f-hotDry) * (1f-wet);
        float ice = cold * smoothstep(0.3f, 0.6f, icePatch);
        float puddle = wet * smoothstep(0.35f, 0.6f, puddlePatch);
        float snow = Math.max(0f, cold-ice);
        float swamp = Math.max(0f, wet-puddle);
        float grass = Math.max(0f, 1f - cold - hotDry - wet - rough);

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
