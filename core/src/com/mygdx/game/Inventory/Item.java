package com.mygdx.game.Inventory;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.unit.ClassUnit;
import com.mygdx.game.unit.Unit;
import com.mygdx.game.unit.moduleUnit.Gun;

import java.util.ArrayList;

import static com.mygdx.game.main.Main.Option;

public class Item implements Cloneable{
    public String ID;
    public static ArrayList<Object[]>IDListItem = new ArrayList<>();
    public Sprite image;
    public TypeItem typeItem;
    public ArrayList<TegItem>teg;
    public Gun gun;
    public float Reload;
    public int HPHill;
    public Item(Gun gun,ArrayList<TegItem>teg){
        this.teg = teg;
        this.gun = gun;
        this.typeItem = TypeItem.Gun;
    }
    public Item(Gun gun,String ID,ArrayList<TegItem>teg,Sprite image){
        this.teg = teg;
        this.ID = ID;
        this.gun = gun;
        this.image = image;
        this.typeItem = TypeItem.Gun;
        IDListItem.add(new Object[]{this,ID});
    }
    public Item(int HPHill,String ID,ArrayList<TegItem>teg,Sprite image){
        this.teg = teg;
        this.ID = ID;
        this.HPHill = HPHill;

        this.image = image;
        this.typeItem = TypeItem.Medic;
        IDListItem.add(new Object[]{this,ID});
    }
    public boolean Use(Unit unit){

        switch (typeItem) {
            case Gun:
                //unit.GunUse.Reload = unit.reload;
                //unit.reload = this.Reload;
                //unit.GunUse = this;
                if(unit.classUnit == ClassUnit.Soldat) {
                    gun.GunLoad(unit);
                }
                else if(unit.classUnit == ClassUnit.Transport){
                    for(Unit tower : unit.tower_obj) {
                        gun.GunLoad(tower);
                    }
                }
                break;
            case Medic:
                if(unit.max_hp>=unit.hp+HPHill){
                    unit.hp+=HPHill;
                    unit.green_len = ((float) unit.hp / unit.max_hp) * Option.size_x_indicator;
                    return true;

                }
                break;
        }
        return false;

    }
    public void UseNull(Unit unit){
        switch (typeItem){
            case Gun:
                gun.GunLoad(unit);
                break;
            case Medic:
                if(unit.max_hp>=unit.hp+HPHill){
                    unit.hp+=HPHill;
                }
                break;
        }
    }
    public Item clone(){
        try {
            return (Item) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

}
