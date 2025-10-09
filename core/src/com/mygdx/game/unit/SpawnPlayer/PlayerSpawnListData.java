package com.mygdx.game.unit.SpawnPlayer;

import java.util.ArrayList;

public class PlayerSpawnListData {
    public static PlayerSpawnData PlayerSpawnCannonFlame,PlayerSpawnCannonMortar,PlayerSpawnCannonAcid,PlayerSpawnCannonBull
            ,PlayerSpawnCannonVoid;
    public static ArrayList<String>SpawnList = new ArrayList<>();
    public static void Create(){
        PlayerSpawnCannonFlame = new SpawnPlayerCannonFlame();
        PlayerSpawnCannonMortar = new SpawnPlayerCannonMortar();
        PlayerSpawnCannonAcid = new SpawnPlayerCannonAcid();
        PlayerSpawnCannonBull = new SpawnPlayerCannonBull();
        PlayerSpawnCannonVoid = new SpawnPlayerVoid();
    }
}
