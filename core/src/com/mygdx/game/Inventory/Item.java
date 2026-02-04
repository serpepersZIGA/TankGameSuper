package com.mygdx.game.Inventory;

import com.mygdx.game.unit.ClassUnit;
import com.mygdx.game.unit.Unit;
import com.mygdx.game.unit.moduleUnit.Gun;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mygdx.game.main.Main.Option;

public class Item implements Cloneable{
    public String ID;
    public static HashMap<String,Item>IDListItem = new HashMap<>();
    public String image;
    public TypeItem typeItem;
    public ArrayList<TegItem>teg;
    public Gun gun;
    public int HPHill;
    public float HPPercent,HP, Armor, ArmorPercent,DamagePercent,PenetrationPercent,
            MoveUPPercent,MoveDownPercent, AccelerationPercent;
    public Item(Gun gun,ArrayList<TegItem>teg){
        this.teg = teg;
        this.gun = gun;
        this.typeItem = TypeItem.Gun;
    }
    public Item(float HPPercent,float HP,float Armor,float ArmorPercent
            ,float DamagePercent,float PenetrationPercent,
                int MoveUPPercent,int MoveDownPercent,int AccelerationPercent
            ,String ID,ArrayList<TegItem>teg,String image){
        this.teg = teg;
        this.ID = ID;

        this.HPPercent = HPPercent;
        this.Armor = Armor;
        this.ArmorPercent = ArmorPercent;
        this.DamagePercent = DamagePercent;
        this.HP = HP;
        this.PenetrationPercent = PenetrationPercent;
        this.MoveUPPercent = MoveUPPercent;
        this.MoveDownPercent = MoveDownPercent;
        this.AccelerationPercent = AccelerationPercent;

        this.image = image;
        this.typeItem = TypeItem.Upgrade;
        IDListItem.put(ID,this);
    }
    public Item(Gun gun,String ID,ArrayList<TegItem>teg,String image){
        this.teg = teg;
        this.ID = ID;
        this.gun = gun;
        this.image = image;
        this.typeItem = TypeItem.Gun;
        IDListItem.put(ID,this);
    }
    public Item(int HPHill,String ID,ArrayList<TegItem>teg,String image){
        this.teg = teg;
        this.ID = ID;
        this.HPHill = HPHill;

        this.image = image;
        this.typeItem = TypeItem.Medic;
        IDListItem.put(ID,this);
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
                case Upgrade:
                {
                    unit.max_hp+= (int) HP;
                    unit.armor+= Armor;
                    unit.hp += (int) HP;
                    unit.max_hp += (int) (unit.HpBase * HPPercent*0.01f);
                    unit.hp += (int) (unit.HpBase * HPPercent*0.01f);
                    unit.armor += (int) (unit.ArmorBase * ArmorPercent*0.01f);
                    unit.Acceleration +=  unit.AccelerationBase * AccelerationPercent*0.01f;
                    unit.SpeedUp += unit.SpeedUpBase *MoveUPPercent*0.01f;
                    unit.SpeedDown += unit.SpeedDownBase *MoveDownPercent*0.01f;

                    for (Unit cannon : unit.tower_obj) {
                        cannon.damage += (int) (cannon.DamageBase*DamagePercent*0.01);
                    }
                    for (Unit cannon : unit.tower_obj) {
                        cannon.penetration += (int) (cannon.PenetrationBase*PenetrationPercent*0.01);
                    }
                    unit.green_len = ((float) unit.hp / unit.max_hp) * Option.size_x_indicator;
                    return true;
                }


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
