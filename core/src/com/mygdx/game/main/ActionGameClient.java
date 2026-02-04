package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import Content.Particle.Acid;
import Content.Particle.FlameSpawn;
import com.mygdx.game.Network.SoundPacket;
import com.mygdx.game.Shader.FlameShader;
import com.mygdx.game.Shader.LiquidShader;
import com.mygdx.game.Sound.SoundPlay;
import com.mygdx.game.Sound.SoundRegister;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.Network.DebrisPacket;
import com.mygdx.game.method.RenderCenter;
import com.mygdx.game.unit.SpawnPlayer.SpawnPlayerPack;
import com.mygdx.game.unit.Unit;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.mygdx.game.Inventory.ItemObject.ItemList;
import static com.mygdx.game.Sound.SoundRegister.IDSound;
import static com.mygdx.game.Weather.WeatherMainSystem.RippleIteration;
import static com.mygdx.game.Weather.WeatherMainSystem.WeatherIteration;
import static com.mygdx.game.bull.Bullet.BulletListDown;
import static com.mygdx.game.bull.Bullet.BulletListUp;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.main.ClientMain.Client;
import static com.mygdx.game.method.Option.SoundConst;
import static com.mygdx.game.method.pow2.pow2;
import static com.mygdx.game.unit.TransportRegister.*;
import static java.lang.StrictMath.sqrt;


public class ActionGameClient extends ActionGame {
    private static Callable<ArrayList<Bullet>> BulletThreadIteration;
    private static Callable<ArrayList<Unit>> UnitThreadIteration;
    private static Callable<ArrayList<Unit>> DebrisThreadIteration;

    public void ThreadAllAdd(){
        BulletThreadIteration = IterationBullet();

        UnitThreadIteration = IterationUnit();

        DebrisThreadIteration = IterationDebris();

    }
    public static void ActionGameClientIteration(){
        SpawnPlayerPack pack = new SpawnPlayerPack();
        //inventoryMain = new InventoryInterface(new Inventory(new Item[4][4]),200,200,500,400);
        pack.ID = SpawnIDPlayer;
        Client.sendTCP(pack);
    }
    int i;
    private static int timer = 0;
    @Override final
    public void action() throws ExecutionException, InterruptedException {
        Future<ArrayList<Bullet>> BulletFutureIteration = executor.submit(BulletThreadIteration);
        BulletFutureIteration.get();
        Future<ArrayList<Unit>>UnitFutureIteration = executor.submit(UnitThreadIteration);
        UnitFutureIteration.get();
        Future<ArrayList<Unit>>DebrisFutureIteration = executor.submit(DebrisThreadIteration);
        DebrisFutureIteration.get();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RC.method();
        if(RC.UnitCamera==null){
            if(Keyboard.PressW){
                RC.y += 10;
            }
            if(Keyboard.PressS){
                RC.y -= 10;
            }
            if(Keyboard.PressA){
                RC.x -= 10;
            }
            if(Keyboard.PressD){
                RC.x += 10;
            }
            try {
                if(timer <= 0) {

                    if (Keyboard.LeftMouse) {
                        FlameSpawnList.add(new FlameSpawn(Keyboard.MouseX / Zoom + RC.x2,Keyboard.MouseY / Zoom + RC.y2));
                        timer = 60;


                    }
                    if (Keyboard.RightMouse) {
                        //main.Main.bang_obj.add(new particle.bang(mouse_x,mouse_y,new Color(236,124,38),12));
                        LiquidList.add(new Acid(Keyboard.MouseX / Zoom + RC.x2,Keyboard.MouseY / Zoom + RC.y2));
                        //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
                        //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
                        //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));

                    }
                }
                else{timer-= 1;}
            }
            catch(Exception ignored){

            }

        }



        Batch.begin();
        Render.polyBatch.begin();
        RC.render_block();
        RippleIteration(Batch);

        if(flame_spawn_time > 0){flame_spawn_time-=1;}
        LiquidShader.AcidShaderIteration();
        for (i= 0; i< LiquidList.size(); i++){
            LiquidList.get(i).all_action();}
        Batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        LiquidShader.BloodShaderIteration();
        for (i= 0; i< BloodList.size(); i++){
            BloodList.get(i).all_action();}
        Batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        for (i = 0; i< FlameStaticList.size(); i++){
            FlameStaticList.get(i).all_action();}
        for (i = 0; i< FlameList.size(); i++){
            FlameList.get(i).all_action();}
        for (i = 0; i< FlameParticleList.size(); i++){
            FlameParticleList.get(i).all_action();}

        Render.polyBatch.flush();

        FlameShader.FlameShaderIteration();
        for (i= 0; i< FlameSpawnList.size(); i++){
            FlameSpawnList.get(i).all_action();
        }
        Batch.setShader(LightSystem.shader);
        //Batch.flush();
        for(int i = 0;i<ItemList.size();i++){
            ItemList.get(i).IterationItemClient();
        }
        Render.polyBatch.flush();

        for (i = 0;i< BulletListDown.size();i++){
            Bullet bullet = BulletListDown.get(i);
            bullet.update();
        }
        for(i = 0;i< UnitList.size();i++) {
            Unit unit = UnitList.get(i);
            if(unit.height == 1) {
                unit.UpdateUnit();
                unit.update();
                for (Unit tower : unit.tower_obj){
                    tower.updateTower();
                }
            }
        }
        for(Unit unit : DebrisList) {
            unit.UpdateUnit();

        }

        SoundPacket pack;
        while (!SoundRegister.SoundPack.isEmpty()) {
            pack =  SoundRegister.SoundPack.get(0);
            SoundPlay.sound((Sound) IDSound.get(pack.ID)[0],
                    1f - ((float) sqrt(pow2(RC.x - pack.ix) + pow2(RC.y - pack.iy)) / SoundConst));
            SoundRegister.SoundPack.remove(pack);
        }



        RC.BuildingUpdate();

        for (i = 0;i< BulletListUp.size();i++){
            BulletListUp.get(i).update();

        }

        for(i = 0;i< UnitList.size();i++) {
            Unit unit = UnitList.get(i);
            if(unit.height == 2) {
                unit.UpdateUnit();
                unit.update();
                for (Unit tower : unit.tower_obj){
                    tower.updateTower();
                }
            }
        }

        inventoryMain.InventoryIterationClient();

        for (i= 0; i< BangList.size(); i++){
            BangList.get(i).all_action();
        }
        if(flame_spawn_time < 0){flame_spawn_time=flame_spawn_time_max;}
        Render.polyBatch.end();
        Batch.end();
        WeatherIteration(Batch);
        LightSystem.begin(Batch);
        //PackUpdateUnit();
    }
    public static void PackUpdateUnit(){
        if(packetUnitUpdate.ConfUnitList){
            Main_client.UnitCreate();
            packetUnitUpdate.ConfUnitList = false;
        }
        if(packetUnitUpdate.ConfDebrisList){
            DebrisList.clear();
            for (DebrisPacket packetDebris : PacketDebris) {
                Main_client.debris_create(packetDebris);
                Main_client.debris_data_add(packetDebris);
            }
            KeyboardObj.ZoomConstTransport();
            packetUnitUpdate.ConfDebrisList = false;
        }
    }
    private static Callable<ArrayList<Unit>> IterationDebris() {
        return () -> {
            for (int i = 0;i< DebrisList.size();i++){
                DebrisList.get(i).all_action_client();
                //Main_client.debris_data(debris);
            }
            return DebrisList; // результат
        };
    }
    private static Callable<ArrayList<Bullet>> IterationBullet() {
        return () -> {
            for (int i = 0; i< BulletList.size(); i++){
                Bullet bullet = BulletList.get(i);
                if(bullet != null) {
                    bullet.all_action_client();

                }
            }
            return BulletList; // результат
        };
    }
    private static Callable<ArrayList<Unit>> IterationUnit() {
        return () -> {
            for(int i = 0;i< UnitList.size();i++) {
                Unit unit = UnitList.get(i);
                //Main_client.player_data(unit);
                if(unit.host || unit.nConnect != IDClient) {
                    unit.all_action_client_2();
                }
                else {
                    //System.out.println(unit.nConnect+" "+IDClient);
                    unit.all_action_client_1();
                }
            }
            return UnitList; // результат
        };
    }
}
