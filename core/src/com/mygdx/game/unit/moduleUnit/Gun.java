package com.mygdx.game.unit.moduleUnit;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.unit.Fire.Fire;
import com.mygdx.game.unit.Fire.FireRegister;
import com.mygdx.game.unit.Fire.FireVoid;
import com.mygdx.game.unit.Unit;

public class Gun {
    public Fire fire;
    public float damage,penetration,SpeedBullet;
    public int TimeBullet,TimeBulletRand,ReloadMax;
    public int Reload;
    public float damage_fragment, penetration_fragment, TemperatureDamage;
    public int AmountFragment;
    public Sound sound;
    public Gun(){
        this.fire = FireRegister.FireVoid;

    }
    public Gun(float damage, float penetration, float damage_fragment, float penetration_fragment, float TemperatureDamage,
                int ReloadMax, int TimeBullet, Fire fire
            ,Sound sound,int AmountFragment,int SpeedBullet){
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
        unit.t_damage = TemperatureDamage;
        unit.damage_fragment = damage_fragment;
        unit.penetration_fragment = penetration_fragment;
        unit.SpeedBullet = SpeedBullet;
        unit.reload_max = ReloadMax;
        unit.sound_fire = sound;
        unit.AmountFragment = AmountFragment;

        unit.TimeBullet = TimeBullet;
        unit.TimeBulletRand = TimeBulletRand;
        unit.fire = fire;
        //unit.tower_x_const =XTower;
        //unit.tower_y_const =YTower;

    }
}
