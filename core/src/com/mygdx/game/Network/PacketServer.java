package com.mygdx.game.Network;

import java.util.ArrayList;

public class PacketServer {
    public ArrayList<TransportPacket>player;
    public ArrayList<BuildPacket>building;
    public ArrayList<DebrisPacket>debris;
    public boolean debrisConf,unitConf,InventoryConf;
    public ArrayList<PacketMapObject>mapObject = new ArrayList<>();

    //public ArrayList<ItemPacket>item = new ArrayList<>();

    public ArrayList<SoundPacket>sound = new ArrayList<>();
    public float TotalLight;
    public ArrayList<Integer>Money = new ArrayList<>();

}
