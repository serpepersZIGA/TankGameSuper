package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import Content.Particle.Acid;
import Content.Particle.FlameSpawn;
import com.mygdx.game.build.BuildPacket;
import com.mygdx.game.method.CycleTimeDay;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.object_map.MapObject;
import com.mygdx.game.unit.DebrisPacket;
import com.mygdx.game.Inventory.PacketInventory;
import com.mygdx.game.unit.Unit;
import com.mygdx.game.unit.TransportPacket;

import static com.mygdx.game.Inventory.ItemObject.ItemList;

import static com.mygdx.game.Sound.SoundRegister.SoundPack;
import static com.mygdx.game.build.BuildRegister.PacketBuilding;
import static com.mygdx.game.bull.BulletRegister.PacketBull;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.main.ServerMain.Server;
import static com.mygdx.game.unit.TransportRegister.*;

public class ActionGameHost extends com.mygdx.game.main.ActionGame {
    private int i;
    private static int timer = 0;
    public void action() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RC.method();
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
                        timer = 30;


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
        //boolean[]mouse_e = new metod.mouse_control().mouse_event();
        //Main.player_obj.get(1).all_action_client(Main.left_mouse_client, Main.right_mouse_client, Main.mouse_x_client,
                //Main.mouse_y_client, Main.press_w_client, Main.press_a_client, Main.press_s_client, Main.press_d_client);
        if(Unit.ai_sost != 0){
            Unit.ai_sost-=1;
            Unit.AIScan = false;}
        else {
            Unit.ai_sost=1000;
            Unit.AIScan = true;
        }
        if(flame_spawn_time > 0){flame_spawn_time-=1;}
        Batch.begin();
        Render.begin();
        LightSystem.begin(Batch);
        Main.RC.render_block();
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
            if(Main.BulletList.get(i).height == 1) {
                Main.BulletList.get(i).all_action();
            }
        }
        Render.end();
        Batch.begin();
        Render.begin();
        for (i= 0; i< Main.FlameSpawnList.size(); i++){
            Main.FlameSpawnList.get(i).all_action(i);
        }
        for(int i = 0;i<ItemList.size();i++){
            ItemList.get(i).IterationItem();
        }
        for(i = 0;i< DebrisList.size();i++) {
            Unit debris = DebrisList.get(i);
            packet_debris_server(debris);
            debris.all_action();
            debris.corpus_corpus(Main.UnitList);
            debris.corpus_corpus(Main.DebrisList);
        }
        for(i = 0;i< UnitList.size();i++) {
            Unit unit = UnitList.get(i);
            packet_player_server(unit);
            if (unit.host || unit.control == RegisterControl.controllerBot
                    || unit.control == RegisterControl.controllerBotSupport
                    || unit.control == RegisterControl.controllerSoldatTransport
                    || unit.control == RegisterControl.controllerSoldatBot
                    || unit.control == RegisterControl.controllerHelicopter) {
                unit.all_action();
            } else {
                unit.all_action_client();
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
            Unit unit = DebrisList.get(i);
            unit.UpdateUnit();
            //unit.update();

        }
        RC.BuildingIteration();
        inventoryMain.InventoryIteration();
        //System.out.println(inventoryMain.SlotInventory.length);
        Render.end();

        Render.begin();

        for (i = 0; i< Main.BulletList.size(); i++){
            if(Main.BulletList.get(i).height == 2) {
                Main.BulletList.get(i).all_action();
            }
        }
        for (i= 0; i< Main.BangList.size(); i++){
            Main.BangList.get(i).all_action(i);}
        Render.end();
        Batch.end();
        //LightSystem.end(Batch);
        server_packet();
        //System.out.println(UnitList.size());
//        if(Unit.ai_sost == 0){
//            Unit.AIScan = false;}
        if(flame_spawn_time <= 0){flame_spawn_time=flame_spawn_time_max;}
        CycleDayNight.WorkTime();
        Collision.CollisionIterationGlobal();
    }
    private void server_packet() {
        if(packetUnitUpdate.ConfUnitList || packetUnitUpdate.ConfDebrisList){
            Server.sendToAllTCP(packetUnitUpdate);
            if(packetUnitUpdate.ConfUnitList){
                for(Unit unit : ClearUnitList){
                    UnitList.remove(unit);
                }
                ClearUnitList.clear();
            }
            else if(packetUnitUpdate.ConfDebrisList){
                for(Unit unit : ClearDebrisList){
                    DebrisList.remove(unit);
                }
                ClearDebrisList.clear();
            }
        }
//        if(EnumerationList){
//            for (i = 0; i < Main.UnitList.size(); i++) {
                //packet_player_server(Main.UnitList.get(i));
//            }
//            PacketUnit.clear();
//            EnumerationList = false;
//        }
        PacketServer.item = ItemPackList;
        PacketServer.debris = PacketDebris;
        PacketServer.player = PacketUnit;
        PacketServer.bull = PacketBull;
        PacketServer.building = PacketBuilding;
        packetInventoryServer();
        PacketServer.sound = SoundPack;
        PacketServer.mapObject = MapObject.PacketMapObjects;
        PacketServer.TotalLight = CycleTimeDay.lightTotal;
        Server.sendToAllUDP(PacketServer);
        SoundPack.clear();
        PacketServer.inventory.clear();
        MapObject.PacketMapObjects.clear();
        packetUnitUpdate.ConfDebrisList = false;
        packetUnitUpdate.ConfUnitList = false;
        ItemPackList.clear();
        PacketUnit.clear();
        PacketBull.clear();
        PacketDebris.clear();
        PacketBuilding.clear();
    }
    private void packet_player_server(Unit unit){
        PacketUnit.add(new TransportPacket());
        TransportPacket pack = PacketUnit.get(i);
        pack.name = unit.type_unit;
        pack.x = unit.x;
        pack.y = unit.y;
        pack.PlayerConf = unit.PlayerConf;
        pack.rotation_corpus = unit.rotation_corpus;
        pack.hp = unit.hp;
        pack.team = unit.team;
        pack.speed = unit.speed;
        pack.host = unit.host;
        pack.IDClient = unit.nConnect;
        pack.ID = unit.ID;
        for (Unit Tower : unit.tower_obj){
            pack.reloadTower.add(Tower.reload);
            pack.rotation_tower_2.add(Tower.rotation_tower);
        }
    }
    public static void packetInventoryServer(){
        for (int i = 0;i<UnitList.size();i++) {
            Unit unit = UnitList.get(i);
            PacketInventory pack = new PacketInventory();
            pack.Inventory = new String[unit.inventory.InventorySlots.length][unit.inventory.InventorySlots[0].length];
            //InventoryPack.add(new PacketInventory());
            //PacketInventory pack = InventoryPack.get(InventoryPack.size() - 1);
            for (int ix = 0;ix<unit.inventory.InventorySlots.length;ix++) {
                //pack.Inventory[ix][0]
                for (int iy = 0;iy<unit.inventory.InventorySlots[ix].length;iy++) {
                    if (unit.inventory.InventorySlots[ix][iy] != null) {
                        pack.Inventory[ix][iy] = unit.inventory.InventorySlots[ix][iy].ID;
                    } else {
                        pack.Inventory[ix][iy] = null;
                    }
                }
            }
            PacketServer.inventory.add(pack);
        }
    }
    private void packet_debris_server(Unit unit){
        PacketDebris.add(new DebrisPacket());
        DebrisPacket pack = PacketDebris.get(i);
        pack.UnitID = unit.ID;
        pack.x = unit.x;
        pack.y = unit.y;
        pack.rotation = unit.rotation_corpus;
    }
    public void PacketBuildServer(int i){
        PacketBuilding.add(new BuildPacket());
        PacketBuilding.get(i).name = BuildingList.get(i).name;
        PacketBuilding.get(i).x = BuildingList.get(i).x;
        PacketBuilding.get(i).y = BuildingList.get(i).y;
    }
}
