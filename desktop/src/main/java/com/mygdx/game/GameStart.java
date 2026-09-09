package com.mygdx.game;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.main.Main;


// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class GameStart {
	public static Lwjgl3ApplicationConfiguration config;
	public static int WidthWindow,HeightWindow;
	public static void main (String[] arg) {
		KotlinToolchainInfo.logStartupInfo();
		config = new Lwjgl3ApplicationConfiguration();
        config.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL30, 3, 0);

		// Ask GLFW itself for the monitor size instead of going through java.awt.
		// AWT reports display bounds scaled by the OS's logical DPI factor, while
		// GLFW/LWJGL3 (which actually creates the window and reports mouse
		// coordinates at runtime) works in raw monitor pixels. On Linux desktops
		// that apply fractional/HiDPI scaling those two disagree, so windows sized
		// via AWT end up a different size than what GLFW thinks the screen is -
		// every mouse click then lands offset from where it visually appears.
		// Querying GLFW directly keeps window size and mouse coordinates in the
		// same coordinate space on every platform.
		DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
		WidthWindow = displayMode.width;
		HeightWindow = displayMode.height - 100;

		config.setWindowedMode(WidthWindow,HeightWindow);
		config.useVsync(true);
		// 0 = uncapped - paired with vsync so the monitor's own refresh is the
		// only thing pacing frames here. Main.create() re-applies the user's
		// saved vsync preference (and the matching FPS-cap pairing) right
		// after the window exists, via GraphicsSettings.setVsync - see there
		// for why a mismatched hard FPS cap alongside vsync causes stutter.
		config.setForegroundFPS(0);
		config.setTitle("Game");
		config.setWindowIcon("image/player/tower_player.png");
		new Lwjgl3Application(new Main(WidthWindow,HeightWindow,120), config);

	}
}
