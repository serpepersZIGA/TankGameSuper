package com.mygdx.game.main;
import Content.Particle.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.mygdx.game.Event.EventRegister;
import com.mygdx.game.FunctionalComponent.FunctionalBullet.FunctionalComponentBulletRegister;
import com.mygdx.game.Inventory.*;
import com.mygdx.game.MapFunction.MapScan;
import com.mygdx.game.Shader.LightingMainSystem;
import com.mygdx.game.Sound.SoundRegister;
import com.mygdx.game.block.Block;
import com.mygdx.game.block.BlockMap;
import Content.Block.Air;
import com.mygdx.game.build.*;
import com.mygdx.game.bull.Bullet;
import Data.DataImage;
import com.mygdx.game.bull.BulletRegister;
import com.mygdx.game.bull.Updater.UpdateRegister;
import com.mygdx.game.menu.InputWindow;
import com.mygdx.game.menu.MapAllLoad;
import com.mygdx.game.menu.button.*;
import com.mygdx.game.method.*;
import com.mygdx.game.object_map.MapObject;
import com.mygdx.game.object_map.VoidObject;
import com.mygdx.game.particle.*;
import com.mygdx.game.Sound.DataSound;
import com.mygdx.game.unit.*;
import com.mygdx.game.unit.CollisionUnit.CollisionMethodGlobal;
import com.mygdx.game.unit.Controller.RegisterController;
import com.mygdx.game.unit.Fire.FireRegister;
import com.mygdx.game.FunctionalComponent.FunctionalUnit.FunctionalComponentUnitRegister;
import com.mygdx.game.unit.PlayerSpawnList.PlayerAllLoad;
import com.mygdx.game.unit.SpawnPlayer.PlayerSpawnData;
import com.mygdx.game.unit.SpawnPlayer.PlayerSpawnListData;
import com.mygdx.game.unit.moduleUnit.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import static com.mygdx.game.unit.SpawnPlayer.PlayerSpawnListData.PlayerSpawnCannonVoid;
import static com.mygdx.game.unit.TransportRegister.PlayerCannonFlameA2;
import static com.mygdx.game.unit.TransportRegister.TrackSoldatT1;


public class Main extends ApplicationAdapter {
	public static ArrayList<Unit> UnitList = new ArrayList<>();
	public static ArrayList<Building> BuildingList = new ArrayList<>();
	public static ArrayList<Bullet> BulletList = new ArrayList<>();
	public static LinkedList<Particle> FlameStaticList = new LinkedList<>();
	public static ArrayList<Button>ButtonList = new ArrayList<>();
	public static LinkedList<Particle> FlameList = new LinkedList<>();
	public static LinkedList<Particle> BangList = new LinkedList<>();
	public static LinkedList<Particle> FlameParticleList = new LinkedList<>();
	public static LinkedList<Particle> LiquidList = new LinkedList<>();
	public static LinkedList<Particle> FlameSpawnList = new LinkedList<>();
	public static ArrayList<Unit> DebrisList = new ArrayList<>();

	public static DataSound ContentSound;
	public static ArrayList<ArrayList<Block>> AirList = new ArrayList<>();
	public static ArrayList<ArrayList<Block>> BlockList2D = new ArrayList<>();

	public static RenderCenter RC;
	public static DataImage ContentImage;
	public static SpriteBatch Batch;
	public static Keyboard KeyboardObj;
	public static int screenWidth;
	public static int screenHeight;
	public static float Zoom = 1,ZoomWindowX,ZoomWindowY;
	public static AI Ai;
	public static boolean EnumerationList;
	public static ActionGame ActionGame;
	public static boolean GameStart;
	public static int FPS;
	public static boolean GameHost;
	public static int width_block_2, height_block_2,x_block,y_block,width_block= 50,height_block =50,width_block_air= 12,height_block_air =12,quantity_width,quantity_height;
	public static int width_block_zoom= 50,height_block_zoom =50,width_block_render= 64,height_block_render =64;
	public static float radius_air_max = 150,radius_air_max_zoom;
	public static ServerMain serverMain;
	public static ClientMain Main_client;
	public static Option Option;
	public static PackerServer PacketServer;
	public static MapObject VoidObj;
	public static PacketBuildingServer PacketBuildingServer;
	public static Packet_client PacketClient;
	public static int TickBlock,TickBlockMax = 600;
	public static BitmapFont font,font2;
	public static byte ConfigMenu;
	public static InputWindow InputWindow;
	public static int xMaxAir;
	public static int yMaxAir;
	public static int xMap ;
	public static int yMap ;
	public static Texture textureBuffer;
	public static EventRegister EventData;
	public static int IDClient;
	public static UpdateBuildingRegister BuildingRegister;
	public static  ArrayList<Packet_client> Clients = new ArrayList<>();
	public static PlayerSpawnData SpawnPlayer;
	public static String SpawnIDPlayer;
	public static CycleTimeDay CycleDayNight;
	public static int flame_spawn_time,flame_spawn_time_max = 20;
	public static RegisterController RegisterControl;
	public static FunctionalComponentUnitRegister RegisterFunctionalComponent;
	public static CollisionMethodGlobal Collision;
	public static ArrayList<Unit> ClearUnitList = new ArrayList<>();
	public static ArrayList<Unit> ClearDebrisList = new ArrayList<>();
	public static ArrayList<PacketInventory> InventoryPack = new ArrayList<>();
	public static InventoryInterface inventoryMain;
	public static ArrayList<ItemPacket>ItemPackList = new ArrayList<>();
	public static LightingMainSystem LightSystem;
	public static RenderPrimitive Render;



	public Main(int x,int y,int FPS){
		screenWidth = x;
		screenHeight = y;
		Main.FPS = FPS;
		ZoomWindowX = (float) screenWidth /1920;
		ZoomWindowY = (float) screenHeight /1000;

	}




	public static void spawn_object(){
		//PlayerList.add(new PlayerCannonFlame(200,200, PlayerList,true));
		//SoldatList.add(new SoldatBull(1200,200, UnitList));
		MapScan.MapInput("Map/maps/MapBase.mapt");
		MapAllLoad.MapCount();
		TrackSoldatT1.UnitAdd(2000,1200,true, (byte) 2,
				RegisterControl.controllerBotSupport,new Inventory(new Item[3][4]));
		PlayerCannonFlameA2.UnitAdd(1200,1200,true, (byte) 2,
				RegisterControl.controllerBot,new Inventory(new Item[3][4]));

		//UnitList.add(new TrackSoldatT1(2700,2000,Main.UnitList,true,(byte)2));
		LiquidList.add(new Acid(200,200));
		LiquidList.add(new Blood(200,200));
		FlameSpawnList.add(new FlameSpawn(200,200));
		FlameStaticList.add(new FlameStatic(200,200));
		BangList.add(new Bang(200,200,10));
	}
	public void field(int width_field,int height_field){
		quantity_width = width_field;
		quantity_height = height_field;
		width_block_2 = width_block/2;
		height_block_2 = height_block/2;
		width_block*=1.24;
		height_block*=1.24;
		boolean confWallY = false;
		boolean confWallX;
		x_block = width_block_2;
		y_block = 0;
		for(int i = 0;i<quantity_height;i++){
			BlockList2D.add(new ArrayList<>());
			if(y_block == 0){
				confWallY = true;
			}
			else if(i == quantity_height){
				confWallY = true;
			}
			y_block += height_block;
			x_block = 0;
			confWallX = true;


			for(int i2 = 0;i2<quantity_width;i2++){
				x_block += width_block;
				BlockList2D.get(i).add(new BlockMap(x_block,y_block));
				if(quantity_width == i2){
					confWallX = true;
				}
				if(confWallX || confWallY){
					BlockList2D.get(i).get(i2).passability= true;
					confWallX = false;

				}


			}
			confWallY = false;
		}
		//width_block-= 1;
		//height_block-= 1;
//		quantity_width = (int)(screenWidth/width_block_air);
//		quantity_height = (int)(screenHeight/height_block_air);
//		y_block = -height_block_air;
//		for(int i = 0; i<quantity_height+1;i++){
//			AirList.add(new ArrayList<>());
//			y_block += height_block_air;
//			x_block = -width_block_air;
//			for(int i2 = 0; i2<quantity_width+1;i2++){
//				x_block += width_block_air;
//				AirList.get(i).add(new Air(x_block,y_block));
//
//			}
//		}
//		MapLighting = new boolean[quantity_height][quantity_width];
//		for(int i = 0;i<quantity_height;i++){
//			for(int i2 = 0;i2<quantity_width;i2++){
//				MapLighting[i][i2] = BlockList2D.get(i).get(i2).passability;
//			}
//		}

		LiquidList.add(new Acid(200,200));
		LiquidList.add(new Blood(200,200));
		FlameSpawnList.add(new FlameSpawn(200,200));
		FlameStaticList.add(new FlameStatic(200,200));
		BangList.add(new Bang(200,200,10));
	}

	@Override
	public void create () {
		RegisterFunctionalComponent = new FunctionalComponentUnitRegister();
		FunctionalComponentBulletRegister.FunctionalComponentBulletRegisters();
		UpdateRegister.UpdateBulletRegisterCreate();
		LightSystem = new LightingMainSystem();
		LightSystem.setAmbientColor(new Color(0,0,0,1f));


		VoidObj = new VoidObject();
		textureBuffer = new Texture("image/infantry/soldat_enemy.png");
		ContentImage = new DataImage();
		ContentSound = new DataSound();
		Collision = new CollisionMethodGlobal();
		inventoryMain = new InventoryInterface();
		SoundRegister.SoundAdd();
		FireRegister.Create();
		BulletRegister.BulletRegisterAdd();
		RegisterControl = new RegisterController();

		RegisterModuleCannon.Create();
		RegisterModuleEngine.Create();
		RegisterModuleCorpus.Create();
		RegisterModuleSoldat.Create();
		GunRegister.Create();
		ItemRegister.Create();
		InventoryPack = new ArrayList<>();//new PacketInventory();
		CycleDayNight = new CycleTimeDay(5,5,3,3,0.3f,0.95f);
		BuildingRegister = new UpdateBuildingRegister();
		PacketBuildingServer = new PacketBuildingServer();

		Render = new RenderPrimitive();
//		Render = new ShapeRenderer(128,LightSystem.shader);
//		Matrix4 u_projTrans = new Matrix4();
//		Render.setTransformMatrix(u_projTrans);
//		u_projTrans.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



		RC = new RenderCenter(0,0);
		Batch = new SpriteBatch();
		font = TXTFont((int) (64*ZoomWindowX),"font/Base/BaseFont4.ttf");
		font2 = TXTFont((int) (16*ZoomWindowX),"font/Base/BaseFont.ttf");
		InputWindow = new InputWindow();
		EventData = new EventRegister();
		PlayerSpawnListData.Create();
		KeyboardObj = new Keyboard();
		Keyboard.ZoomMaxMin();
		Main.Zoom = 1;
		Gdx.input.setInputProcessor(KeyboardObj);
		Option = new Option();
		Ai = new AI();
		TransportRegister.Create();
		field(160, 160);
		spawn_object();
		ButtonList.add(new Play(100,600,400,120,"PLAY",(byte)0));
		ButtonList.add(new PlayHost(100,800,400,120,"HOST",(byte)1));
		ButtonList.add(new PlayClient(100,600,400,120,"CONNECT",(byte)1));
		ButtonList.add(new Cancel(100,400,400,120,"CANCEL",(byte)1));
		ButtonList.add(new Maps(100,400,400,120,"MAPS",(byte)0));
		ButtonList.add(new Exit(100,200,400,120,"Exit",(byte)0));
		ButtonList.add(new Cancel(100,400,400,120,"CANCEL",(byte)3));
		PlayerAllLoad.PlayerCount();
		ActionGame = new ActionMenu();
		RC.const_xy_block();
		xMap = Main.BlockList2D.get(0).size();
		yMap = Main.BlockList2D.size();
		SpawnPlayer = PlayerSpawnCannonVoid;
		//Render.begin(ShapeRenderer.ShapeType.Filled);
		//Render.setAutoShapeType(true);

		//viewport = new StretchViewport(ZoomWindowX, ZoomWindowY, camera);
		//viewport = new StretchViewport(ZoomWindowX, ZoomWindowY, camera);
		KeyboardObj.zoom_const();
	}
	public static BitmapFont TXTFont(int size,String fontPath){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		parameter.size = size;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}
	@Override
	public void render () {
		ActionGame.action();
		//LightSystem.clearLights();
	}
	@Override
	public void dispose () {
		ContentSound.dispose();
		textureBuffer.dispose();
		BlockList2D.clear();
		BuildingList.clear();
		AirList.clear();
		FlameList.clear();
		FlameSpawnList.clear();
		LiquidList.clear();
		UnitList.clear();
		DebrisList.clear();
		FlameParticleList.clear();
		FlameStaticList.clear();
		ButtonList.clear();
		KeyboardObj = null;
		RC= null;
		Clients= null;
		Batch.dispose();
		Render.dispose();
		font.dispose();
		font2.dispose();
		if(ServerMain.Server != null) {
			try {
				ServerMain.Server.dispose();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else if(ClientMain.Client != null) {
			try {
				ClientMain.Client.dispose();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		Gdx.app.exit();
		System.exit(0);
		super.dispose();
	}
}