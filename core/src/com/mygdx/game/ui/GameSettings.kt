package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.mygdx.game.method.Option

/**
 * Player-configurable options, persisted with libGDX Preferences (a small file
 * under the OS's standard app-data location) so they survive between launches.
 * This replaces ad hoc, hand-parsed .txt files as the way settings are stored.
 */
object GameSettings {
    private const val PREFS_NAME = "com.mygdx.game.settings"
    private const val KEY_SOUND_VOLUME = "soundVolume"
    private const val DEFAULT_SOUND_VOLUME = 0.5f

    private val prefs get() = Gdx.app.getPreferences(PREFS_NAME)

    var soundVolume: Float = DEFAULT_SOUND_VOLUME
        private set

    /** Loads persisted settings and applies them. Call once during startup. */
    fun load() {
        soundVolume = prefs.getFloat(KEY_SOUND_VOLUME, DEFAULT_SOUND_VOLUME)
        applySoundVolume()
    }

    /** Updates, applies and persists the sound volume (0..1). */
    fun setSoundVolume(value: Float) {
        soundVolume = value.coerceIn(0f, 1f)
        applySoundVolume()
        prefs.putFloat(KEY_SOUND_VOLUME, soundVolume)
        prefs.flush()
    }

    private fun applySoundVolume() {
        // Option.SoundProcent is the field the rest of the game already reads
        // to scale sound effect volume; 0.2f matches the old sound slider's range.
        Option.SoundProcent = soundVolume * 0.2f
    }
}
