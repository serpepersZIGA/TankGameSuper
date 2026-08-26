package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics
import org.lwjgl.glfw.GLFW

enum class WindowMode { FULLSCREEN, WINDOWED, BORDERLESS }

// common resolutions to offer in the windowed dropdown
val COMMON_RESOLUTIONS = listOf(1280 to 720, 1600 to 900, 1920 to 1080, 2560 to 1440, 3840 to 2160)

// actually applies a display mode/resolution/vsync change, and persists it
object GraphicsSettings {

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
