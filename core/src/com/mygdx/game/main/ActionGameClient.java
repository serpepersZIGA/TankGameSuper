package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import Content.Particle.Acid;
import Content.Particle.FlameSpawn;
import com.mygdx.game.Inventory.Inventory;
import com.mygdx.game.Inventory.InventoryInterface;
import com.mygdx.game.Inventory.Item;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.unit.DebrisPacket;
import com.mygdx.game.unit.SpawnPlayer.SpawnPlayerPack;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.Inventory.ItemObject.ItemList;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.main.ClientMain.Client;
import static com.mygdx.game.unit.TransportRegister.*;


public class ActionGameClient extends com.mygdx.game.main.ActionGame {
    public ActionGameClient(){
        SpawnPlayerPack pack = new SpawnPlayerPack();
        //inventoryMain = new InventoryInterface(new Inventory(new Item[4][4]),200,200,500,400);
        pack.ID = SpawnIDPlayer;
        Client.sendTCP(pack);

    }
    int i;
    private static int timer = 0;
    public void action() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Main.RC.method();
        if(Main.UnitList.size()==0){
            if(Keyboard.PressW){
                Main.RC.y += 10;
            }
            if(Keyboard.PressS){
                Main.RC.y -= 10;
            }
            if(Keyboard.PressA){
                Main.RC.x -= 10;
            }
            if(Keyboard.PressD){
                Main.RC.x += 10;
            }
            try {
                if(timer <= 0) {

                    if (Keyboard.LeftMouse) {
                        Main.FlameSpawnList.add(new FlameSpawn(Keyboard.MouseX / Zoom + RC.x2,Keyboard.MouseY / Zoom + RC.y2));
                        timer = 60;


                    }
                    if (Keyboard.RightMouse) {
                        //main.Main.bang_obj.add(new particle.bang(mouse_x,mouse_y,new Color(236,124,38),12));
                        Main.LiquidList.add(new Acid(Keyboard.MouseX / Zoom + RC.x2,Keyboard.MouseY / Zoom + RC.y2));
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
        LightSystem.begin(Batch);
        Render.begin();
        Main.RC.render_block();
        if(flame_spawn_time > 0){flame_spawn_time-=1;}
        Batch.end();
        for (i= 0; i< Main.LiquidList.size(); i++){
            Main.LiquidList.get(i).all_action(i);}
        for (i = 0; i< Main.FlameStaticList.size(); i++){
            Main.FlameStaticList.get(i).all_action(i);}
        for (i = 0; i< Main.FlameList.size(); i++){
            Main.FlameList.get(i).all_action(i);}
        for (i = 0; i< Main.FlameParticleList.size(); i++){
            Main.FlameParticleList.get(i).all_action(i);}
        for (i = 0; i< Main.BulletList.size(); i++){
            Bullet bullet = Main.BulletList.get(i);
            if(bullet != null) {
                if (bullet.height == 1) {
                    bullet.all_action_client();
                }
            }
        }
        Render.end();
        Batch.begin();
        Render.begin();
        for (i= 0; i< Main.FlameSpawnList.size(); i++){
            Main.FlameSpawnList.get(i).all_action(i);
        }
        for(int i = 0;i<ItemList.size();i++){
            ItemList.get(i).IterationItemClient();
        }
        for(i = 0;i< UnitList.size();i++) {
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
        for(i = 0;i< DebrisList.size();i++) {
            DebrisList.get(i).UpdateUnit();

        }

        for (Unit debris : DebrisList){
            debris.all_action_client();
            //Main_client.debris_data(debris);
        }
        RC.BuildingIteration();
        inventoryMain.InventoryIterationClient();
        Batch.draw(textureBuffer,-20,1,1,1);
        Render.end();

        Render.begin();

        for (i = 0; i< Main.BulletList.size(); i++){
            Bullet bullet = Main.BulletList.get(i);
            if(bullet != null) {
                if (bullet.height == 2) {
                    bullet.all_action_client();
                }
            }
        }
        for (i= 0; i< Main.BangList.size(); i++){
            Main.BangList.get(i).all_action(i);
        }
        if(flame_spawn_time < 0){flame_spawn_time=flame_spawn_time_max;}
        Render.end();
        Batch.end();
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
}
