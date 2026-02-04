package com.mygdx.game.Inventory;

import com.mygdx.game.method.Keyboard;
import com.mygdx.game.method.RenderMethod;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.Keyboard.MouseX;
import static com.mygdx.game.method.Keyboard.MouseY;
import static java.lang.Math.abs;

public class WindowName {
    public int x,y,width = 120,height = 45,height2 = (int) (height*0.5);
    public String str="";
    public boolean conf=false;

    public WindowName(){

    }
    public void MouseCursorHover(int x, int y,Slot slot){
        if(abs(MouseX-x-slot.width2)<slot.width2 && abs(MouseY-y-slot.height2)<slot.height2){
            this.str = slot.item.ID;
            this.x = MouseX;
            this.y = MouseY;
            this.conf = true;
            //RenderWindow(Keyboard.MouseX,Keyboard.MouseY,str);
        }
    }
    public void RenderWindow(){
        RenderMethod.transorm_img(this.x,this.y,width,height,TextureAtl.createSprite("InventoryBackground"));
        font2.setColor(0.1f,0.9f,0.8f,1f);
        font2.draw(Batch,this.str,x,y+height2);

    }
}
