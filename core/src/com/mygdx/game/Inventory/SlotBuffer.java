package com.mygdx.game.Inventory;

import com.mygdx.game.Event.EventDeleteItemClient;
import com.mygdx.game.Event.EventTransferItemClient;
import com.mygdx.game.main.ClientMain;
import com.mygdx.game.method.RenderMethod;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.Inventory.ItemObject.ItemList;
import static com.mygdx.game.main.ClientMain.Client;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.main.Main.inventoryMain;
import static com.mygdx.game.method.Keyboard.*;

public class SlotBuffer {
    public Slot slot;
    public Item item;
    public int x, y, width, height,ix,iy,XCol,YCol,XConst,YConst;
    public boolean ConfInterface;
    public InventoryInterface inventoryBuffer;

    public SlotBuffer(Slot slot, int x, int y, int width, int height,int ix,int iy,InventoryInterface inventoryBuffer) {
        this.item = slot.item;
        this.slot = slot;
        this.XConst = x;
        this.YConst = y;
        this.width = width;
        this.height = height;
        this.ix = ix;this.iy = iy;
        this.inventoryBuffer = inventoryBuffer;
    }

    public void SlotXY() {
        this.x = MouseX-XConst;
        this.y = MouseY-YConst;
    }

    public void SlotPaste() {
        if(!MiddleMouse){
            XCol = MouseX-(inventoryMain.x);
            YCol = MouseY-(inventoryMain.y);

            int XCol2 = MouseX-(equipmentMain.x);
            int YCol2 = MouseY-(equipmentMain.y);
            if(!(YCol < inventoryMain.HeightWindow & YCol > 0 & XCol<inventoryMain.WidthWindow &XCol> 0) &
                    !(YCol2 < equipmentMain.HeightWindow & YCol2 > 0 & XCol2<equipmentMain.WidthWindow &XCol2> 0)
            ){
                ItemList.add(new ItemObject(slot.item, (int) RC.x, (int) RC.y));
                slot.item = null;
                InventoryInterface.slotBuf = null;
                inventoryMain.inventory.InventorySlots[ix][iy] = null;
                InventoryInterface.SlotBuffer = null;
                return;
            }
            int ix = 0;
            int iy = 0;
            for(Slot[] SlotLine : inventoryMain.SlotInventory){
                for(Slot Slot : SlotLine) {
                    if(SlotPaste(Slot,ix,iy,inventoryMain)){
                        return;
                    }
                    iy++;
                }
                ix++;
                iy = 0;

            }

            ix = 0;
            for(Slot[] SlotLine : equipmentMain.SlotInventory){
                for(Slot Slot : SlotLine) {
                    if(SlotPaste(Slot,ix,iy,equipmentMain)){
                        //equipmentMain.StrengtheningInitialization();
                        return;
                    }
                    iy++;
                }
                ix++;
                iy = 0;

            }
            InventoryInterface.SlotBuffer = null;
            InventoryInterface.slotBuf = null;
        }
    }
    public boolean SlotPaste(Slot Slot,int ix,int iy,InventoryInterface inventoryMain) {
        XCol = MouseX - (Slot.x + inventoryMain.x);
        YCol = MouseY - (Slot.y + inventoryMain.y);
        if (YCol < Slot.height & YCol > 0 & XCol < Slot.width & XCol > 0) {
            Item item1 = item;
            Item item2 = Slot.item;
//                        if(item2!= null){
//                            slot.item = item2.clone();
//                            inventoryMain.inventory.InventorySlots[ix][iy] = item2.clone();
//                        }
            if (item1 != null) {
                Slot.item = item1.clone();
                inventoryMain.inventory.InventorySlots[ix][iy] = item1.clone();
                if (item2 != null) {
                    slot.item = item2.clone();
                    inventoryBuffer.inventory.InventorySlots[this.ix][this.iy] = item2.clone();
                } else {
                    slot.item = null;
                    inventoryBuffer.inventory.InventorySlots[this.ix][this.iy] = null;
                }
            }
            InventoryInterface.slotBuf = null;
            InventoryInterface.SlotBuffer = null;
            return true;
        }
        return false;
    }

    public void SlotPasteClient() {
        if(!MiddleMouse) {
            XCol = MouseX - (inventoryMain.x);
            YCol = MouseY - (inventoryMain.y);
            if (!(YCol < inventoryMain.HeightWindow & YCol > 0 & XCol < inventoryMain.WidthWindow & XCol > 0)) {
                slot.item = null;
                InventoryInterface.slotBuf = null;
                inventoryMain.inventory.InventorySlots[ix][iy] = null;
                InventoryInterface.SlotBuffer = null;
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
                        InventoryInterface.slotBuf = null;
                        InventoryInterface.SlotBuffer = null;
                        return;
                    }
                    iy2++;
                }
                ix2++;
                iy2 = 0;
            }
            InventoryInterface.SlotBuffer = null;
            InventoryInterface.slotBuf = null;
        }
    }
    public void DropEvent(){
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
    public void SlotRender() {
        RenderMethod.transorm_img(this.x, this.y, this.width, this.height,TextureAtl.createSprite(item.image));
    }
}
