package com.mygdx.game.Network;

import java.util.ArrayList;

public class TowerPack {
    public TowerPack() {
    }
    public TowerPack(float rotationTower,float reloadTower){
        this.reloadTower = reloadTower;
        this.rotationTower = rotationTower;
        TowerList1 = new ArrayList<>();
        TowerList2 = new ArrayList<>();
    }
    public float rotationTower,reloadTower;
    public ArrayList<TowerPack>TowerList1,TowerList2;
}
