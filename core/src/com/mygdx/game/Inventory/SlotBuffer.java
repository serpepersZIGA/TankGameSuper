package com.mygdx.game.Inventory;

import com.mygdx.game.Event.EventDeleteItemClient;
import com.mygdx.game.Event.EventTransferItemClient;
import com.mygdx.game.main.ClientMain;
import com.mygdx.game.method.RenderMethod;

import static com.mygdx.game.Inventory.ItemObject.ItemList;
import static com.mygdx.game.main.ClientMain.Client;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.Keyboard.*;

public class SlotBuffer {
    public Slot slot;
    public Item item;
    public int x, y, width, height,ix,iy,XCol,YCol,XConst,YConst;

    public SlotBuffer(Slot slot, int x, int y, int width, int height,int ix,int iy) {
        this.item = slot.item;
        this.slot = slot;
        this.XConst = x;
        this.YConst = y;
        this.width = width;
        this.height = height;
        this.ix = ix;this.iy = iy;
    }

    public void SlotXY() {
        this.x = MouseX-XConst;
        this.y = MouseY-YConst;
    }

    public void SlotPaste() {
        if(!MiddleMouse){
            XCol = MouseX-(inventoryMain.x);
            YCol = MouseY-(inventoryMain.y);
            if(!(YCol < inventoryMain.HeightWindow & YCol > 0 & XCol<inventoryMain.WidthWindow &XCol> 0)){
                ItemList.add(new ItemObject(slot.item, (int) RC.x, (int) RC.y));
                slot.item = null;
                inventoryMain.slotBuf = null;
                inventoryMain.inventory.InventorySlots[ix][iy] = null;
                inventoryMain.SlotBuffer = null;
                return;
            }
            int ix2 = 0;
            int iy2 = 0;
            for(Slot[] SlotLine : inventoryMain.SlotInventory){
                for(Slot Slot : SlotLine) {
                    XCol = MouseX-(Slot.x+inventoryMain.x);
                    YCol = MouseY-(Slot.y+inventoryMain.y);
                    if(YCol < Slot.height & YCol > 0 & XCol<Slot.width &XCol> 0){
                        Item item1 = item;
                        Item item2 = Slot.item;
//                        if(item2!= null){
//                            slot.item = item2.clone();
//                            inventoryMain.inventory.InventorySlots[ix][iy] = item2.clone();
//                        }
                        if(item1!= null){
                            Slot.item = item1.clone();
                            inventoryMain.inventory.InventorySlots[ix2][iy2] = item1.clone();
                            if(item2!= null){
                                slot.item = item2.clone();
                                inventoryMain.inventory.InventorySlots[ix][iy] = item2.clone();
                            }
                            else{
                                slot.item = null;
                                inventoryMain.inventory.InventorySlots[ix][iy] = null;
                            }
                        }
                        inventoryMain.slotBuf = null;
                        inventoryMain.SlotBuffer = null;
                        return;
                    }
                    iy2++;
                }
                ix2++;
                iy2 = 0;

            }
            inventoryMain.SlotBuffer = null;
            inventoryMain.slotBuf = null;
        }
    }

    public void SlotPasteClient() {
        if(!MiddleMouse) {
            XCol = MouseX - (inventoryMain.x);
            YCol = MouseY - (inventoryMain.y);
            if (!(YCol < inventoryMain.HeightWindow & YCol > 0 & XCol < inventoryMain.WidthWindow & XCol > 0)) {
                slot.item = null;
                inventoryMain.slotBuf = null;
                inventoryMain.inventory.InventorySlots[ix][iy] = null;
                inventoryMain.SlotBuffer = null;
                EventDeleteItemClient event = new EventDeleteItemClient();
                event.x = ix;
                event.y = iy;
                for (int i = 0; i < UnitList.size(); i++) {
                    if (IDClient == UnitList.get(i).nConnect) {
                        event.i = i;
                        break;
                    }
                }
                Client.sendTCP(event);
            }
            int ix2 = 0;
            int iy2 = 0;
            for (Slot[] SlotLine : inventoryMain.SlotInventory) {
                for (Slot Slot : SlotLine) {
                    XCol = MouseX - (Slot.x + inventoryMain.x);
                    YCol = MouseY - (Slot.y + inventoryMain.y);
                    if (YCol < Slot.height & YCol > 0 & XCol < Slot.width & XCol > 0) {
                        Item item1 = item;
                        Item item2 = Slot.item;
//                     if(item2!= null){
//                         slot.item = item2.clone();
//                         inventoryMain.inventory.InventorySlots[ix][iy] = item2.clone();
//                     }
                        if (item1 != null) {
                            EventTransferItemClient event = new EventTransferItemClient();
                            Slot.item = item1.clone();
                            inventoryMain.inventory.InventorySlots[ix2][iy2] = item1.clone();
                            if (item2 != null) {
                                slot.item = item2.clone();
                                inventoryMain.inventory.InventorySlots[ix][iy] = item2.clone();
                                event.item2 = inventoryMain.inventory.InventorySlots[ix][iy].ID;
                            } else {
                                slot.item = null;
                                event.item2 = null;
                                inventoryMain.inventory.InventorySlots[ix][iy] = null;
                            }

                            event.x = ix;
                            event.y = iy;
                            event.x2 = ix2;
                            event.y2 = iy2;
                            event.item1 = inventoryMain.inventory.InventorySlots[ix2][iy2].ID;
                            for (int i = 0; i < UnitList.size(); i++) {
                                if (IDClient == UnitList.get(i).nConnect) {
                                    event.i = i;
                                    break;
                                }
                            }
                            Client.sendTCP(event);
                        }
                        inventoryMain.slotBuf = null;
                        inventoryMain.SlotBuffer = null;
                        return;
                    }
                    iy2++;
                }
                ix2++;
                iy2 = 0;
            }
            inventoryMain.SlotBuffer = null;
            inventoryMain.slotBuf = null;
        }
    }

    public void SlotRender() {
        RenderMethod.transorm_img(this.x, this.y, this.width, this.height,item.image);
    }
}
