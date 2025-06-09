package com.mygdx.game.unit.SpawnPlayer;

import com.mygdx.game.main.Main;
import com.mygdx.game.Inventory.Inventory;
import com.mygdx.game.Inventory.InventoryInterface;
import com.mygdx.game.Inventory.Item;
import com.mygdx.game.Inventory.ItemRegister;

import static com.mygdx.game.main.Main.UnitList;
import static com.mygdx.game.main.Main.inventoryMain;
import static com.mygdx.game.unit.TransportRegister.PlayerCannonMortarA1;

public class SpawnPlayerCannonMortar extends PlayerSpawnData{
    public void SpawnPlayer(boolean host){
        //Main.UnitList.add(PlayerCannonFlameA1.UnitAdd(200,200,host, (byte) 1,Main.RegisterControl.controllerPlayer));
        PlayerCannonMortarA1.UnitAdd(200,200,host, (byte) 1,Main.RegisterControl.controllerPlayer,new Inventory(new Item[3][4]));
        //UnitList.get(UnitList.size()-1).control = Main.RegisterControl.controllerPlayer;
        UnitList.get(UnitList.size()-1).PlayerConf = true;
        UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.flamethrower);
        UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.AK74);
        UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.MedicineT1);

    }

}
