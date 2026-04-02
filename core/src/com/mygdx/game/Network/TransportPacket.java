package com.mygdx.game.Network;

import com.mygdx.game.Inventory.PacketInventory;
import com.mygdx.game.unit.UnitType;

import java.util.ArrayList;

public class TransportPacket {
    public float x,y,rotation_corpus;
    public UnitType name;
    public String ID;
    public byte team;
    public int hp,max_hp;
    public float speed;
    public boolean host,crite_life,PlayerConf;
    public int IDClient;
    public ArrayList<Float>rotation_tower_2 = new ArrayList<>(),reloadTower = new ArrayList<>();
    public String[][] inventory,equipment;
    //public ArrayList<String>ItemList = new ArrayList<>();
}
