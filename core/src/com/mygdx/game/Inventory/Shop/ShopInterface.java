package com.mygdx.game.Inventory.Shop;

import com.mygdx.game.Event.EventUseClient;
import com.mygdx.game.Inventory.*;
import com.mygdx.game.main.ClientMain;
import com.mygdx.game.method.RenderMethod;

import java.util.ArrayList;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.Keyboard.MouseX;
import static com.mygdx.game.method.Keyboard.MouseY;
import static java.lang.StrictMath.sqrt;

public class ShopInterface extends InventoryInterface {
    public ShopInterface(Inventory ShopGlobal){
        this.inventory = ShopGlobal;
        WindowName = new WindowName();
        XInterface = ShopGlobal.xySize;
        YInterface = ShopGlobal.xySize;
        SlotInventory = new Slot[XInterface][YInterface];
        this.x = 420;this.y = 420;
        WidthWindow = 650;
        HeightWindow = 420;
        XSlots = WidthWindow/XInterface;
        YSlots = HeightWindow/YInterface;
        if(XSlots>YSlots){XSlots=YSlots;}
        else if(XSlots<YSlots){YSlots = XSlots;}
        SlotGeneration();
    }
    @Override final
    public void CollisionMouseItem(){
        int ix = 0;
        int iy = 0;
        for(Slot[] SlotLine : SlotInventory){
            for(Slot Slot : SlotLine) {
                    XCol2 = MouseX - (Slot.x + this.x);
                    YCol2 = MouseY - (Slot.y + this.y);
                    if (YCol2 < Slot.height & YCol2 > 0 & XCol2 < Slot.width & XCol2 > 0) {
                        if (SlotInventory[ix][iy].item != null) {
                            if(Slot.item.Price<Inventory.Money) {
                                if(GameHost) {
                                    inventoryMain.inventory.ItemAdd(Slot.item);
                                    inventoryMain.SlotGeneration();
                                    Inventory.Money -= Slot.item.Price;
                                }
                                else {
                                    for (int i = 0;i<UnitList.size();i++) {
                                        if(IDClient == UnitList.get(i).nConnect) {
                                            EventUseClient event = new EventUseClient();
                                            event.str = Slot.item.ID;
                                            //System.out.println(i);
                                            event.ID = i;
                                            event.conf = false;
                                            event.ConfUse = true;
                                            event.MoneyAdd = true;
                                            ClientMain.Client.sendTCP(event);

                                            inventoryMain.inventory.ItemAdd(Slot.item);
                                            inventoryMain.SlotGeneration();
                                            Inventory.Money -= Slot.item.Price;
                                        }
                                    }
                                }
                            }
                        }
                        return;
                    }

                    iy++;

            }
            ix++;
            iy = 0;

        }
    }

}

