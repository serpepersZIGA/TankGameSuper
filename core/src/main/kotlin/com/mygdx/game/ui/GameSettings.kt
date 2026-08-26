package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.mygdx.game.method.Option

data class RecentServer(val address: String, val port: Int) {
    override fun toString() = "$address:$port"
}

/**
 * Player-configurable options, persisted with libGDX Preferences (a small file
 * under the OS's standard app-data location) so they survive between launches.
 * This replaces ad hoc, hand-parsed .txt files as the way settings are stored.
 */
object GameSettings {
    private const val PREFS_NAME = "com.mygdx.game.settings"
    private const val KEY_SOUND_VOLUME = "soundVolume"
    private const val KEY_LANGUAGE = "language"
    private const val KEY_RECENT_SERVERS = "recentServers"
    private const val KEY_WINDOW_MODE = "windowMode"
    private const val KEY_RES_WIDTH = "resWidth"
    private const val KEY_RES_HEIGHT = "resHeight"
    private const val KEY_VSYNC = "vsync"
    private const val KEY_FRAME_LIMIT_MODE = "frameLimitMode"
    private const val KEY_SHOW_FPS = "showFps"
    private const val KEY_PROCEDURAL_VOLUME = "proceduralVolume"
    private const val DEFAULT_SOUND_VOLUME = 0.5f
    private const val MAX_RECENT_SERVERS = 6

    private val prefs get() = Gdx.app.getPreferences(PREFS_NAME)

    var soundVolume: Float = DEFAULT_SOUND_VOLUME
        private set
    // separate volume for the procedural audio (engine, tracks, gunfire,
    // impacts, explosions - see com.mygdx.game.Sound.Procedural) - it never
    // went through soundVolume/Option.SoundProcent at all, so without its
    // own control it was stuck at a fixed level no matter what the main
    // sound slider was set to
    var proceduralVolume: Float = DEFAULT_SOUND_VOLUME
        private set

    var recentServers: List<RecentServer> = emptyList()
        private set

    var windowMode: WindowMode = WindowMode.FULLSCREEN
        private set
    var resolutionWidth: Int = 1920
        private set
    var resolutionHeight: Int = 1080
        private set
    var frameLimitMode: FrameLimitMode = FrameLimitMode.VSYNC
        private set
    var showFps: Boolean = false
        private set

    /** Loads persisted settings and applies them. Call once during startup. */
    fun load() {
        soundVolume = prefs.getFloat(KEY_SOUND_VOLUME, DEFAULT_SOUND_VOLUME)
        applySoundVolume()
        Localization.init(GameLanguage.fromCode(prefs.getString(KEY_LANGUAGE, GameLanguage.RUSSIAN.locale.language)))
        recentServers = parseRecentServers(prefs.getString(KEY_RECENT_SERVERS, ""))
        windowMode = runCatching { WindowMode.valueOf(prefs.getString(KEY_WINDOW_MODE, WindowMode.FULLSCREEN.name)) }
            .getOrDefault(WindowMode.FULLSCREEN)
        resolutionWidth = prefs.getInteger(KEY_RES_WIDTH, 1920)
        resolutionHeight = prefs.getInteger(KEY_RES_HEIGHT, 1080)
        // migrate the old plain on/off vsync flag if that's all that's there yet
        val legacyVsync = if (prefs.contains(KEY_FRAME_LIMIT_MODE)) null else prefs.getBoolean(KEY_VSYNC, true)
        frameLimitMode = when {
            prefs.contains(KEY_FRAME_LIMIT_MODE) -> FrameLimitMode.fromOrdinalSafe(prefs.getInteger(KEY_FRAME_LIMIT_MODE, 0))
            legacyVsync == false -> FrameLimitMode.FPS_120
            else -> FrameLimitMode.VSYNC
        }
        showFps = prefs.getBoolean(KEY_SHOW_FPS, false)
        proceduralVolume = prefs.getFloat(KEY_PROCEDURAL_VOLUME, DEFAULT_SOUND_VOLUME)
        applyProceduralVolume()
    }

    fun setWindowMode(mode: WindowMode, width: Int, height: Int) {
        windowMode = mode
        resolutionWidth = width
        resolutionHeight = height
        prefs.putString(KEY_WINDOW_MODE, mode.name)
        prefs.putInteger(KEY_RES_WIDTH, width)
        prefs.putInteger(KEY_RES_HEIGHT, height)
        prefs.flush()
    }

    fun setFrameLimitMode(mode: FrameLimitMode) {
        frameLimitMode = mode
        prefs.putInteger(KEY_FRAME_LIMIT_MODE, mode.ordinal)
        prefs.flush()
    }

    fun setShowFps(on: Boolean) {
        showFps = on
        prefs.putBoolean(KEY_SHOW_FPS, on)
        prefs.flush()
    }

    /** Updates, applies and persists the sound volume (0..1). */
    fun setSoundVolume(value: Float) {
        soundVolume = value.coerceIn(0f, 1f)
        applySoundVolume()
        prefs.putFloat(KEY_SOUND_VOLUME, soundVolume)
        prefs.flush()
    }

    /** Updates, applies and persists the procedural-audio volume (0..1). */
    fun setProceduralVolume(value: Float) {
        proceduralVolume = value.coerceIn(0f, 1f)
        applyProceduralVolume()
        prefs.putFloat(KEY_PROCEDURAL_VOLUME, proceduralVolume)
        prefs.flush()
    }

    fun setLanguage(language: GameLanguage) {
        prefs.putString(KEY_LANGUAGE, language.locale.language)
        prefs.flush()
    }

    /** Remembers a server the player just tried to connect to, most-recent first. */
    fun rememberServer(address: String, port: Int) {
        val entry = RecentServer(address, port)
        val updated = listOf(entry) + recentServers.filterNot { it == entry }
        recentServers = updated.take(MAX_RECENT_SERVERS)
        prefs.putString(KEY_RECENT_SERVERS, recentServers.joinToString(";") { "${it.address}:${it.port}" })
        prefs.flush()
    }

    private fun parseRecentServers(raw: String): List<RecentServer> {
        if (raw.isBlank()) return emptyList()
        return raw.split(";").mapNotNull { entry ->
            val parts = entry.split(":")
            val port = parts.getOrNull(1)?.toIntOrNull()
            if (parts.isNotEmpty() && port != null) RecentServer(parts[0], port) else null
        }
    }

    private fun applySoundVolume() {
        // Option.SoundProcent is the field the rest of the game already reads
        // to scale sound effect volume; 0.2f matches the old sound slider's range.
        Option.SoundProcent = soundVolume * 0.2f
    }

    private fun applyProceduralVolume() {
        com.mygdx.game.Sound.Procedural.AudioMixer.masterVolume = proceduralVolume
    }
}
