package com.mygdx.game.Sound.Procedural;

import java.util.Random;

// A heavy collision thud: a sharp low-passed "punch" transient right at
// contact, then a deep rumbling tail underneath it - no sustained pitched
// tone anywhere in the mix. An earlier version added a slowly-decaying pair
// of sine partials for "metallic ring", but any sustained pure tone reads as
// a resonant hollow object (a struck barrel/drum), not a solid impact.
// Tank/debris hits (metallic) instead get a very short burst of higher,
// faster-decaying filtered noise layered on top - a "clank" transient with
// no ring to it, gone in under 40ms.
public class ImpactVoice implements Voice {
    private static final float DURATION = 0.3f;
    private final float volume;
    private final boolean metallic;
    private float t = 0f;
    private float lowFilter = 0f;
    private float midFilter = 0f;
    private final Random rand = new Random();

    public ImpactVoice(float volume, boolean metallic){
        this.volume = volume;
        this.metallic = metallic;
    }
    @Override
    public float nextSample(float sampleRate){
        t += 1f/sampleRate;
        if (t > DURATION) return 0f;
        float punch = (float) Math.exp(-t*40f);
        float body = (float) Math.exp(-t*8f);
        float noise = rand.nextFloat()*2f-1f;
        lowFilter += (noise-lowFilter)*0.02f;
        float thud = lowFilter*(punch*0.7f+body*0.9f);
        float clank = 0f;
        if (metallic) {
            float clankEnv = (float) Math.exp(-t*70f);
            midFilter += (noise-midFilter)*0.5f;
            clank = midFilter*clankEnv*0.5f;
        }
        return (float) Math.tanh((thud+clank)*1.3f)*volume;
    }
    @Override
    public boolean isFinished(){
        return t > DURATION;
    }
}
