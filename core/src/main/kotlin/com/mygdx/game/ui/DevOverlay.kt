package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.mygdx.game.main.Main

// tiny FPS counter, only drawn when DevFlags.showFps is on
object DevOverlay {
    fun render() {
        if (!DevFlags.showFps) return
        val font = Main.font2 ?: return
        // the world render leaves the lighting shader attached to Batch and never clears it,
        // so plain text drawn through it comes out wrong - force the default shader for this draw
        Main.Batch.shader = null
        Main.Batch.begin()
        font.draw(Main.Batch, "FPS: ${Gdx.graphics.framesPerSecond}", 20f, Gdx.graphics.height - 20f)
        Main.Batch.end()
    }
}
