package com.mygdx.game.Inventory;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.unit.Unit;
import com.mygdx.game.unit.moduleUnit.Gun;

import java.util.ArrayList;


import static com.mygdx.game.main.Main.ItemPackList;
import static com.mygdx.game.main.Main.UnitList;
import static com.mygdx.game.method.pow2.pow2;
import static java.lang.Math.sqrt;

public class ItemObject{
    public static ArrayList<ItemObject>ItemList = new ArrayList<>();

    //public String ID;
    public Item item;
    public static int LineSelection = 70;
    public int x,y,x_rend,y_rend;
    public static int width = 35,height = 35,widthRender = width,heightRender = height;
    public ItemObject(Item item,int x,int y){
        this.x = x;
        this.y = y;
        this.item = item;
        //width = 20;
        //height = 20;
        //this.ID = item.ID;

    }
    public void IterationItem(){
        CenterRender();
        RenderMethod.transorm_img(x_rend,y_rend,widthRender,heightRender,item.image);
        Press();
        ItemPacket pack = new ItemPacket();
        pack.ID = item.ID;
        pack.x = x;
        pack.y = y;
        ItemPackList.add(pack);
        //Server.sendUDP();
    }
    public void IterationItemClient(){
        CenterRender();
        RenderMethod.transorm_img(x_rend,y_rend,widthRender,heightRender,item.image);
        //Press();


    }
    protected void Press(){
        for (Unit unit : UnitList) {
            if (unit.press_f & sqrt(pow2(this.x-unit.x)+pow2(this.y-unit.y))<LineSelection) {
                unit.inventory.ItemAdd(this.item);
                ItemList.remove(this);
                unit.press_f = false;
                    //unit.inventory.ItemAdd(item);
            }
        }
    }
    protected void CenterRender(){
        int[]xy = Main.RC.render_objZoom(this.x,this.y);
        this.x_rend = xy[0];
        this.y_rend = xy[1];
    }
}
