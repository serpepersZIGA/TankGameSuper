package com.mygdx.game;

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
		WindowSize("WindowSize/SizeWindow.txt");
		config.setWindowedMode(WidthWindow,HeightWindow-80);
		config.useVsync(true);
//		config.title = "Title";
//		config.useGL20 = true;
//		config.height = 640;
		config.setForegroundFPS(60);
		config.setTitle("Game");
		config.setWindowIcon("image/player/tower_player_1.png");
		new Lwjgl3Application(new Main(WidthWindow,HeightWindow-80,60), config);

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
			System.out.println(TotalTxT);

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
