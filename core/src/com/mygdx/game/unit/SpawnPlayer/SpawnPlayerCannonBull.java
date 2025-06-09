package com.mygdx.game.unit.SpawnPlayer;

import com.mygdx.game.main.Main;
import com.mygdx.game.Inventory.Inventory;
import com.mygdx.game.Inventory.InventoryInterface;
import com.mygdx.game.Inventory.Item;

import static com.mygdx.game.main.Main.UnitList;
import static com.mygdx.game.main.Main.inventoryMain;
import static com.mygdx.game.unit.TransportRegister.PlayerCannonMachineGunA1;

public class SpawnPlayerCannonBull extends PlayerSpawnData{
    public void SpawnPlayer(boolean host){
        PlayerCannonMachineGunA1.UnitAdd(200,200,host, (byte) 1,Main.RegisterControl.controllerPlayer,new Inventory(new Item[3][4]));
        UnitList.get(UnitList.size()-1).PlayerConf = true;

    }
}
