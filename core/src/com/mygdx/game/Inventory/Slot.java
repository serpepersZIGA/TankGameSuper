package com.mygdx.game.Inventory;

public class Slot {
    public int x,y,width,height,width2,height2,IX,IY;
    public Item item;

    public Slot(int x,int y,int width,int height,int IX,int IY){
        this.x = x;
        this.y = y;
        this.IX = IX;
        this.IY = IY;
        this.width = width;
        this.height = height;
        this.width2 = (int) (width*0.5);
        this.height2 = (int) (height*0.5);

    }

    public void SlotUpdate(){

    }
}
