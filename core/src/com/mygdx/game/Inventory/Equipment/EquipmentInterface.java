package com.mygdx.game.Inventory.Equipment;

import com.mygdx.game.Inventory.*;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.Keyboard.MouseX;
import static com.mygdx.game.method.Keyboard.MouseY;

public class EquipmentInterface extends InventoryInterface {
    public EquipmentInterface(Inventory inventory){
        this.inventory = inventory;
        WindowName = new WindowName();
        InventoryType = true;
        XInterface = inventory.InventorySlots.length;
        YInterface = inventory.InventorySlots[0].length;
        SlotInventory = new Slot[XInterface][YInterface];
        this.x = 220;this.y = 220;
        WidthWindow = 600;
        HeightWindow = 320;
        XSlots = WidthWindow/XInterface;
        YSlots = HeightWindow/YInterface;
        if(XSlots>YSlots){XSlots=YSlots;}
        else if(XSlots<YSlots){YSlots = XSlots;}
        SlotGeneration();
    }
    public void StrengtheningInitialization(Unit unit){
        //unit.max_hp+= (int) HP;
        //unit.armor+= Armor;
        //unit.hp += (int) HP;
        unit.max_hp = unit.HpBase;
        unit.hp = unit.HpBase;
        unit.armor = unit.ArmorBase;
        unit.Acceleration =  unit.AccelerationBase;
        unit.SpeedUp = unit.SpeedUpBase;
        unit.SpeedDown = unit.SpeedDownBase;

        for (Unit cannon : unit.tower_obj) {
            cannon.damage = (int) cannon.DamageBase;
        }
        for (Unit cannon : unit.tower_obj) {
            cannon.penetration = cannon.PenetrationBase;
        }


        unit.green_len = ((float) unit.hp / unit.max_hp) * Option.size_x_indicator;
        for(int i = 0;i<inventory.InventorySlots.length;i++){
            for(int i2 = 0;i2<inventory.InventorySlots[i].length;i2++){
                Item item = inventory.InventorySlots[i][i2];
                if(item != null) {
                    item.Use(unit);
                    //inventory.InventorySlots[i][i2].
                }
            }
        }
    }

}
