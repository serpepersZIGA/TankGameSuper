package com.mygdx.game.MapFunction;

// A small, dependency-free 2D value-noise field, seeded and deterministic:
// the same (seed, x, y) always gives the same value, with no Random object
// or mutable state involved - just a hash of the lattice coordinates. That's
// what lets both the map generator and, later, the terrain painter re-derive
// the exact same road path or biome field independently, from the seed
// alone, without needing to save anything extra.
//
// fbm() (fractal Brownian motion - a sum of octaves at doubling frequency
// and shrinking amplitude) is what actually gets used for terrain: a low
// base frequency gives slow, wide biome regions, while adding a couple of
// higher-frequency octaves on top gives fine detail without breaking up the
// large-scale shape.
public class TerrainNoise {
    private final long seed;

    public TerrainNoise(long seed){
        this.seed = seed;
    }

    private float latticeValue(int x, int y, int channel){
        long h = x*374761393L + y*668265263L + seed*2246822519L + channel*3266489917L;
        h = (h ^ (h >>> 13)) * 1274126177L;
        h = h ^ (h >>> 16);
        return ((h & 0xFFFFFFFFL) / (float) 0xFFFFFFFFL) * 2f - 1f;
    }

    private static float smooth(float t){
        return t*t*t*(t*(t*6f-15f)+10f);
    }

    /** Continuous value noise in roughly [-1,1] at (x,y), for the given independent channel. */
    public float noise(float x, float y, int channel){
        int x0 = (int) Math.floor(x), y0 = (int) Math.floor(y);
        int x1 = x0+1, y1 = y0+1;
        float sx = smooth(x-x0), sy = smooth(y-y0);
        float n00 = latticeValue(x0, y0, channel);
        float n10 = latticeValue(x1, y0, channel);
        float n01 = latticeValue(x0, y1, channel);
        float n11 = latticeValue(x1, y1, channel);
        float ix0 = n00 + (n10-n00)*sx;
        float ix1 = n01 + (n11-n01)*sx;
        return ix0 + (ix1-ix0)*sy;
    }

    /** Fractal sum of octaves, roughly in [-1,1]. Lower baseFrequency = bigger features. */
    public float fbm(float x, float y, int channel, int octaves, float baseFrequency, float persistence){
        float total = 0f, amplitude = 1f, frequency = baseFrequency, maxValue = 0f;
        for (int i = 0; i < octaves; i++){
            total += noise(x*frequency, y*frequency, channel)*amplitude;
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= 2f;
        }
        return total/maxValue;
    }
}
