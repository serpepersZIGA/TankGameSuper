package com.mygdx.game.Inventory;

public class Slot {
    public int x,y,width,height,width2,height2;
    public Item item;

    public Slot(int x,int y,int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.width2 = (int) (width*0.5);
        this.height2 = (int) (height*0.5);

    }

    public void SlotUpdate(){

    }
}
