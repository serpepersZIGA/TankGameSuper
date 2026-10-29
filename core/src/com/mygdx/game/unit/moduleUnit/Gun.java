package com.mygdx.game.unit.moduleUnit;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.FunctionalComponent.FunctionalUnit.FunctionalComponentUnitRegister;
import com.mygdx.game.unit.Fire.Fire;
import com.mygdx.game.unit.Fire.FireRegister;
import com.mygdx.game.unit.Fire.FireVoid;
import com.mygdx.game.unit.Unit;

import java.util.HashMap;

import static com.mygdx.game.FunctionalComponent.FunctionalUnit.FunctionalComponentUnitRegister.*;
import static com.mygdx.game.FunctionalComponent.FunctionalUnit.FunctionalComponentUnitRegister.FireDrumControl;

public class Gun {
    public Fire fire;
    public float damage,penetration,SpeedBullet;
    public int TimeBullet,TimeBulletRand,ReloadMax,QuantityShots,
    ReloadBetweenShots,ReloadShots ;
    public int Reload;
    public float damage_fragment, penetration_fragment, TemperatureDamage,ReloadUP,OneShot;
    public int AmountFragment;
    public Sound sound;
    public String ID;
    FunctionalComponent TypeFire;
    public static HashMap<String,Gun>GunList = new HashMap<>();
    public Gun(){
        this.fire = FireRegister.FireVoid;

    }
    public Gun(float damage, float penetration, float damage_fragment, float penetration_fragment, float TemperatureDamage,
               int ReloadMax, int TimeBullet, Fire fire
            , Sound sound, int AmountFragment, int SpeedBullet,
               int QuantityShots, int ReloadBetweenShots, int ReloadShots, float ReloadUP,
               float OneShot, FunctionalComponent TypeFire
            , String ID){

        this.QuantityShots = QuantityShots;
        this.ReloadBetweenShots = ReloadBetweenShots;
        this.ReloadShots = ReloadShots;
        this.ReloadUP = ReloadUP;
        this.OneShot = OneShot;
        this.damage = damage;
        this.SpeedBullet = SpeedBullet;
        this.penetration = penetration;
        this.damage_fragment = damage_fragment;
        this.penetration_fragment = penetration_fragment;
        this.TemperatureDamage = TemperatureDamage;
        this.ReloadMax = ReloadMax;
        this.TimeBullet = TimeBullet;
        this.fire = fire;
        this.sound = sound;
        this.AmountFragment = AmountFragment;
        this.TypeFire = TypeFire;
        this.ID = ID;
        GunList.put(ID,this);

    }
    public Cannon GunAdd(){
        Cannon CannonAdd;
        try {
            CannonAdd = (Cannon) this.clone();
            //CannonAdd.ConstTowerX = unit.const_tower_x;
            //CannonAdd.ConstTowerY = unit.const_tower_y;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return CannonAdd;

    }
    public void GunLoad(Unit unit){
        unit.damage = damage;
        unit.DamageBase = damage;
        unit.t_damage = TemperatureDamage;
        unit.damage_fragment = damage_fragment;
        unit.PenetrationBase = (int) penetration;
        unit.penetration = (int) penetration;
        unit.penetration_fragment = penetration_fragment;
        unit.SpeedBullet = SpeedBullet;
        unit.reload_max = ReloadMax;
        unit.sound_fire = sound;
        unit.AmountFragment = AmountFragment;

        for(int i = 0;i<unit.functional.functional.size();i++){
            FunctionalComponent func = unit.functional.functional.get(i);
            if(func == FireTankCumulativeControl || func ==FireDrumControl || func== FireControl){
                unit.functional.functional.remove(func);
                break;
                //unit.functional.functional.add(TypeFire);
            }
        }
        unit.functional.functional.add(TypeFire);

        unit.QuantityShots = QuantityShots;
        unit.ReloadUP = ReloadUP;
        unit.OneShot = OneShot;
        unit.ReloadBetweenShots = ReloadBetweenShots;
        unit.ReloadShots = ReloadShots;

        unit.TimeBullet = TimeBullet;
        unit.TimeBulletRand = TimeBulletRand;
        unit.fire = fire;
        //unit.tower_x_const =XTower;
        //unit.tower_y_const =YTower;

    }
}
