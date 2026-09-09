package com.mygdx.game.Sound.Procedural;

import java.util.Random;

// A gunshot "crack" - fast attack, short exponential decay, mostly filtered
// noise: a sharp high edge for the crack plus a quick low thump for weight.
// One-shot: finishes on its own once the envelope runs out.
public class GunfireVoice implements Voice {
    private final float volume;
    private final float duration;
    private float t = 0f;
    private float highFilter = 0f;
    private float lowFilter = 0f;
    private final Random rand = new Random();

    public GunfireVoice(float volume){
        this(volume, 0.12f);
    }
    public GunfireVoice(float volume, float duration){
        this.volume = volume;
        this.duration = duration;
    }
    @Override
    public float nextSample(float sampleRate){
        t += 1f/sampleRate;
        if (t > duration) return 0f;
        float envelope = (float) Math.exp(-t*28f);
        float noise = rand.nextFloat()*2f-1f;
        highFilter += (noise-highFilter)*0.9f;
        lowFilter += (noise-lowFilter)*0.05f;
        float crack = (noise-highFilter)*0.6f;
        float thump = lowFilter*0.5f;
        return (crack+thump)*envelope*volume;
    }
    @Override
    public boolean isFinished(){
        return t > duration;
    }
}
