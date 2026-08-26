package com.mygdx.game.MapFunction;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.block.Block;
import com.mygdx.game.main.Main;

// Paints ground color + movement effects directly onto the already-loaded
// BlockList2D, using nothing but continuous noise fields keyed off the same
// seed the layout generator used - no PNG tiles, no per-tile texture seams,
// since every cell's color/physics is a smooth function of its world
// position rather than a lookup into a small fixed palette of images.
//
// Every non-road cell is a WEIGHTED BLEND across all candidate materials
// (temperature/moisture/roughness scores, each a smoothstep of a noise
// field), not a hard pick - that's what keeps every biome boundary looking
// like an organic gradient instead of a line, and what makes the crossing
// between two biomes read as its own distinct in-between color rather than
// a sudden cut. Road cells (from the same seeded path the layout generator
// traced - recomputed here from the same seed, not stored anywhere) are
// pure asphalt with no blend - a real road edge is reasonably sharp, unlike
// a biome-to-biome transition.
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
        for (int y = 1; y < height-1; y++){
            for (int x = 1; x < width-1; x++){
                Block block = Main.BlockList2D.get(y).get(x);
                if (block.passability) continue; // a building sits here, leave its sprite alone
                if (road[y][x]) {
                    apply(block, TerrainMaterial.ASPHALT.color, TerrainMaterial.ASPHALT.speedMultiplier, TerrainMaterial.ASPHALT.frictionMultiplier);
                } else {
                    paintCell(block, noise, x, y);
                }
            }
        }
    }

    private static void paintCell(Block block, TerrainNoise noise, int x, int y){
        // sampled in world pixel coordinates, not grid indices - the
        // frequencies below are tuned against real driving distance, and
        // need to stay meaningful regardless of how fine/coarse the grid is
        float wx = block.x, wy = block.y;
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

        // a bit of continuous per-cell brightness variation so a biome's
        // core doesn't read as one flat color - kept small so it stays a
        // texture, not a mess of colors
        float shade = 1f + detail*0.08f;
        r = clamp01(r*shade); g = clamp01(g*shade); b = clamp01(b*shade);

        apply(block, new Color(r, g, b, 1f), speed, friction);
    }

    private static void apply(Block block, Color color, float speedMultiplier, float frictionMultiplier){
        block.hasTerrainPaint = true;
        block.terrainColorBits = color.toFloatBits();
        block.terrainSpeedMultiplier = speedMultiplier;
        block.terrainFrictionMultiplier = frictionMultiplier;
    }

    private static float smoothstep(float edge0, float edge1, float x){
        float t = clamp01((x-edge0)/(edge1-edge0));
        return t*t*(3f-2f*t);
    }

    private static float clamp01(float v){
        return Math.max(0f, Math.min(1f, v));
    }
}
