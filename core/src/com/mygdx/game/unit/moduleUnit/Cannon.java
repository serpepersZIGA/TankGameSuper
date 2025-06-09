package com.mygdx.game.unit.moduleUnit;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.unit.Fire.Fire;
import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.unit.Unit;

public class Cannon extends moduleUnit implements Cloneable{
    public Fire fire;
    public int WidthTower,WidthTower2;
    public int HeightTower,HeightTower2;
    public int ConstTowerX;
    public int ConstTowerY;
    public float damage,penetration,SpeedBullet;
    public float SpeedRotationTower;
    public int TimeBullet,TimeBulletRand,ReloadMax,SizeBullet;
    public int Reload,towerXConst,towerYConst;
    public float RotationTower;
    public boolean FireConf;
    public float XRend,YRend;
    public Sound SoundFire;
    public float fire_x;
    public float fire_y;
    public FunctionalList functional = new FunctionalList();
    public float damage_fragment, penetration_fragment, TemperatureDamage;
    public int differenceY, differenceX,TowerX,TowerY,XTower, YTower,CenterX,CenterY,AmountFragment;
    public int XTowerRend,YTowerRend,TowerWidth2,TowerHeight2;
    public Sound sound;
    public Sprite image;

    public Cannon(int WidthTower, int HeightTower, int ConstTowerX, int ConstTowerY, float SpeedRotationTower,
                  float damage, float penetration, float damage_fragment, float penetration_fragment, float TemperatureDamage,
                  int SizeBullet, int ReloadMax, float SpeedBullet, int TimeBullet,int TowerX,int TowerY, Fire fire, Sprite image,FunctionalList functional
            ,Sound sound,int AmountFragment){
        this.AmountFragment = AmountFragment;
        this.WidthTower = WidthTower;
        this.HeightTower = HeightTower;
        WidthTower2 = WidthTower/2;
        HeightTower2 = HeightTower/2;
        this.ConstTowerX = ConstTowerX;
        this.ConstTowerY = ConstTowerY;
        this.SpeedRotationTower = SpeedRotationTower;
        this.fire = fire;
        this.damage = damage;
        this.penetration = penetration;
        this.SpeedBullet = SpeedBullet;
        this.TimeBullet =  TimeBullet;
        this.ReloadMax =  ReloadMax;
        this.SizeBullet =  SizeBullet;
        this.damage_fragment =  damage_fragment;
        this.penetration_fragment =  penetration_fragment;
        this.TemperatureDamage =  TemperatureDamage;
        this.TowerX = TowerX;
        this.TowerY = TowerY;
        this.image =  image;
        this.functional = functional.clone();
        this.sound = sound;
        //IDList.add(ID);


    }
    public Cannon(int WidthTower, int HeightTower, int ConstTowerX, int ConstTowerY, float SpeedRotationTower,
                  float damage, float penetration
            , float damage_fragment, float penetration_fragment, float TemperatureDamage, int SizeBullet, int ReloadMax,
                  float SpeedBullet, int TimeBullet, int TimeBulletRand,int TowerX,int TowerY, Fire fire,Sprite image,FunctionalList functional
            ,Sound sound,int AmountFragment){
        this.AmountFragment = AmountFragment;
        this.WidthTower = WidthTower;
        this.HeightTower = HeightTower;
        WidthTower2 = WidthTower/2;
        HeightTower2 = HeightTower/2;
        this.ConstTowerX = ConstTowerX;
        this.ConstTowerY = ConstTowerY;
        this.SpeedRotationTower = SpeedRotationTower;
        this.fire = fire;
        this.damage = damage;
        this.penetration = penetration;
        this.SpeedBullet = SpeedBullet;
        this.TimeBullet =  TimeBullet;
        this.TimeBulletRand =  TimeBulletRand;
        this.ReloadMax =  ReloadMax;
        this.SizeBullet =  SizeBullet;
        this.damage_fragment =  damage_fragment;
        this.penetration_fragment =  penetration_fragment;
        this.TemperatureDamage =  TemperatureDamage;
        this.TowerX = TowerX;
        this.TowerY = TowerY;
        this.image =  image;
        this.functional = functional.clone();
        this.sound = sound;
    }
    public Cannon CannonAdd(Unit unit,int differenceX,int differenceY){
        Cannon CannonAdd;
        try {
            CannonAdd = (Cannon) this.clone();
            CannonAdd.differenceX = differenceX;
            CannonAdd.differenceY = differenceY;

            CannonAdd.CenterX = unit.CorpusUnit.corpus_width_2-WidthTower2-6;
            CannonAdd.CenterY = unit.CorpusUnit.corpus_height_2-HeightTower2;
            //CannonAdd.ConstTowerX = unit.const_tower_x;
            //CannonAdd.ConstTowerY = unit.const_tower_y;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return CannonAdd;

    }
    public void CannonLoad(Unit unit){
        unit.damage = damage;
        unit.tower_img = image;
        unit.t_damage = TemperatureDamage;
        unit.width_tower = WidthTower;
        unit.height_tower = HeightTower;
        unit.tower_width_2 = WidthTower2;
        unit.tower_height_2 = HeightTower2;
        unit.damage_fragment = damage_fragment;
        unit.penetration_fragment = penetration_fragment;
        unit.SizeBullet = SizeBullet;
        unit.tower_x_const =CenterX;
        unit.tower_y_const = CenterY;
        unit.speed_tower = SpeedRotationTower;
        unit.SpeedBullet = SpeedBullet;
        unit.difference_2 = differenceX;
        unit.difference = differenceY;
        unit.reload_max = ReloadMax;
        unit.sound_fire = sound;
        unit.AmountFragment = AmountFragment;

        unit.const_tower_x = ConstTowerX;
        unit.const_tower_y = ConstTowerY;

        unit.TimeBullet = TimeBullet;
        unit.TimeBulletRand = TimeBulletRand;
        unit.fire = fire;
        for(int i = 0;i<functional.functional.size();i++){
            unit.functional.Add(functional.functional.get(i));
        }
        //unit.tower_x_const =XTower;
        //unit.tower_y_const =YTower;
        unit.x_tower = TowerX;
        unit.y_tower = TowerY;

    }
//    public static void CannonAdd(Cannon cannon, Unit unit,int differenceX,int differenceY){
//        try {
//            Cannon cannonAdd = (Cannon) cannon.clone();
//            CannonSpecifications(cannonAdd,differenceX,differenceY);
//            unit.CannonUnit.add(cannonAdd);
//        } catch (CloneNotSupportedException e) {
//            //throw new RuntimeException(e);
//        }
//    }

}
