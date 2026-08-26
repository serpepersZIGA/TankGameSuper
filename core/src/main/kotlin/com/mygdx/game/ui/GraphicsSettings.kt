package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics
import org.lwjgl.glfw.GLFW

enum class WindowMode { FULLSCREEN, WINDOWED, BORDERLESS }

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

    fun setVsync(on: Boolean) {
        Gdx.graphics.setVSync(on)
        GameSettings.setVsync(on)
    }
}
