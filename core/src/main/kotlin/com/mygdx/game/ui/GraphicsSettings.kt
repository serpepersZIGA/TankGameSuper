package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics
import org.lwjgl.glfw.GLFW

enum class WindowMode { FULLSCREEN, WINDOWED, BORDERLESS }

/**
 * One slider, one concept: how frames get paced. VSYNC (slider position 0)
 * locks to the monitor's own refresh with no separate cap fighting it -
 * that fight (a hardcoded FPS cap alongside vsync, at a rate that doesn't
 * evenly divide the monitor's refresh) is what was causing bad stutter
 * before. UNLIMITED (the top of the slider) removes any cap at all. The fps
 * values in between are plain software caps with vsync off.
 */
enum class FrameLimitMode(val fps: Int) {
    VSYNC(0),
    FPS_30(30),
    FPS_60(60),
    FPS_90(90),
    FPS_120(120),
    FPS_144(144),
    FPS_165(165),
    FPS_240(240),
    UNLIMITED(0);

    companion object {
        fun fromOrdinalSafe(i: Int): FrameLimitMode = entries.getOrElse(i) { VSYNC }
    }
}

// actually applies a display mode/resolution/vsync change, and persists it
object GraphicsSettings {

    /** Resolutions the monitor the window is actually on supports right now -
     * not a hardcoded 16:9 list, so ultrawide/16:10/multi-monitor setups get
     * their real options instead of made-up ones. */
    fun availableResolutions(): List<Pair<Int, Int>> =
        Gdx.graphics.getDisplayModes()
            .map { it.width to it.height }
            .distinct()
            .sortedByDescending { (w, h) -> w * h }

    fun apply(mode: WindowMode, width: Int, height: Int) {
        val windowHandle = (Gdx.graphics as? Lwjgl3Graphics)?.window?.windowHandle
        when (mode) {
            WindowMode.FULLSCREEN -> {
                if (windowHandle != null) GLFW.glfwSetWindowAttrib(windowHandle, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE)
                Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
            }
            WindowMode.WINDOWED -> {
                if (windowHandle != null) GLFW.glfwSetWindowAttrib(windowHandle, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE)
                Gdx.graphics.setWindowedMode(width, height)
            }
            WindowMode.BORDERLESS -> {
                val display = Gdx.graphics.displayMode
                if (windowHandle != null) GLFW.glfwSetWindowAttrib(windowHandle, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE)
                Gdx.graphics.setWindowedMode(display.width, display.height)
                (Gdx.graphics as? Lwjgl3Graphics)?.window?.setPosition(0, 0)
            }
        }
        GameSettings.setWindowMode(mode, width, height)
    }

    fun applyFrameLimit(mode: FrameLimitMode) {
        when (mode) {
            FrameLimitMode.VSYNC -> {
                Gdx.graphics.setVSync(true)
                Gdx.graphics.setForegroundFPS(0)
            }
            FrameLimitMode.UNLIMITED -> {
                Gdx.graphics.setVSync(false)
                Gdx.graphics.setForegroundFPS(0)
            }
            else -> {
                Gdx.graphics.setVSync(false)
                Gdx.graphics.setForegroundFPS(mode.fps)
            }
        }
        GameSettings.setFrameLimitMode(mode)
    }
}
