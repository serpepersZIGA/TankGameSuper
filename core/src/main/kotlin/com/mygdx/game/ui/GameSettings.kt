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
    private const val DEFAULT_SOUND_VOLUME = 0.5f
    private const val MAX_RECENT_SERVERS = 6

    private val prefs get() = Gdx.app.getPreferences(PREFS_NAME)

    var soundVolume: Float = DEFAULT_SOUND_VOLUME
        private set

    var recentServers: List<RecentServer> = emptyList()
        private set

    /** Loads persisted settings and applies them. Call once during startup. */
    fun load() {
        soundVolume = prefs.getFloat(KEY_SOUND_VOLUME, DEFAULT_SOUND_VOLUME)
        applySoundVolume()
        Localization.init(GameLanguage.fromCode(prefs.getString(KEY_LANGUAGE, GameLanguage.RUSSIAN.locale.language)))
        recentServers = parseRecentServers(prefs.getString(KEY_RECENT_SERVERS, ""))
    }

    /** Updates, applies and persists the sound volume (0..1). */
    fun setSoundVolume(value: Float) {
        soundVolume = value.coerceIn(0f, 1f)
        applySoundVolume()
        prefs.putFloat(KEY_SOUND_VOLUME, soundVolume)
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
}
