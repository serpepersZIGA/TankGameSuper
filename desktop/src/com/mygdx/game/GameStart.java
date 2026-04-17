package com.mygdx.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.main.Main;


// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class GameStart {
	public static Lwjgl3ApplicationConfiguration config;
	public static int WidthWindow,HeightWindow;
	public static void main (String[] arg) {
		config = new Lwjgl3ApplicationConfiguration();
        config.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL30, 3, 0);
		//WindowSize("WindowSize/SizeWindow.txt");

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = ge.getScreenDevices();

		GraphicsConfiguration gc = devices[0].getDefaultConfiguration();
		Rectangle bounds = gc.getBounds(); // logical pixels / user space
		AffineTransform tx = gc.getDefaultTransform(); // maps user->device
		double scaleX = tx.getScaleX();
		double scaleY = tx.getScaleY();

		WidthWindow = (int) Math.round(bounds.getWidth() * scaleX);
		HeightWindow = (int) Math.round(bounds.getHeight() * scaleY)-100;

		config.setWindowedMode(WidthWindow,HeightWindow);
		config.useVsync(true);
//		config.title = "Title";
//		config.useGL20 = true;
//		config.height = 640;
		config.setForegroundFPS(120);
		config.setTitle("Game");
		config.setWindowIcon("image/player/tower_player.png");
		new Lwjgl3Application(new Main(WidthWindow,HeightWindow,120), config);

	}
	private static void WindowSize(String path){

		StringBuilder result = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			result.append(br.readLine());

		} catch (IOException e) {
			e.printStackTrace();
			bb(path);
			try {
				BufferedReader br = new BufferedReader(new FileReader(path));
				result.append(br.readLine());
			} catch (IOException ignored) {
			}
		}
		String TxT = result.toString();
		String TotalTxT = "";
		String X = "";
		String Y = "";
		int conf = 0;
		for (int i = 0; i < TxT.length(); i++) {
			char c = TxT.charAt(i);
			if(c == ':'){
				if(conf == 0){
					X = TotalTxT;
				}
				else if(conf == 1){
					Y = TotalTxT;
				}
				TotalTxT = "";
				conf += 1;
			}
			else if(c == ';'){
				WidthWindow = Integer.parseInt(X);
				HeightWindow = Integer.parseInt(Y);
				return;
			}
			else{
				TotalTxT = TotalTxT + c;
			}


		}

	}
	public static void bb(String path){
		new File("WindowSize").mkdirs();
		File myFile = new File(path);
		try {
			myFile.createNewFile();
		} catch (IOException ignored) {
		}
		String data = "1920:1080:;";
		//Path file = Paths.get(path);
		try {
			PrintWriter out = new PrintWriter(path);
			out.println(data);
			out.close();
		} catch (IOException ignored) {
		}
	}
}
