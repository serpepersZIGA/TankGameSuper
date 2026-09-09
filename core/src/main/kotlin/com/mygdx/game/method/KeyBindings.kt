package com.mygdx.game.method

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

/** Every keyboard action the player can rebind - see the Controls tab in SettingsScreen. */
enum class GameAction(val prefsKey: String, val default: Int) {
    MOVE_FORWARD("move_forward", Input.Keys.W),
    MOVE_BACK("move_back", Input.Keys.S),
    MOVE_LEFT("move_left", Input.Keys.A),
    MOVE_RIGHT("move_right", Input.Keys.D),
    TOGGLE_INVENTORY("toggle_inventory", Input.Keys.E),
    TOGGLE_EQUIPMENT("toggle_equipment", Input.Keys.Z),
    TOGGLE_SHOP("toggle_shop", Input.Keys.B),
}

/**
 * Keyboard.java asks this for a keycode instead of hard-coding Input.Keys.*,
 * so a rebind takes effect immediately with no other code needing to change.
 * Saved to a Preferences file so it survives a restart. Escape is
 * deliberately not in here - it's the universal "back/pause" key everywhere
 * in the menus (each screen's own onEscape), rebinding it would mean hunting
 * down every one of those, for a key nobody expects to move anyway.
 */
object KeyBindings {
    private val prefs by lazy { Gdx.app.getPreferences("keybindings") }
    private val bindings = linkedMapOf<GameAction, Int>().apply {
        for (action in GameAction.entries) {
            put(action, prefs.getInteger(action.prefsKey, action.default))
        }
    }

    fun keycodeFor(action: GameAction): Int = bindings[action] ?: action.default

    fun keyName(action: GameAction): String {
        val code = keycodeFor(action)
        return if (code < 0) "-" else Input.Keys.toString(code)
    }

    /** A key can only mean one thing at a time - binding it here unbinds it from wherever it was. */
    fun rebind(action: GameAction, keycode: Int) {
        for (other in GameAction.entries) {
            if (other != action && bindings[other] == keycode) {
                bindings[other] = -1
                prefs.putInteger(other.prefsKey, -1)
            }
        }
        bindings[action] = keycode
        prefs.putInteger(action.prefsKey, keycode)
        prefs.flush()
    }

    fun resetToDefaults() {
        for (action in GameAction.entries) {
            bindings[action] = action.default
            prefs.putInteger(action.prefsKey, action.default)
        }
        prefs.flush()
    }
}
