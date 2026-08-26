package com.mygdx.game.Sound.Procedural;

import java.util.Random;

// Tank explosion/death - a sharp crack transient right up front, then a
// deep, long sub-bass boom underneath it. Considerably bigger and longer
// than a plain ImpactVoice hit. One-shot: finishes on its own.
public class ExplosionVoice implements Voice {
    private static final float DURATION = 1.2f;
    private final float volume;
    private float t = 0f;
    private float lowFilter = 0f;
    private float crackFilter = 0f;
    private final Random rand = new Random();

    public ExplosionVoice(float volume){
        this.volume = volume;
    }
    @Override
    public float nextSample(float sampleRate){
        t += 1f/sampleRate;
        if (t > DURATION) return 0f;
        float crackEnv = (float) Math.exp(-t*35f);
        float boomEnv = (float) Math.exp(-t*3.2f);
        float noise = rand.nextFloat()*2f-1f;
        lowFilter += (noise-lowFilter)*0.012f;
        crackFilter += (noise-crackFilter)*0.5f;
        float boom = lowFilter*boomEnv;
        float crack = crackFilter*crackEnv*0.7f;
        return (float) Math.tanh((boom*1.6f + crack)*1.2f)*volume;
    }
    @Override
    public boolean isFinished(){
        return t > DURATION;
    }
}
