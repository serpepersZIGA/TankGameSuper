package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.I18NBundle
import java.util.Locale

enum class GameLanguage(val locale: Locale, val displayName: String) {
    RUSSIAN(Locale.forLanguageTag("ru"), "Русский"),
    ENGLISH(Locale.forLanguageTag("en"), "English");

    companion object {
        fun fromCode(code: String): GameLanguage = entries.firstOrNull { it.locale.language == code } ?: RUSSIAN
    }
}

/**
 * Wraps a libGDX I18NBundle (assets/i18n/strings*.properties) so every screen
 * shares one small, real translation table instead of hard-coded strings, and
 * the interface language can be switched from Settings.
 *
 * strings_ru.properties and strings_en.properties hold the two supported
 * languages; strings.properties (no suffix) is a Russian copy kept only as
 * I18NBundle's ultimate fallback. Both languages need their own dedicated
 * file: if a requested locale has no exact file, I18NBundle's fallback chain
 * tries the JVM's default locale *before* settling for the no-suffix root
 * bundle - and Gradle's forked JVMs run with -Duser.language=en, which would
 * silently turn a request for Russian into the English bundle if no
 * strings_ru.properties existed.
 */
object Localization {

    private val listeners = mutableListOf<() -> Unit>()

    var language: GameLanguage = GameLanguage.RUSSIAN
        private set

    private lateinit var bundle: I18NBundle

    fun init(language: GameLanguage) {
        this.language = language
        bundle = I18NBundle.createBundle(Gdx.files.internal("i18n/strings"), language.locale)
    }

    fun setLanguage(language: GameLanguage) {
        if (this.language == language) return
        init(language)
        GameSettings.setLanguage(language)
        listeners.forEach { it() }
    }

    fun tr(key: String): String = if (::bundle.isInitialized) bundle.get(key) else key

    /** Like [tr], but returns [default] instead of throwing when the key is missing. */
    fun trOrDefault(key: String, default: String): String =
        if (::bundle.isInitialized) {
            try {
                bundle.get(key)
            } catch (e: java.util.MissingResourceException) {
                default
            }
        } else default

    /** Registers a callback invoked after the language changes, so open screens can rebuild their text. */
    fun onLanguageChanged(listener: () -> Unit) {
        listeners.add(listener)
    }
}
