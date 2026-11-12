package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Inventory.InventoryInterface;
import com.mygdx.game.Inventory.ItemObject;
import com.mygdx.game.Network.PacketUnitUpdate;
import com.mygdx.game.Network.TowerPack;
import com.mygdx.game.Shader.FlameShader;
import com.mygdx.game.Shader.LiquidShader;
import com.mygdx.game.block.Block;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.method.CycleTimeDay;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.method.Method;
import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.object_map.MapObject;
import com.mygdx.game.Network.DebrisPacket;
import com.mygdx.game.unit.AI;
import com.mygdx.game.unit.Squad;
import com.mygdx.game.unit.Unit;
import com.mygdx.game.Network.TransportPacket;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.concurrent.*;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.Inventory.ItemObject.ItemList;

import static com.mygdx.game.Sound.SoundRegister.SoundPack;
import static com.mygdx.game.Weather.WeatherMainSystem.*;
import static com.mygdx.game.block.Block.BlockID;
import static com.mygdx.game.build.BuildRegister.PacketBuilding;
import static com.mygdx.game.bull.BulletRegister.PacketBull;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.main.ServerMain.Server;
import static com.mygdx.game.unit.TransportRegister.*;
import static com.mygdx.game.unit.Unit.AIScan;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

public class ActionGameHost extends ActionGame{

    //private static Callable<ArrayList<Bullet>> BulletThreadIteration;
    private static Callable<ArrayList<Unit>> UnitThreadIteration;
  //  private static Callable<ArrayList<Unit>> DebrisThreadIteration;

    private Thread ThreadIterationDebris;
    private Thread ThreadIterationBullet;
    private Thread ThreadIterationUnit;
    private int e = 0;
    private float TimeGlobals = 0f;

    //private static IterationBullet IterationBullet = new IterationBullet();

    private int i;
    @Override final
    public void action() {
        if(Unit.ai_sost != 0){
            Unit.ai_sost-=1;
            Unit.AIScan = false;}
        else {
            Squad.SquadCreate();
            Unit.ai_sost=700;
//            for (ArrayList<Block> i2:Main.BlockList2D){
//                for (Block i3:i2){
//                    i3.render_block = BlockID.get(1);
//                }
//            }
            Unit.AIScan = true;
        }

        //Unit.yu = 0;
        ThreadIterationDebris = new Thread(new IterationDebris());
        ThreadIterationBullet = new Thread(new IterationBullet());
        ThreadIterationUnit = new Thread(new IterationUnit());

        ThreadIterationDebris.start();
        ThreadIterationBullet.start();
        ThreadIterationUnit.start();
        //executor = Executors.newFixedThreadPool(1);
        //ThreadAllAddHost();
        //Future<ArrayList<Bullet>>BulletFutureIteration = executor.submit(BulletThreadIteration);
        //BulletFutureIteration.get();


        //UnitFutureIteration.get();
        //Future<ArrayList<Unit>>DebrisFutureIteration = executor.submit(DebrisThreadIteration);

        //BulletFutureIteration.get();
        //DebrisList = DebrisThreadIteration.Debris;

        //DebrisFutureIteration.get();




        //Gdx.gl.glClearColor(0.2f, 0.4f, 0.8f, 1f); // R, G, B, Alpha
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
            if(Keyboard.ClickEsc){
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


        if(flame_spawn_time > 0){flame_spawn_time-=1;}
        Batch.begin();
        Render.polyBatch.begin();
        Main.RC.render_block();
        RippleIteration(Batch);

        LiquidShader.AcidShaderIteration();

        //LiquidShader.BloodShaderIteration();


        //liquidGlobal();
//        for (i = 0; i< Main.FlameStaticList.size(); i++){
//            Main.FlameStaticList.get(i).all_action();}
//        for (i = 0; i< Main.FlameList.size(); i++){
//            Main.FlameList.get(i).all_action();}
//        for (i = 0; i< Main.FlameParticleList.size(); i++){
//            Main.FlameParticleList.get(i).all_action();}
        Render.polyBatch.flush();

        FlameShader.FlameShaderIteration();
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
        for (i = 0;i< BulletList.size();i++){
            Bullet bullet = BulletList.get(i);
            if(bullet != null) {
                if(bullet.height == 1) {
                    bullet.update();
                }
            }


        }


        for(int i = 0;i< UnitList.size();i++) {
            Unit unit = UnitList.get(i);
            if(unit.height == 1) {
                unit.UpdateUnit();
                unit.update();
                for (Unit tower : unit.TowerUnitList){
                    tower.updateTower();
                }
            }
        }
        for(int i = 0;i< DebrisList.size();i++){
            Unit unit = DebrisList.get(i);
            unit.UpdateUnit();
        }

        RC.BuildingUpdate();

        for (i = 0;i< BulletList.size();i++){
            Bullet bullet = BulletList.get(i);
            if(bullet != null) {
                if(bullet.height == 2) {
                    bullet.update();
                }
            }
        }


        for(i = 0;i< UnitList.size();i++) {
            Unit unit = UnitList.get(i);
            if(unit.height == 2) {
                unit.UpdateUnit();
                unit.update();
                for (Unit tower : unit.TowerUnitList){
                    tower.updateTower();
                }
            }
        }
        shopMain.InventoryIteration();
        equipmentMain.InventoryIteration();
        inventoryMain.InventoryIteration();
        if(InventoryInterface.SlotBuffer != null){
            InventoryInterface.SlotBuffer.SlotXY();
            InventoryInterface.SlotBuffer.SlotRender();
            InventoryInterface.SlotBuffer.SlotPaste();
        }
        if(InventoryInterface.WindowName.conf){
            InventoryInterface.WindowName.RenderWindow();
        }

        for (i= 0; i< Main.BangList.size(); i++){
            Main.BangList.get(i).all_action();}
        Render.polyBatch.end();
        Batch.end();
        WeatherIteration(Batch);
        LightSystem.begin(Batch);
        if(flame_spawn_time <= 0){flame_spawn_time=flame_spawn_time_max;}
        CycleDayNight.WorkTime();
        while (ThreadIterationBullet.isAlive()||ThreadIterationUnit.isAlive()||ThreadIterationDebris.isAlive()){

        }
        server_packet();

    }
    private static float time;
    private void server_packet() {

        for (Unit unit : ClearUnitList) {
            for (Squad squad : AI.SquadList) {
                if (squad.EnemySquad == unit) {
                    for(Unit unit2 : squad.UnitSquad){
                        if(unit2.EnemyFire == unit){
                            unit2.EnemyFire = null;
                            unit2.trigger_fire = false;
                        }
                        if(unit2.TargetUnit == unit){
                            unit2.TargetUnit = null;
                        }
                    }
                    squad.EnemySquad = null;
                }
            }
            PacketUnit.remove(unit.PackUnit);
            UnitList.remove(unit);
        }
        ClearUnitList.clear();
        for(Bullet bullet : Bullet.BulletListClear){
            bullet.clear();
            //Bullet.BulletListClear.remove(bullet);
        }
        Bullet.BulletListClear.clear();
        i = 0;
        boolean conf;
        for (Bullet bullet : BulletAddList){
            conf = false;
            for(;i<BulletList.size();i++) {
                //Bullet bull = BulletList.get(i);
                if(BulletList.get(i) == null) {
                    bullet.ind = i;
                    Bullet.BulletListInd.add(bullet.ind);
                    BulletList.set(i,bullet);
                    conf = true;
                    break;
                }
            }
            if(!conf) {
                bullet.ind = BulletList.size();
                Bullet.BulletListInd.add(bullet.ind);
                BulletList.add(bullet);
            }
        }
        BulletAddList.clear();
        for (Unit unit : ClearDebrisList) {
            DebrisList.remove(unit);
        }
        ClearDebrisList.clear();
        //System.out.println(RespawnList.size());
        for(int i = 0;i<RespawnList.size();i++){
            Object[] obj = RespawnList.get(i);
            time = (float)obj[3];
            time -= TimeGlobal;
            obj[3] = time;
            //System.out.println(time+" time");
            if(time <0){
                MapObject.SpawnPlayer((String) obj[2]
                        ,(boolean)obj[0]
                        ,(byte)obj[4]
                        ,(int) obj[1]);
                RespawnList.remove(obj);
            }
        }

        PacketServer.building = PacketBuilding;
        PacketServer.debris = PacketDebris;
        PacketServer.player = PacketUnit;
        if(!PacketBull.isEmpty()){
            packetUnitUpdate.bull = PacketBull;
            Server.sendToAllTCP(packetUnitUpdate);
            packetUnitUpdate.bull = null;
        }
        PacketServer.sound = SoundPack;
        PacketServer.mapObject = MapObject.PacketMapObjects;
        PacketServer.TotalLight = CycleTimeDay.lightTotal;

        if(PacketServer.unitConf){
            PacketUnitUpdate pack = new PacketUnitUpdate();
            pack.inventory = new ArrayList<>();
            pack.equipment = new ArrayList<>();
            pack.ConfUnit = true;
            pack.ConfInventory = true;
            for (Unit unit : UnitList){
                pack.inventory.add(unit.inventory.inventoryStr);
                pack.equipment.add(unit.equipment.inventoryStr);
            }
            PacketServer.player = PacketUnit;
            pack.packServer = PacketServer;
            Server.sendToAllTCP(pack);
            PacketServer.unitConf = false;
            PacketServer.InventoryConf = false;
        }
        if(PacketServer.InventoryConf){
            PacketUnitUpdate pack = new PacketUnitUpdate();
            pack.inventory = new ArrayList<>();
            pack.equipment = new ArrayList<>();
            pack.ConfInventory = true;
            for (Unit unit : UnitList){
                pack.inventory.add(unit.inventory.inventoryStr);
                pack.equipment.add(unit.equipment.inventoryStr);
            }
            PacketServer.player = PacketUnit;
            pack.packServer = PacketServer;
            Server.sendToAllTCP(pack);
            PacketServer.InventoryConf = false;
        }
        //PacketServer.Money = new int[5];
        for(int i = 0;i<TeamGlobal.size();i++) {
            PacketServer.Money.add(TeamGlobal.get((byte) i));
        }


        Server.sendToAllUDP(PacketServer);
        SoundPack.clear();
        MapObject.PacketMapObjects.clear();
        ItemObject.ConfSentPackItem = false;
        ItemPackList.clear();
        PacketServer.Money.clear();
        PacketDebris.clear();
        PacketBull.clear();
        PacketBuilding.clear();
    }
    public static TransportPacket packet_player_server(Unit unit){
        TransportPacket pack = new TransportPacket();
        pack.name = unit.TypeUnit;
        pack.x = unit.x;
        pack.y = unit.y;
        pack.PlayerConf = unit.PlayerConf;
        pack.PressA = unit.press_a;
        pack.PressD = unit.press_d;
        pack.rotation_corpus = unit.rotation_corpus;
        pack.hp = unit.hp;
        pack.max_hp = unit.max_hp;
        pack.team = unit.team;
        pack.speed = unit.speed;
        pack.host = unit.host;
        pack.IDClient = unit.nConnect;
        pack.ID = unit.ID;
        pack.TowerList1 = new ArrayList<>();
        TowerPack TowerPacket;
        for (Unit Tower : unit.TowerUnitList){
            TowerPacket = new TowerPack(Tower.rotation_tower,Tower.reload);
            TowerPackInit(TowerPacket,Tower);
            pack.TowerList1.add(TowerPacket);
            //pack.reloadTower.add(Tower.reload);
            //pack.rotation_tower_2.add(Tower.rotation_tower);
        }
        return pack;
    }
    public static void PacketUnitUpdate(Unit unit){
        unit.PackUnit.name = unit.TypeUnit;
        unit.PackUnit.x = unit.x;
        unit.PackUnit.y = unit.y;
        unit.PackUnit.PlayerConf = unit.PlayerConf;
        unit.PackUnit.PressA = unit.press_a;
        unit.PackUnit.PressD = unit.press_d;
        unit.PackUnit.rotation_corpus = unit.rotation_corpus;
        unit.PackUnit.hp = unit.hp;
        unit.PackUnit.max_hp = unit.max_hp;
        unit.PackUnit.team = unit.team;
        unit.PackUnit.speed = unit.speed;
        unit.PackUnit.host = unit.host;
        unit.PackUnit.IDClient = unit.nConnect;
        unit.PackUnit.ID = unit.ID;
        int i = 0;
        for (Unit Tower : unit.TowerUnitList){
            unit.PackUnit.TowerList1.get(i).reloadTower = Tower.reload;
            unit.PackUnit.TowerList1.get(i).rotationTower = Tower.rotation_tower;
            TowerPackInit2(unit.PackUnit.TowerList1.get(i),Tower);
            //pack.reloadTower.add(Tower.reload);
            //pack.rotation_tower_2.add(Tower.rotation_tower);
            i++;
        }
    }
    private static void TowerPackInit2(TowerPack pack,Unit unit){
        int i = 0;
        TowerPack packTower;
        for (Unit Tower : unit.TowerUnitList){
            packTower = pack.TowerList1.get(i);
            packTower.reloadTower = Tower.reload;
            packTower.rotationTower = Tower.rotation_tower;
            TowerPackInit(packTower,Tower);
            i++;
        }
        i = 0;
        for (Unit Tower : unit.TrackUnitList){
            packTower = pack.TowerList2.get(i);
            packTower.reloadTower = Tower.reload;
            packTower.rotationTower = Tower.rotation_tower;
            TowerPackInit(packTower,Tower);
            i++;
        }
    }
    private static void TowerPackInit(TowerPack pack,Unit unit){
        pack.TowerList1 = new ArrayList<>();
        pack.TowerList2 = new ArrayList<>();
        TowerPack TowerPacket;
        for (Unit Tower : unit.TowerUnitList){
            TowerPacket = new TowerPack(Tower.rotation_tower,Tower.reload);
            TowerPackInit(TowerPacket,Tower);
            pack.TowerList1.add(TowerPacket);
        }
        for (Unit Tower : unit.TrackUnitList){
            TowerPacket = new TowerPack(Tower.rotation_tower,Tower.reload);
            TowerPackInit(TowerPacket,Tower);
            pack.TowerList2.add(TowerPacket);
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
    private class IterationDebris implements Runnable{
        public void run(){
            for (int i = 0; i < DebrisList.size(); i++) {
                Unit debris = DebrisList.get(i);
                    //synchronized (debris) {
                    debris.XYMapCordDebris();
                    packet_debris_server(debris, i);
                    debris.all_action();
                    debris.corpus_corpus(Main.UnitList);
                    //}
            }
            Collision.DebrisCollisionIterationGlobal();
        }
    }
    private class IterationBullet implements Runnable {
        public void run() {
            for (int i = 0; i < Bullet.BulletListInd.size(); i++) {
                Bullet bullet = BulletList.get(Bullet.BulletListInd.get(i));
                bullet.all_action();

            }
//            for (int i = 0; i < BulletList.size(); i++) {
//                Bullet bullet = BulletList.get(i);
//                    //synchronized (bullet) {
//                        bullet.all_action();
//                    //}
//
//            }
        }
    }
    public class IterationUnit implements Runnable {
        public void run() {
            if (AIScan) {
                for (Squad squad : AI.SquadList) {
                    squad.SquadIteration();
                }
            }
            for (int i = 0; i < UnitList.size(); i++) {
                Unit unit = UnitList.get(i);
                unit.XYMapCord();
                ActionGameHost.PacketUnitUpdate(unit);
                PacketUnit.set(i,unit.PackUnit);
                if (unit.host || unit.control == RegisterControl.controllerBot
                        || unit.control == RegisterControl.controllerBotSupport
                        || unit.control == RegisterControl.controllerSoldatTransport
                        || unit.control == RegisterControl.controllerSoldatBot
                        || unit.control == RegisterControl.controllerHelicopter) {
                    //PacketUnit.add(pack);
                    unit.all_action();
                } else {
                    //PacketUnit.add(pack);
                    unit.all_action_client();
                }
            }
            if (!AI.SquadDeleteList.isEmpty()) {
                AI.SquadDeleteList.subList(0, AI.SquadDeleteList.size()).clear();
            }
            Collision.CollisionIterationGlobal();
        }
    }
}

