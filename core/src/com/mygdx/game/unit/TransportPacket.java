package com.mygdx.game.unit;

import java.util.ArrayList;

public class TransportPacket {
    public float x,y,rotation_corpus;
    public UnitType name;
    public String ID;
    public byte team;
    public int hp;
    public float speed;
    public boolean host,crite_life,PlayerConf;
    public int IDClient;
    public ArrayList<Float>rotation_tower_2 = new ArrayList<>(),reloadTower = new ArrayList<>();
    //public ArrayList<String>ItemList = new ArrayList<>();
}
