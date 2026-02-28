package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import Content.Particle.Acid;
import Content.Particle.FlameSpawn;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Network.BuildPacket;
import com.mygdx.game.Shader.FlameShader;
import com.mygdx.game.Shader.LiquidShader;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.method.CycleTimeDay;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.object_map.MapObject;
import com.mygdx.game.Network.DebrisPacket;
import com.mygdx.game.Inventory.PacketInventory;
import com.mygdx.game.particle.Particle;
import com.mygdx.game.unit.Unit;
import com.mygdx.game.Network.TransportPacket;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.*;

import static com.badlogic.gdx.graphics.Pixmap.*;
import static com.mygdx.game.Inventory.ItemObject.ItemList;

import static com.mygdx.game.Sound.SoundRegister.SoundPack;
import static com.mygdx.game.Weather.WeatherMainSystem.*;
import static com.mygdx.game.build.BuildRegister.PacketBuilding;
import static com.mygdx.game.bull.Bullet.BulletListDown;
import static com.mygdx.game.bull.Bullet.BulletListUp;
import static com.mygdx.game.bull.BulletRegister.PacketBull;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.main.ServerMain.Server;
import static com.mygdx.game.unit.TransportRegister.*;

public class ActionGameHost extends ActionGame{

    private static Callable<ArrayList<Bullet>> BulletThreadIteration;
    private static Callable<ArrayList<Unit>> UnitThreadIteration;
    private static Callable<ArrayList<Unit>> DebrisThreadIteration;

    //private static IterationBullet IterationBullet = new IterationBullet();

    private int i;
    private static int timer = 0;
    public void ThreadAllAdd(){
        BulletThreadIteration = IterationBullet();

        UnitThreadIteration = IterationUnit();

        DebrisThreadIteration = IterationDebris();


    }
    @Override final
    public void action() throws ExecutionException, InterruptedException {
        //executor = Executors.newFixedThreadPool(1);
        //ThreadAllAddHost();
        Future<ArrayList<Bullet>>BulletFutureIteration = executor.submit(BulletThreadIteration);
        BulletFutureIteration.get();
        Future<ArrayList<Unit>>UnitFutureIteration = executor.submit(UnitThreadIteration);
        UnitFutureIteration.get();
        Future<ArrayList<Unit>>DebrisFutureIteration = executor.submit(DebrisThreadIteration);
        DebrisFutureIteration.get();




        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RC.method();
        //if(RC.UnitCamera==null){
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
            if(Keyboard.PressEsc){
                ActionGameMain = ActionMenu;
                ConfigMenu = 4;
            }
//            try {
//                if(timer <= 0) {
//                    if (Keyboard.LeftMouse) {
//
//                        Main.FlameSpawnList.add(new FlameSpawn(Keyboard.MouseX / Zoom + RC.x2,Keyboard.MouseY / Zoom + RC.y2));
//                        timer = 30;
//
//

//                    }
//                    if (Keyboard.RightMouse) {
//                        //main.Main.bang_obj.add(new particle.bang(mouse_x,mouse_y,new Color(236,124,38),12));
//                        Main.LiquidList.add(new Acid(Keyboard.MouseX / Zoom + RC.x2,Keyboard.MouseY / Zoom + RC.y2));
//                        //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
//                        //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
//                        //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
//
//                    }
//                }
//                else{timer-= 1;}
//            }
//            catch(Exception ignored){
//
//            }
        //}
        //boolean[]mouse_e = new metod.mouse_control().mouse_event();
        //Main.player_obj.get(1).all_action_client(Main.left_mouse_client, Main.right_mouse_client, Main.mouse_x_client,
                //Main.mouse_y_client, Main.press_w_client, Main.press_a_client, Main.press_s_client, Main.press_d_client);
        if(Unit.ai_sost != 0){
            Unit.ai_sost-=1;
            Unit.AIScan = false;}
        else {
            Unit.ai_sost=700;
            Unit.AIScan = true;
        }


        if(flame_spawn_time > 0){flame_spawn_time-=1;}
        Batch.begin();
        Render.polyBatch.begin();
        Main.RC.render_block();
        RippleIteration(Batch);

        LiquidShader.AcidShaderIteration();
        for (i= 0; i< Main.LiquidList.size(); i++){
            Main.LiquidList.get(i).all_action();}
        Batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        LiquidShader.BloodShaderIteration();
        for (i= 0; i< BloodList.size(); i++){
            Main.BloodList.get(i).all_action();}
        Batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        //liquidGlobal();
        for (i = 0; i< Main.FlameStaticList.size(); i++){
            Main.FlameStaticList.get(i).all_action();}
        for (i = 0; i< Main.FlameList.size(); i++){
            Main.FlameList.get(i).all_action();}
        for (i = 0; i< Main.FlameParticleList.size(); i++){
            Main.FlameParticleList.get(i).all_action();}
        Render.polyBatch.flush();

        FlameShader.FlameShaderIteration();
        for (i= 0; i< Main.FlameSpawnList.size(); i++){
            Main.FlameSpawnList.get(i).all_action();
        }
        Batch.setShader(LightSystem.shader);

        for(int i = 0;i<ItemList.size();i++){
            ItemList.get(i).IterationItem();
        }

        Render.polyBatch.flush();

//        for(i = 0;i< DebrisList.size();i++) {
//            Unit debris = DebrisList.get(i);
//            packet_debris_server(debris,i);
//            debris.all_action();
//            debris.corpus_corpus(Main.UnitList);
//            debris.corpus_corpus(Main.DebrisList);
//        }
//        for(i = 0;i< UnitList.size();i++) {
//            Unit unit = UnitList.get(i);
//            packet_player_server(unit);
//            if (unit.host || unit.control == RegisterControl.controllerBot
//                    || unit.control == RegisterControl.controllerBotSupport
//                    || unit.control == RegisterControl.controllerSoldatTransport
//                    || unit.control == RegisterControl.controllerSoldatBot
//                    || unit.control == RegisterControl.controllerHelicopter) {
//                unit.all_action();
//            } else {
//                unit.all_action_client();
//            }
//        }



        //IterationBullet.start();

//        IterationBullet.start();
        for (int i = 0;i< BulletListDown.size();i++){
            BulletListDown.get(i).update();

        }


        for(int i = 0;i< UnitList.size();i++) {
            Unit unit = UnitList.get(i);
            if(unit.height == 1) {
                unit.UpdateUnit();
                unit.update();
                for (Unit tower : unit.tower_obj){
                    tower.updateTower();
                }
            }
        }
        for(Unit u: DebrisList){
            u.UpdateUnit();
        }

        //Batch.flush();

        RC.BuildingUpdate();

        for (i = 0;i< BulletListUp.size();i++){
            BulletListUp.get(i).update();

        }
//        for (i = 0; i< Main.BulletList.size(); i++){
//            if(Main.BulletList.get(i).height == 2) {
//                Main.BulletList.get(i).all_action();
//            }
//        }

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

        inventoryMain.InventoryIteration();
//        Map<Thread,StackTraceElement[]> threads = Thread.getAllStackTraces();
//        for (Map.Entry<Thread, StackTraceElement[]> entry : threads.entrySet()) {
//            System.out.println(entry.getKey());
//        }


        //System.out.println(inventoryMain.SlotInventory.length);

//        for (i = 0; i< Main.BulletList.size(); i++){
//            if(Main.BulletList.get(i).height == 2) {
//                Main.BulletList.get(i).all_action();
//            }
//        }
        //Batch.flush();
        for (i= 0; i< Main.BangList.size(); i++){
            Main.BangList.get(i).all_action();}
        Render.polyBatch.end();
        Batch.end();
        WeatherIteration(Batch);
        LightSystem.begin(Batch);
        //LightSystem.end(Batch);
        server_packet();
        //System.out.println(UnitList.size());
//        if(Unit.ai_sost == 0){
//            Unit.AIScan = false;}
        if(flame_spawn_time <= 0){flame_spawn_time=flame_spawn_time_max;}
        CycleDayNight.WorkTime();
        BulletFutureIteration.cancel(true);
        UnitFutureIteration.cancel(true);
        DebrisFutureIteration.cancel(true);


        //executor.shutdown();
        //executor.close();
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
    public static void packet_player_server(Unit unit,int i){
        PacketUnit.add(new TransportPacket());
        TransportPacket pack = PacketUnit.get(i);
        pack.name = unit.TypeUnit;
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
    public static void packet_debris_server(Unit unit,int i){
        PacketDebris.add(new DebrisPacket());
        DebrisPacket pack = PacketDebris.get(i);
        pack.UnitID = unit.ID;
        pack.x = unit.x;
        pack.y = unit.y;
        pack.rotation = unit.rotation_corpus;
    }
    public void PacketBuildServer(int i){
        PacketBuilding.add(new BuildPacket());
        PacketBuilding.get(i).ID = BuildingList.get(i).ID;
        PacketBuilding.get(i).x = BuildingList.get(i).x;
        PacketBuilding.get(i).y = BuildingList.get(i).y;
    }
    private static Callable<ArrayList<Unit>> IterationDebris() {
        return () -> {
            for(int i = 0;i< DebrisList.size();i++) {
                Unit debris = DebrisList.get(i);
                packet_debris_server(debris,i);
                debris.all_action();
                debris.corpus_corpus(Main.UnitList);
                debris.corpus_corpus(Main.DebrisList);
            }
            return DebrisList; // результат
        };
    }
    private static Callable<ArrayList<Bullet>> IterationBullet() {
        return () -> {
            for (int i = 0; i < BulletList.size(); i++) {
                BulletList.get(i).all_action();

            } // имитация длительной задачи
            return BulletList; // результат
        };
    }
    private static Callable<ArrayList<Unit>> IterationUnit() {
        return () -> {
            for(int i = 0;i< UnitList.size();i++) {
                Unit unit = UnitList.get(i);
                ActionGameHost.packet_player_server(unit,i);
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
            Collision.CollisionIterationGlobal();

            return UnitList; // результат
        };
    }
}

