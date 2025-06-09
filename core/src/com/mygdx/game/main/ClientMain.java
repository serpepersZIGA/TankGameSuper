package com.mygdx.game.main;
import Content.Build.BigBuildingWood1;
import Content.Build.Home1;
import Content.Particle.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.Event.EventDeleteItemClient;
import com.mygdx.game.Event.EventTransferItemClient;
import com.mygdx.game.Event.EventUseClient;
import com.mygdx.game.Inventory.*;
import com.mygdx.game.Sound.SoundPacket;
import com.mygdx.game.Sound.SoundRegister;
import com.mygdx.game.block.Block;
import com.mygdx.game.build.BuildPacket;
import com.mygdx.game.build.BuildType;
import com.mygdx.game.build.PacketBuildingServer;
import com.mygdx.game.bull.BullPacket;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.method.CycleTimeDay;
import com.mygdx.game.object_map.ObjectLoad;
import com.mygdx.game.Sound.SoundPlay;
import com.mygdx.game.object_map.MapObject;
import com.mygdx.game.object_map.ObjectMapAssets;
import com.mygdx.game.object_map.PacketMapObject;
import com.mygdx.game.object_map.VoidObject;
import com.mygdx.game.object_map.component_collision_system.CollisionVoid;
import com.mygdx.game.unit.*;
import com.mygdx.game.unit.SpawnPlayer.*;

import static com.mygdx.game.Inventory.ItemObject.ItemList;
import static com.mygdx.game.Sound.SoundRegister.IDSound;
import static com.mygdx.game.build.BuildRegister.PacketBuilding;
import static com.mygdx.game.bull.BulletRegister.IDBullet;
import static com.mygdx.game.bull.BulletRegister.PacketBull;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.CycleTimeDay.lightGlobal;
import static com.mygdx.game.method.Option.SoundConst;
import static com.mygdx.game.method.pow2.pow2;
import static com.mygdx.game.object_map.MapObject.PacketMapObjects;
import static com.mygdx.game.Inventory.Item.IDListItem;
import static com.mygdx.game.unit.TransportRegister.*;
import static java.lang.StrictMath.sqrt;


public class ClientMain extends Listener {
    public static Client Client;
    static int udpPort = 27950, tcpPort = 27950;
    public static String IP = "127.0.0.1";
    private int i;

    public void create() {
        System.out.println("Подключаемся к серверу");
        Client = new Client(200000000, 200000000);

        //Регистрируем пакет
        Client.getKryo().register(ItemPacket.class);
        Client.getKryo().register(String[][].class);
        Client.getKryo().register(String[].class);
        Client.getKryo().register(EventUseClient.class);
        Client.getKryo().register(EventDeleteItemClient.class);
        Client.getKryo().register(EventTransferItemClient.class);
        Client.getKryo().register(PacketInventory.class);
        Client.getKryo().register(PackerServer.class);
        Client.getKryo().register(Packet_client.class);
        Client.getKryo().register(TransportPacket.class);
        Client.getKryo().register(BullPacket.class);
        Client.getKryo().register(ArrayList.class);
        Client.getKryo().register(SoundPlay.class);
        Client.getKryo().register(DebrisPacket.class);
        Client.getKryo().register(UnitType.class);
        Client.getKryo().register(Bang.class);
        Client.getKryo().register(FlameSpawn.class);
        Client.getKryo().register(Flame.class);
        Client.getKryo().register(FlameParticle.class);
        Client.getKryo().register(Acid.class);
        Client.getKryo().register(Blood.class);
        Client.getKryo().register(FlameStatic.class);
        Client.getKryo().register(BuildPacket.class);
        Client.getKryo().register(BuildType.class);
        Client.getKryo().register(PacketBuildingServer.class);
        Client.getKryo().register(SoundPacket.class);


        Client.getKryo().register(PacketMapObject.class);
        Client.getKryo().register(ObjectMapAssets.class);

        Client.getKryo().register(PacketUnitUpdate.class);
        Client.getKryo().register(SpawnPlayerPack.class);

        //Запускаем клиент
        Client.start();
        //Клиент начинает подключатся к серверу

        //Клиент подключается к серверу
        try {
            Client.connect(5000, IP, tcpPort, udpPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        packetUnitUpdate.ConfUnitList = true;
        packetUnitUpdate.ConfDebrisList = true;

        Client.addListener(Main.Main_client);
    }

    @Override
    public void received(Connection c, Object p) {
        IDClient = c.getID();
        if (p instanceof PackerServer) {
            PacketBull = ((PackerServer) p).bull;
            for (BullPacket pack : PacketBull) {
                for (Object[] obj :IDBullet){
                    if(pack.ID == (int)obj[1]) {
                        Bullet bullet = (Bullet) obj[0];
                        bullet.BulletAdd(pack.x,pack.y,pack.rotation,0,0,0,0,
                                pack.team, pack.height, 0,pack.speed,0, pack.time);
                    }
                }
            }
            PacketBull.clear();

            CycleTimeDay.lightTotal = ((PackerServer) p).TotalLight;
            lightGlobal = (CycleTimeDay.lightTotal-CycleTimeDay.lightFlame);
            CycleTimeDay.lightRealGlobal = lightGlobal*1.2f;
            CycleTimeDay.lightColorGlobal = lightGlobal*10f;
            PacketUnit = ((PackerServer) p).player;
            i = 0;
            if(PacketUnit.size()== UnitList.size()) {
                for (Unit unit : UnitList) {
                    player_data(unit);
                    i++;
                }
            }
            else {
                UnitCreate();
            }

            ItemPackList = ((PackerServer) p).item;
            i = 0;
            if(ItemPackList.size()== ItemList.size()) {
                for (ItemObject item : ItemList) {
                    item_data(item);
                    i++;
                }
            }
            else {
                ItemCreate();
            }



            InventoryPack = ((PackerServer) p).inventory;
            if(InventoryPack != null) {
                try {
                for (i = 0; i < InventoryPack.size(); i++) {
                    if(UnitList.get(i).inventory!= null) {
                        UnitList.get(i).inventory.ItemClear();
                    }
                    else {
                        UnitList.get(i).inventory = new Inventory(new Item[InventoryPack.get(i).Inventory.length][InventoryPack.get(i).Inventory[0].length]);

                    }
                    for (int ix = 0; ix < InventoryPack.get(i).Inventory.length; ix++) {
                        for (int iy = 0; iy < InventoryPack.get(i).Inventory[ix].length; iy++) {
                            if (InventoryPack.get(i).Inventory[ix][iy] != null) {
                                for (Object[] obj : IDListItem) {
                                    if(InventoryPack.get(i).Inventory[ix][iy] != null) {
                                        if (Objects.equals(obj[1], InventoryPack.get(i).Inventory[ix][iy])) {
                                            UnitList.get(i).inventory.ItemAdd(ix, iy, (Item) obj[0]);
                                        }
                                    }
                                }
                            }
                            else {
                                UnitList.get(i).inventory.InventorySlots[ix][iy] = null;
                            }
                        }
                    }
                }
                InventoryPack = null;
                }catch (IndexOutOfBoundsException ignored){}
            }

            PacketDebris = ((PackerServer) p).debris;
            i = 0;

            if(PacketDebris.size()== DebrisList.size()) {
                for (Unit debris : DebrisList) {
                    debris_data(debris);
                    i++;
                }
            }
            else {
                DebrisList.clear();
                for (DebrisPacket packetDebris : PacketDebris) {
                    Main_client.debris_create(packetDebris);
                    Main_client.debris_data_add(packetDebris);
                }
                KeyboardObj.ZoomConstTransport();
                packetUnitUpdate.ConfDebrisList = false;
            }
            //ActionGameClient.PackUpdateUnit();

            PacketMapObjects = ((PackerServer) p).mapObject;
            for (PacketMapObject packetMapObject : PacketMapObjects) {
                BlockList2D.get(packetMapObject.iy).get(packetMapObject.ix).objMap
                        = new VoidObject();
                //KeyboardObj.zoom_const();
            }
            SoundRegister.SoundPack = ((PackerServer) p).sound;
            for (SoundPacket pack : SoundRegister.SoundPack) {
                SoundPlay.sound((Sound) IDSound.get(pack.ID)[0],
                        1f-((float) sqrt(pow2(RC.x-pack.ix) + pow2(RC.y-pack.iy)) / SoundConst));
            }
            ItemPackList.clear();
            PacketMapObjects.clear();
            PacketBull.clear();
            PacketUnit.clear();
            PacketDebris.clear();
        } else if (p instanceof PacketBuildingServer) {
            PacketBuilding = ((PacketBuildingServer) p).BuildPack;
            BuildingList.clear();
            for (int i = 0; i < PacketBuilding.size(); i++) {
                Building_create(i, PacketBuilding.get(i).x - width_block, PacketBuilding.get(i).y - height_block);
            }
            ArrayList<ArrayList<PacketMapObject>> objMapList;
            objMapList = ((PacketBuildingServer) p).ObjectMapPack;
            for (int iy = 0; iy < objMapList.size(); iy++) {
                for (int ix = 0; ix < objMapList.get(iy).size(); ix++) {
                    //System.out.println(ix+" "+iy);
                    object_create(ix, iy, objMapList.get(iy).get(ix).objectAssets, objMapList.get(iy).get(ix).x,
                            objMapList.get(iy).get(ix).y, objMapList.get(iy).get(ix).width, objMapList.get(iy).get(ix).height,
                            objMapList.get(iy).get(ix).lighting, objMapList.get(iy).get(ix).distance_lighting);
                }
            }
            Block.passability_detected();

        }
        else if (p instanceof PacketUnitUpdate) {
            packetUnitUpdate = (PacketUnitUpdate)p;
        }
    }

    public void object_create(int ix, int iy, ObjectMapAssets assets, int x, int y, int width, int height,
                              boolean light, float distance_lighting) {
        try {
            if (width != 0) {
                Sprite asset = ObjectLoad.ImageLoad(assets);
                BlockList2D.get(iy).get(ix).objMap = new MapObject(x - ix * width_block, y - iy * height_block, asset, width, height, 0, ix, iy, new CollisionVoid()
                        , light, distance_lighting, assets);
            } else {
                BlockList2D.get(iy).get(ix).objMap = new VoidObject();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
    public void item_data(ItemObject item) {
        ItemPacket packet = ItemPackList.get(i);;
        item.x = packet.x;
        item.y = packet.y;
        //item.ID

    }


    public void player_data(Unit unit) {
        TransportPacket packet = PacketUnit.get(i);
        unit.type_unit = packet.name;
        unit.x = packet.x;
        unit.y = packet.y;
        unit.rotation_corpus = packet.rotation_corpus;
        unit.hp = packet.hp;
        unit.team = packet.team;
        unit.speed = packet.speed;
        unit.host = packet.host;
        unit.nConnect = packet.IDClient;
        for (int i2 = 0; i2 < unit.tower_obj.size(); i2++) {

            unit.tower_obj.get(i2).rotation_tower = packet.rotation_tower_2.get(i2);
            unit.tower_obj.get(i2).reload = packet.reloadTower.get(i2);
        }

    }

    public void UnitDataCreate(TransportPacket pack, Unit transport) {
        transport.type_unit = pack.name;
        transport.x = pack.x;
        transport.y = pack.y;
        transport.rotation_corpus = pack.rotation_corpus;
        transport.hp = pack.hp;
        transport.team = pack.team;
        transport.speed = pack.speed;
        transport.host = pack.host;
        transport.nConnect = pack.IDClient;
        for (int i2 = 0; i2 < transport.tower_obj.size(); i2++) {
            transport.tower_obj.get(i2).rotation_tower=pack.rotation_tower_2.get(i2);
            transport.tower_obj.get(i2).reload=pack.reloadTower.get(i2);
        }

    }
    public void debris_data(Unit debris) {
        DebrisPacket pack = PacketDebris.get(i);
        debris.ID = pack.UnitID;
        debris.x = pack.x;
        debris.y = pack.y;
        debris.rotation_corpus = pack.rotation;

    }

    public void debris_data_add(DebrisPacket packet) {
        Unit debris = DebrisList.get(DebrisList.size()- 1);
        debris.ID = packet.UnitID;
        debris.x = packet.x;
        debris.y = packet.y;
        debris.rotation_corpus = packet.rotation;
    }


    public void Building_create(int i, int x, int y) {
        if (PacketBuilding.get(i).name != null) {
            switch (PacketBuilding.get(i).name) {
                case BigBuildingWood1:
                    BuildingList.add(new BigBuildingWood1(x, y));
                    break;
                case Home1:
                    BuildingList.add(new Home1(x, y));
                    break;
            }
        }
    }

    public void debris_create(DebrisPacket debris) {
        for(Object[] obj :Unit.IDList) {
            if(Objects.equals(obj[1], debris.UnitID)){
                Unit unit = (Unit)obj[0];
                DebrisList.add(new UnitPattern(unit.CorpusUnit, (String) obj[1],debris.x,debris.y,debris.rotation,0,0,0));
                break;
            }
        }
    }

    public void UnitCreate() {
        UnitList.clear();
        Unit unit;
        for (TransportPacket pack : PacketUnit) {
            for(Object[] obj :Unit.IDList) {
                if(Objects.equals(obj[1], pack.ID)){
                    unit = (Unit)obj[0];

                    UnitList.add(unit.UnitAdd(0,0,pack.host,pack.team));
                    UnitList.get(UnitList.size() - 1).control = RegisterControl.controllerBot;
                    UnitDataCreate(pack,unit);
                    break;
                }
            }
            if (pack.PlayerConf) {
                UnitList.get(UnitList.size() - 1).control = RegisterControl.controllerPlayer;
                UnitList.get(UnitList.size() - 1).nConnect = pack.IDClient;
            }
        }
        KeyboardObj.ZoomConstTransport();
    }
    public void ItemCreate() {
        ItemList.clear();
        for (ItemPacket pack : ItemPackList) {
            for(Object[] obj : IDListItem) {
                if(Objects.equals(obj[1], pack.ID)){

                    ItemList.add(new ItemObject((Item) obj[0],pack.x,pack.y));
                    break;
                }
            }
        }
        KeyboardObj.ZoomConstTransport();
    }

}

