package com.mygdx.game.Inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Event.EventGame;
import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.unit.Unit;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.Keyboard.MouseX;
import static com.mygdx.game.method.Keyboard.MouseY;

public class InventoryInterface {
    public static boolean InventoryConf = false;
    public static boolean InventoryConfMoving = false;
    public int XCol,YCol,XColUs,YColUs,XCol2,YCol2;
    public int XInterface,YInterface,XSlots,YSlots,WidthWindow,HeightWindow,x,y;
    public String frame = "frameSlot";
    public String frameInventory = "InventoryBackground";
    public Slot[][]SlotInventory;
    public SlotBuffer SlotBuffer;
    public Slot slotBuf;
    public Inventory inventory;
    public WindowName WindowName;
    public InventoryInterface(Inventory inventory,int x,int y,int width,int height){
        WindowName = new WindowName();
        XInterface = inventory.InventorySlots.length;
        YInterface = inventory.InventorySlots[0].length;
        this.inventory = inventory;
        SlotInventory = new Slot[XInterface][YInterface];
        this.x = x;this.y = y;
        WidthWindow = width;
        HeightWindow = height;
        XSlots = width/XInterface;
        YSlots = height/YInterface;
        if(XSlots>YSlots){XSlots=YSlots;}
        else if(XSlots<YSlots){YSlots = XSlots;}
        SlotGeneration(inventory);
    }
    public InventoryInterface(){
        SlotInventory = new Slot[0][0];
        this.x = 200;this.y = 1000;
        WidthWindow = 600;
        HeightWindow = 350;
    }
    public void SlotGeneration(Inventory inventory){
        for(int ix = 0;ix<inventory.InventorySlots.length;ix++){
            for(int iy = 0;iy<inventory.InventorySlots[ix].length;iy++){
                SlotInventory[ix][iy] = new Slot(XSlots*ix,YSlots*iy,XSlots,YSlots);
                Slot slot = SlotInventory[ix][iy];
                slot.item = inventory.InventorySlots[ix][iy];
            }
        }
    }
    public void InventoryIteration(){
        if(InventoryConf) {
            ix = 0;
            iy = 0;
            RenderMethod.transorm_img(this.x, this.y, this.WidthWindow, this.HeightWindow,
                    TextureAtl.createSprite(frameInventory));
            for (Slot[] slotX : SlotInventory) {
                for (Slot slot : slotX) {
                    RenderMethod.transorm_img(slot.x+x, slot.y+y, slot.width,
                            slot.height, TextureAtl.createSprite(frame));
                    if(inventory.InventorySlots[ix][iy] != null & slotBuf !=slot) {
                        RenderMethod.transorm_img(slot.x + x, slot.y + y, slot.width, slot.height,
                                TextureAtl.createSprite(inventory.InventorySlots[ix][iy].image));
                    }
                    if(slot.item != null) {
                        WindowName.MouseCursorHover(slot.x + this.x, slot.y + this.y, slot);
                    }
                    iy++;
                }
                ix++;
                iy = 0;
            }
            if(InventoryConfMoving){
                x = MouseX-XCol;
                y = MouseY-YCol;
            }
            if(SlotBuffer != null){
                SlotBuffer.SlotXY();
                SlotBuffer.SlotRender();
                SlotBuffer.SlotPaste();
            }
            if(WindowName.conf){
                WindowName.RenderWindow();
            }
            WindowName.conf = false;
        }
    }
    public void InventoryIterationClient(){
        if(InventoryConf) {
            ix = 0;
            iy = 0;
            RenderMethod.transorm_img(this.x, this.y, this.WidthWindow,
                    this.HeightWindow, TextureAtl.createSprite(frameInventory));
            for (Slot[] slotX : SlotInventory) {
                for (Slot slot : slotX) {
                    RenderMethod.transorm_img(slot.x+x, slot.y+y, slot.width, slot.height,
                            TextureAtl.createSprite(frame));
                    if(inventory.InventorySlots[ix][iy] != null & slotBuf !=slot) {
                        RenderMethod.transorm_img(slot.x + x, slot.y + y,
                                slot.width, slot.height, TextureAtl.createSprite(inventory.InventorySlots[ix][iy].image));
                    }
                    if(slot.item != null) {
                        WindowName.MouseCursorHover(slot.x + this.x, slot.y + this.y, slot);
                    }

                    iy++;
                }
                ix++;
                iy = 0;
            }
            if(InventoryConfMoving){
                x = MouseX-XCol;
                y = MouseY-YCol;
            }
            if(SlotBuffer != null){
                SlotBuffer.SlotXY();
                SlotBuffer.SlotRender();
                SlotBuffer.SlotPasteClient();
            }
            if(WindowName.conf){
                WindowName.RenderWindow();
            }
            WindowName.conf = false;
        }
    }
    public boolean CollisionMouseInvert(){
        XCol = MouseX-this.x;
        YCol = MouseY-this.y;
        return YCol < HeightWindow & YCol > 0 & XCol<WidthWindow &XCol> 0;
        //return false;
    }
    public int ix,iy;
    public void CollisionMouseItem(){
        ix = 0;
        iy = 0;
        for(Slot[] SlotLine : SlotInventory){
            for(Slot Slot : SlotLine) {
                XCol2 = MouseX-(Slot.x+this.x);
                YCol2 = MouseY-(Slot.y+this.y);
                if(YCol2 < Slot.height & YCol2 > 0 & XCol2<Slot.width &XCol2> 0){
                    if(inventory.InventorySlots[ix][iy]!= null) {
                        Slot.item = inventory.InventorySlots[ix][iy];
                        slotBuf = Slot;
                        SlotBuffer = new SlotBuffer(Slot, XCol2, YCol2, Slot.width, Slot.height, ix, iy);
                    }
                    return;
                }
                iy++;
            }
            ix++;
            iy = 0;

        }
    }
    public void InventoryUs(Unit unit){
        ix = 0;
        iy = 0;
        for (Slot[] slots : SlotInventory) {
            for (Slot slot : slots) {

                XColUs = MouseX - (this.x + slot.x);
                YColUs = MouseY - (this.y + slot.y);
                if (YColUs < slot.height & YColUs > 0 & XColUs < slot.width & XColUs > 0) {
                    if (slot.item != null) {
                        if(slot.item.Use(unit)){
                            inventory.InventorySlots[ix][iy]= null;
                            SlotInventory[ix][iy].item= null;
                        }
                    }
                    return;
                }
                iy++;
                //return false;
            }
            iy = 0;
            ix++;
        }
    }
    public void InventoryUsClient(Unit unit){
        ix = 0;
        iy = 0;
        for (Slot[] slots : SlotInventory) {
            for (Slot slot : slots) {
                XColUs = MouseX - (this.x + slot.x);
                YColUs = MouseY - (this.y + slot.y);
                if (YColUs < slot.height & YColUs > 0 & XColUs < slot.width & XColUs > 0) {
                    if (slot.item != null) {
                        for (int i = 0; i < UnitList.size(); i++) {
                            EventGame.EventGameClient(slot.item.ID, i,ix,iy);
                        }
                        if(slot.item.Use(unit)) {
                            inventory.InventorySlots[ix][iy]= null;
                            SlotInventory[ix][iy].item = null;
                        }
                    }
                    return;
                }
                iy++;
                //return false;
            }
            iy = 0;
            ix++;
        }

    }
}
