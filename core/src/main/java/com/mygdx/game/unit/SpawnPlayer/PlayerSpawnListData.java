package com.mygdx.game.unit.SpawnPlayer;

import java.util.ArrayList;

public class PlayerSpawnListData {
    public static PlayerSpawnData PlayerSpawnCannonVoid;
    public static ArrayList<String>SpawnList = new ArrayList<>();
    public static void Create(){
        PlayerSpawnCannonVoid = new SpawnPlayerVoid();
    }
}
