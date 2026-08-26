package com.mygdx.game.Sound.Procedural;

import java.util.Random;

// A bullet striking armor - short and dry either way. An earlier version
// gave a non-penetrating hit a rising/falling pure-tone "ricochet ping" -
// that's a cartoon sound effect trope, not what a real deflected round
// sounds like, so it's gone. The actual difference now is just texture: a
// penetrating hit is a duller, slightly longer crack (mostly low-mid noise);
// a deflection is a touch shorter and brighter (more high end), since
// bouncing off is a harder, snappier impact than punching through. One-shot:
// finishes on its own.
public class BulletHitVoice implements Voice {
    private static final float DURATION = 0.1f;
    private final float volume;
    private final boolean penetrated;
    private float t = 0f;
    private float noiseFilter = 0f;
    private float highFilter = 0f;
    private final Random rand = new Random();

    public BulletHitVoice(float volume, boolean penetrated){
        this.volume = volume;
        this.penetrated = penetrated;
    }
    @Override
    public float nextSample(float sampleRate){
        t += 1f/sampleRate;
        if (t > DURATION) return 0f;
        float envRate = penetrated ? 55f : 75f;
        float envelope = (float) Math.exp(-t*envRate);
        float noise = rand.nextFloat()*2f-1f;
        noiseFilter += (noise-noiseFilter)*(penetrated ? 0.35f : 0.55f);
        highFilter += (noise-highFilter)*0.85f;
        float mix = penetrated ? noiseFilter : (noiseFilter*0.5f + (noise-highFilter)*0.5f);
        return (float) Math.tanh(mix*envelope*1.4f)*volume;
    }
    @Override
    public boolean isFinished(){
        return t > DURATION;
    }
}
