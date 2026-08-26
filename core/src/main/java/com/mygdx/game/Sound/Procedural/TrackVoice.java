package com.mygdx.game.Sound.Procedural;

import java.util.Random;

// A tank's track/tread clank - one instance per tank now, not just the local
// player's. A repeating burst of dull, low-passed filtered noise (no tone,
// same idea as ImpactVoice's clank) whose rate follows how fast that tank is
// actually moving, not a fixed loop. Silent when stopped. Each clank gets a
// little random timing/volume jitter so it doesn't sound like a perfect
// metronome - real track links aren't perfectly even.
//
// rateScale (pass a per-tank seed via the constructor) shifts this
// instance's clank rate a bit, same reasoning as EngineVoice's pitchScale -
// several tanks nearby should sound like several distinct tanks, not one
// blurred clatter.
public class TrackVoice implements Voice {
    private static final float MIN_RATE = 3f;
    private static final float RATE_PER_SPEED = 8f;
    private static final float VOLUME = 0.14f;
    private static final float SMOOTHING = 0.002f;

    private final float rateScale;
    private volatile float targetRate = 0f;
    private volatile float targetVolume = 0f;
    private volatile boolean finished = false;
    private float curRate = 0f;
    private float curVolume = 0f;
    private float phase = 0f;
    private float clankEnv = 0f;
    private float clankGain = 1f;
    private float noiseFilter = 0f;
    private final Random rand = new Random();

    public TrackVoice(){
        this(0L);
    }
    public TrackVoice(long seed){
        Random seeded = new Random(seed*31L+7);
        this.rateScale = 0.85f + seeded.nextFloat()*0.3f;
    }

    // speed: same units as Unit.speed, null/near-zero goes silent.
    // attenuation: 1 = full volume, 0 = inaudible (distance falloff)
    public void setState(Float speed, float attenuation){
        if (speed == null || Math.abs(speed) < 0.05f || attenuation <= 0f) { targetVolume = 0f; return; }
        targetRate = (MIN_RATE + Math.abs(speed)*RATE_PER_SPEED)*rateScale;
        targetVolume = VOLUME*attenuation;
    }
    public void stop(){
        finished = true;
    }

    @Override
    public float nextSample(float sampleRate){
        curRate += (targetRate-curRate)*SMOOTHING;
        curVolume += (targetVolume-curVolume)*SMOOTHING;
        phase += curRate/sampleRate;
        if (phase >= 1f) {
            phase -= 1f;
            phase += (rand.nextFloat()-0.5f)*0.15f;
            clankEnv = 1f;
            clankGain = 0.7f + rand.nextFloat()*0.3f;
        }
        float samplesPerThirdCycle = Math.max(sampleRate/Math.max(curRate,1f)/3f, 1f);
        float decay = (float) Math.exp(Math.log(0.08)/samplesPerThirdCycle);
        clankEnv *= decay;
        float noise = rand.nextFloat()*2f-1f;
        noiseFilter += (noise-noiseFilter)*0.15f;
        return noiseFilter*clankEnv*clankGain*curVolume;
    }
    @Override
    public boolean isFinished(){
        return finished;
    }
}
