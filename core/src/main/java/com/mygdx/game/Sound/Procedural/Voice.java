package com.mygdx.game.Sound.Procedural;

// One synthesized sound source, mixed sample-by-sample by AudioMixer along
// with whatever else is currently playing. A continuous voice (the engine
// drone) just always returns false from isFinished(); a one-shot voice
// (gunfire, an impact) reports true once its envelope has run out, and the
// mixer drops it.
public interface Voice {
    float nextSample(float sampleRate);
    boolean isFinished();
}
