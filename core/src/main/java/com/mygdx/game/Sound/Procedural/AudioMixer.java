package com.mygdx.game.Sound.Procedural;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// One real audio output, mixing together however many procedurally
// synthesized voices are currently playing - continuous engine/track drones
// per tank plus short one-shot voices (gunfire, impacts) layered on top, all
// generated live instead of replaying a fixed sample. Runs its own thread
// because AudioDevice.writeSamples blocks until the backend wants more data,
// which would stall the render thread if called there directly.
//
// Persistent voices (an engine, a set of tracks) are pruned the same way
// one-shots are - by isFinished() - so a per-tank voice just needs a stop()
// call when that tank dies/despawns to get cleaned up automatically. One-shot
// voices are additionally capped in count: without that, a burst of bullets
// landing in the same instant would pile up an unbounded number of
// simultaneous voices, which is what was turning into unlistenable mush
// under fire instead of distinct hits.
public class AudioMixer {
    private static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = 1024;
    private static final int MAX_ONE_SHOTS = 8;
    // a single volume knob for the whole procedural audio system - it never
    // went through the regular sound-effects slider (Option.SoundProcent),
    // since that only ever scaled the old sample-based SoundPlay calls, so
    // without this there was no way to turn engines/gunfire/impacts down
    // independently of everything else. Set from GameSettings.
    public static volatile float masterVolume = 1f;

    private final List<Voice> persistent = new CopyOnWriteArrayList<>();
    private final List<Voice> oneShots = new CopyOnWriteArrayList<>();
    private AudioDevice device;
    private Thread thread;
    private volatile boolean running;

    public void start(){
        if (running) return;
        running = true;
        device = Gdx.audio.newAudioDevice(SAMPLE_RATE, true);
        thread = new Thread(this::run, "audio-mixer");
        thread.setDaemon(true);
        thread.start();
    }
    public void stop(){
        running = false;
        if (thread != null) {
            try { thread.join(200); } catch (InterruptedException ignored) {}
            thread = null;
        }
        if (device != null) {
            device.dispose();
            device = null;
        }
        persistent.clear();
        oneShots.clear();
    }
    public boolean isRunning(){
        return running;
    }
    // a continuous voice (an engine, a track loop) - stays in the mix until
    // its own isFinished() says otherwise (call stop() on it when the tank
    // it belongs to dies/despawns)
    public void playPersistent(Voice voice){
        persistent.add(voice);
    }
    // a one-shot effect (gunfire, an impact, a bullet hit) - plays itself out
    // and gets dropped automatically; capped so a burst of simultaneous hits
    // can't pile into an unbounded wall of noise, dropping the oldest to make
    // room for the newest
    public void play(Voice voice){
        while (oneShots.size() >= MAX_ONE_SHOTS) {
            oneShots.remove(0);
        }
        oneShots.add(voice);
    }

    private void run(){
        float[] buffer = new float[BUFFER_SIZE];
        while (running) {
            for (int i = 0; i < BUFFER_SIZE; i++){
                float sum = 0f;
                for (Voice voice : persistent) {
                    sum += voice.nextSample(SAMPLE_RATE);
                }
                for (Voice voice : oneShots) {
                    sum += voice.nextSample(SAMPLE_RATE);
                }
                // soft clip instead of a hard cutoff, so several voices
                // overlapping loudly doesn't crackle
                buffer[i] = (float) Math.tanh(sum)*masterVolume;
            }
            persistent.removeIf(Voice::isFinished);
            oneShots.removeIf(Voice::isFinished);
            device.writeSamples(buffer, 0, BUFFER_SIZE);
        }
    }
}
