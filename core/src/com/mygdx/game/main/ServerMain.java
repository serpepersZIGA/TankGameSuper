package com.mygdx.game.main;
import Content.Particle.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Event.EventDeleteItemClient;
import com.mygdx.game.Event.EventTransferItemClient;
import com.mygdx.game.Event.EventUseClient;
import com.mygdx.game.Inventory.*;
import com.mygdx.game.Sound.SoundPacket;
import com.mygdx.game.build.BuildPacket;
import com.mygdx.game.build.BuildType;
import com.mygdx.game.build.PacketBuildingServer;
import com.mygdx.game.bull.BullPacket;
import com.mygdx.game.method.CycleTimeDay;
import com.mygdx.game.Sound.SoundPlay;
import com.mygdx.game.object_map.ObjectMapAssets;
import com.mygdx.game.object_map.PacketMapObject;
import com.mygdx.game.unit.DebrisPacket;
import com.mygdx.game.unit.SpawnPlayer.*;
import com.mygdx.game.unit.TransportPacket;
import com.mygdx.game.unit.Unit;
import com.mygdx.game.unit.UnitType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static com.mygdx.game.Inventory.ItemObject.ItemList;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.Inventory.Item.IDListItem;
import static com.mygdx.game.unit.Unit.IDList;

public class ServerMain extends Listener {
    public static Server Server;
    static final int udpPort = 27950, tcpPort = 27950;
    public static int nConnect = 0;

    public void create(){
        System.out.println("Создаем сервер");
        //Создаем сервер
        Server = new Server(200000000,200000000);

        //Регистрируем пакет класс
        Server.getKryo().register(ItemPacket.class);
        Server.getKryo().register(String[][].class);
        Server.getKryo().register(String[].class);
        Server.getKryo().register(EventUseClient.class);
        Server.getKryo().register(EventDeleteItemClient.class);
        Server.getKryo().register(EventTransferItemClient.class);
        Server.getKryo().register(PacketInventory.class);
        Server.getKryo().register(PackerServer.class);
        Server.getKryo().register(Packet_client.class);
        Server.getKryo().register(TransportPacket.class);
        Server.getKryo().register(BullPacket.class);
        Server.getKryo().register(ArrayList.class);
        Server.getKryo().register(SoundPlay.class);
        Server.getKryo().register(DebrisPacket.class);
        Server.getKryo().register(UnitType.class);
        Server.getKryo().register(Bang.class);
        Server.getKryo().register(FlameSpawn.class);
        Server.getKryo().register(Flame.class);
        Server.getKryo().register(FlameParticle.class);
        Server.getKryo().register(Acid.class);
        Server.getKryo().register(Blood.class);
        Server.getKryo().register(FlameStatic.class);
        Server.getKryo().register(BuildPacket.class);
        Server.getKryo().register(BuildType.class);
        Server.getKryo().register(PacketBuildingServer.class);

        Server.getKryo().register(SoundPacket.class);

        Server.getKryo().register(PacketMapObject.class);
        Server.getKryo().register(ObjectMapAssets.class);

        Server.getKryo().register(PacketUnitUpdate.class);
        Server.getKryo().register(SpawnPlayerPack.class);

        //Регистрируем порт
        try {
            Server.bind(tcpPort, udpPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Запускаем сервер
        Server.start();

        Server.addListener(Main.serverMain);

    }
    public void connected(Connection c){
        System.out.println("На сервер подключился " + c.getRemoteAddressTCP().getHostString());

        for (int i = 0;i<Main.BuildingList.size();i++){
            PacketBuildServer(i);
        }
        for (int iy = 0; iy< BlockList2D.size(); iy++){
            PacketBuildingServer.ObjectMapPack.add(new ArrayList<>());
            for (int ix = 0; ix< BlockList2D.get(iy).size(); ix++){
                PacketObjectMapServer(ix,iy,PacketBuildingServer.ObjectMapPack.get(iy));
            }
        }
        PacketBuildingServer.FlameLight = CycleTimeDay.lightFlame;
        Server.sendToAllTCP(PacketBuildingServer);
        PacketBuildingServer.ObjectMapPack.clear();
        PacketBuildingServer.BuildPack.clear();
        //KeyboardObj.ZoomConstTransport();
        //KeyboardObj.zoom_const();

    }
    public void PacketObjectMapServer(int ix,int iy,ArrayList<PacketMapObject>YMap){
        YMap.add(new PacketMapObject());
        YMap.get(ix).x = BlockList2D.get(iy).get(ix).objMap.x;
        YMap.get(ix).y = BlockList2D.get(iy).get(ix).objMap.y;
        YMap.get(ix).width = BlockList2D.get(iy).get(ix).objMap.width;
        YMap.get(ix).height = BlockList2D.get(iy).get(ix).objMap.height;
        YMap.get(ix).ix = ix;
        YMap.get(ix).iy = iy;
        YMap.get(ix).lighting = BlockList2D.get(iy).get(ix).objMap.lighting;
        YMap.get(ix).distance_lighting = BlockList2D.get(iy).get(ix).objMap.distance_lighting;
        YMap.get(ix).objectAssets = BlockList2D.get(iy).get(ix).objMap.assets;
    }
    public void PacketBuildServer(int i){
        PacketBuildingServer.BuildPack.add(new BuildPacket());
        PacketBuildingServer.BuildPack.get(i).name = BuildingList.get(i).name;
        PacketBuildingServer.BuildPack.get(i).x = BuildingList.get(i).x;
        PacketBuildingServer.BuildPack.get(i).y = BuildingList.get(i).y;
    }

    //Используется когда клиент отправляет пакет серверу
    public void received(Connection c, Object p){
        if(p instanceof Packet_client) {
            Packet_client pack = (Packet_client)p;
            for (Unit unit : UnitList) {
                //System.out.println(IDClient+" "+unit.nConnect );
                if (pack.IDClient == unit.nConnect) {

                    unit.left_mouse = pack.left_mouse;
                    unit.right_mouse = pack.right_mouse;
                    unit.press_w = pack.press_w;
                    unit.press_a = pack.press_a;
                    unit.press_s = pack.press_s;
                    unit.press_d = pack.press_d;
                    unit.press_f = pack.press_f;
                    unit.TargetX = pack.mouse_x- RC.width_2;
                    unit.TargetY = pack.mouse_y- RC.height_2;
                    //unit.FireControl();
                    for (Unit Tower : unit.tower_obj) {
                        Tower.left_mouse = pack.left_mouse;
                        Tower.TargetX = unit.TargetX+ Tower.tower_x;
                        Tower.TargetY = unit.TargetY+ Tower.tower_y;
                    }
                    return;
                }

            }
        }
        else if(p instanceof SpawnPlayerPack){
            //System.out.println("586855");
            nConnect += 1;
            EnumerationList = true;
            for(Object[] Player :IDList) {
                if (Objects.equals(Player[1], ((SpawnPlayerPack) p).ID)) {
                    Unit unit = (Unit) Player[0];
                    unit.UnitAdd(200,200,false,(byte) 1
                            ,RegisterControl.controllerPlayer,new Inventory(new Item[4][4]));
                    UnitList.get(UnitList.size()-1).nConnect = nConnect;
                    UnitList.get(UnitList.size()-1).PlayerConf = true;
                    UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.MedicineT1);
                    UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.MedicineT1);
                    UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.MedicineT1);
                    UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.MedicineT1);
                    UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.MedicineT1);
                    break;
                }
            }
//            if(!p.equals(new SpawnPlayerVoid())) {
//                int i2 = Main.UnitList.size();
//                ((PlayerSpawnData) p).SpawnPlayer(false);
//                Main.UnitList.get(i2).nConnect = nConnect;
//            }
        }
        else if(p instanceof EventUseClient){
            for(Object[] item :IDListItem) {
                if(Objects.equals(item[1], ((EventUseClient) p).str)) {
                    UnitList.get(((EventUseClient) p).ID).inventory.ItemUse((Item)item[0],UnitList.get(((EventUseClient) p).ID));


                    return;
                }
            }
        }
        else if(p instanceof EventDeleteItemClient){
            EventDeleteItemClient pack = (EventDeleteItemClient) p;
            ItemList.add(new ItemObject(UnitList.get(pack.i).inventory.InventorySlots
                    [pack.x][pack.y], (int) UnitList.get(pack.i).x, (int) UnitList.get(pack.i).y));
            UnitList.get(pack.i).inventory.InventorySlots[pack.x][pack.y] = null;
            return;


        }
        else if(p instanceof EventTransferItemClient){
            EventTransferItemClient pack = (EventTransferItemClient) p;
            //System.out.println("x1 "+pack.x+" y1 "+pack.y+" x2 "+pack.x2+" y2 "+pack.y2);
//            Item item1 = UnitList.get(pack.i).inventory.InventorySlots[pack.x][pack.y];
//            Item item2 = UnitList.get(pack.i).inventory.InventorySlots[pack.x2][pack.y2];
            UnitList.get(pack.i).inventory.ItemAdd(pack.x,pack.y,pack.item2);
            UnitList.get(pack.i).inventory.ItemAdd(pack.x2,pack.y2,pack.item1);
            //packetInventoryServer();
//            if(UnitList.get(pack.i).inventory.InventorySlots[pack.x][pack.y]!= null) {
//                Item itemBuff1 = UnitList.get(pack.i).inventory.InventorySlots[pack.x][pack.y];
//                Item itemBuff2 = UnitList.get(pack.i).inventory.InventorySlots[pack.x2][pack.y2];
//                System.out.println("x1 "+pack.x+" y1 "+pack.y+" x2 "+pack.x2+" y2 "+pack.y2);
//                itemBuff1 = itemBuff1.clone();
//                if (itemBuff2 != null) {
//                    itemBuff2 = itemBuff2.clone();
//                    UnitList.get(pack.i).inventory.InventorySlots[pack.x][pack.y] = itemBuff2.clone();
//                }
//                else{
//                    UnitList.get(pack.i).inventory.InventorySlots[pack.x][pack.y] = null;
//                }
//                UnitList.get(pack.i).inventory.InventorySlots[pack.x2][pack.y2] = itemBuff1.clone();
//            }
        }
    }

    //Используется когда клиент покидает сервер.
    public void disconnected(Connection c){System.out.println("Клиент покинул сервер!");}
}
