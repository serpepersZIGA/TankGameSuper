package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.mygdx.game.main.Main

/**
 * A short-lived on-screen message for host/join failures. Before this, a
 * failed connection was only ever visible in the console log (Gdx.app.error),
 * so a player just saw nothing happen when hosting or joining failed.
 */
object NetworkStatusBanner {
    private const val DISPLAY_SECONDS = 4f
    private var message: String? = null
    private var remaining = 0f

    fun show(text: String) {
        message = text
        remaining = DISPLAY_SECONDS
    }

    /** Draws the current message, if any. Manages its own Batch begin/end pair. */
    fun render() {
        val text = message ?: return
        remaining -= Gdx.graphics.deltaTime
        if (remaining <= 0f) {
            message = null
            return
        }
        val font = Main.font2 ?: return
        val alpha = remaining.coerceAtMost(1f)
        val previousColor = font.color.cpy()
        font.setColor(1f, 0.35f, 0.35f, alpha)
        Main.Batch.shader = null // world render leaves the lighting shader attached, don't draw text through it
        Main.Batch.begin()
        font.draw(Main.Batch, text, 40f, Gdx.graphics.height - 40f)
        Main.Batch.end()
        font.color = previousColor
    }
}
