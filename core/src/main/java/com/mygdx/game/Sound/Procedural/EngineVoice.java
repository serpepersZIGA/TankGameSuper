package com.mygdx.game.Sound.Procedural;

import java.util.Random;

// A tank's engine drone - one instance per tank now, not just the local
// player's. A continuous, saturated multi-harmonic tone carries the actual
// roar/weight, with a rhythmic "chug" wobble in volume riding on top of it
// (not replacing it) for the diesel/tractor character - an earlier pass let
// the tone drop to near-silence between chugs, which made it sound like a
// thin toy motor instead of something with real body behind it.
//
// pitchScale (pass a per-tank seed via the constructor) shifts this
// instance's whole frequency range up or down a bit, so several tanks
// audible at once are actually distinguishable from each other instead of
// blending into one smeared drone.
//
// Call setState() every frame with the driven tank's speed/throttle and how
// attenuated it should be for the listener's distance (1 = full volume, 0 =
// inaudible) - stop() marks the voice finished so AudioMixer drops it once
// the tank it belongs to dies/despawns.
public class EngineVoice implements Voice {
    private static final float IDLE_FREQ = 24f;
    private static final float FREQ_PER_SPEED = 17f;
    private static final float IDLE_VOLUME = 0.22f;
    private static final float THROTTLE_VOLUME = 0.48f;
    private static final float SMOOTHING = 0.0008f;
    private static final float CHUG_FLOOR = 0.55f;
    private static final float DRIVE = 2.2f;
    private static final float GROWL_RATE = 13f;
    private static final float GROWL_DEPTH = 0.12f;
    // how much harder the engine sounds like it's working when the terrain
    // underneath is dragging on it (mud etc, see Unit.terrainLoad) - more
    // distortion drive and more noise grit, not just louder
    private static final float LOAD_DRIVE = 1.6f;
    private static final float LOAD_NOISE = 0.25f;

    private final float pitchScale;
    private volatile float targetFreq;
    private volatile float targetVolume = 0f;
    private volatile float targetLoad = 0f;
    private volatile boolean finished = false;
    private float curFreq;
    private float curVolume = 0f;
    private float curLoad = 0f;
    private float phase = 0f;
    private float pulseEnv = 0f;
    private float noiseFilter = 0f;
    private float growlPhase = 0f;
    private final Random rand = new Random();

    public EngineVoice(){
        this(0L);
    }
    // seed picks this tank's pitch variation (roughly 0.85x-1.15x) - use
    // something stable per-tank (e.g. its id/hashCode), not a fresh random
    // each frame, or its pitch would wander instead of staying its own note
    public EngineVoice(long seed){
        Random seeded = new Random(seed);
        this.pitchScale = 0.85f + seeded.nextFloat()*0.3f;
        this.curFreq = IDLE_FREQ*pitchScale;
        this.targetFreq = curFreq;
    }

    public void setState(Float speed, boolean throttling, float attenuation){
        setState(speed, throttling, attenuation, 0f);
    }
    // load: 0 = normal ground, higher = the terrain is dragging on the tank
    // (see Unit.terrainLoad) - makes the engine sound strained, not just slow
    public void setState(Float speed, boolean throttling, float attenuation, float load){
        if (speed == null || attenuation <= 0f) { targetVolume = 0f; return; }
        targetFreq = (IDLE_FREQ + Math.abs(speed)*FREQ_PER_SPEED)*pitchScale;
        targetVolume = (throttling ? THROTTLE_VOLUME : IDLE_VOLUME)*attenuation;
        targetLoad = load;
    }
    public void stop(){
        finished = true;
    }

    @Override
    public float nextSample(float sampleRate){
        curFreq += (targetFreq-curFreq)*SMOOTHING;
        curVolume += (targetVolume-curVolume)*SMOOTHING;
        curLoad += (targetLoad-curLoad)*SMOOTHING;
        phase += curFreq/sampleRate;
        if (phase >= 1f) {
            phase -= 1f;
            pulseEnv = 1f;
        }
        float samplesPerHalfCycle = Math.max(sampleRate/curFreq/2f, 1f);
        float decay = (float) Math.exp(Math.log(0.05)/samplesPerHalfCycle);
        pulseEnv *= decay;

        float sub = (float) Math.sin(phase*Math.PI)*0.7f;
        float h1 = (float) Math.sin(phase*Math.PI*2);
        float h2 = (float) Math.sin(phase*Math.PI*4)*0.45f;
        float h3 = (float) Math.sin(phase*Math.PI*6)*0.3f;
        float h4 = (float) Math.sin(phase*Math.PI*8)*0.18f;
        float tone = (float) Math.tanh((sub+h1+h2+h3+h4)*(DRIVE+curLoad*LOAD_DRIVE));

        float noise = rand.nextFloat()*2f-1f;
        noiseFilter += (noise-noiseFilter)*0.2f;

        growlPhase += GROWL_RATE/sampleRate;
        float growl = 1f + (float) Math.sin(growlPhase*Math.PI*2)*GROWL_DEPTH;

        float chug = CHUG_FLOOR + (1f-CHUG_FLOOR)*pulseEnv;
        return (tone*chug*growl*0.85f + noiseFilter*(0.15f+curLoad*LOAD_NOISE))*curVolume;
    }
    @Override
    public boolean isFinished(){
        return finished;
    }
}
