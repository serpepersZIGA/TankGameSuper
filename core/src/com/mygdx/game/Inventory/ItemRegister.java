package com.mygdx.game.Inventory;
import com.mygdx.game.unit.moduleUnit.GunRegister;

import java.util.ArrayList;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.ContentImage;
import static com.mygdx.game.unit.moduleUnit.Gun.GunList;

public class ItemRegister {
    public static Item AK74,VoidGun,flamethrower,MedicineT1;
    public static void Create(){
//        flamethrower = new Item(GunList.get("flamethrower"),"FlGun",new ArrayList<>(),"FlameGun");
//        flamethrower.teg.add(TegItem.intimately);
//        MedicineT1 = new Item(220,"Med1",new ArrayList<>(),"Medicine");
//        MedicineT1.teg.add(TegItem.medicine);
//        AK74 = new Item(GunList.get("AK74"),"AK74",new ArrayList<>(),"AK74");
//        AK74.teg.add(TegItem.afar);
        VoidGun = new Item(GunRegister.Void,new ArrayList<>());
    }
}
