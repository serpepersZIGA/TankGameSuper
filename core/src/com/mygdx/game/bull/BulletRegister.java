package com.mygdx.game.bull;

import com.mygdx.game.FunctionalComponent.FunctionalBullet.FunctionalComponentBulletRegister;
import com.mygdx.game.FunctionalComponent.FunctionalList;

import java.util.ArrayList;

import static Data.DataColor.*;

public class BulletRegister {
    public static ArrayList<Object[]>IDBullet =new ArrayList<>();
    public static ArrayList<BullPacket> PacketBull = new ArrayList<>();
    public static Bullet BulletMortar,BulletAcid,BulletFlame,BulletTank,BulletFragment;
    public static void BulletRegisterAdd() {
        FunctionalList func = new FunctionalList();
        BulletMortar = new BulletPattern(MortarR,MortarG,MortarB,true,8,0,0,1,func,true,
                false,false);
        BulletAcid = new BulletPattern(AcidR,AcidG,AcidB,true,10,0,7,2,func,false
                ,true,false);
        BulletTank = new BulletPattern(MortarR,MortarG,MortarB,true,6,0,0,3,func,false,false,
                false);
        BulletFragment = new BulletPattern(FlameR,FlameG,FlameB,false,8,8,4,4,func,false,
                false,true);
        func = new FunctionalList();
        func.Add(FunctionalComponentBulletRegister.ColorFire);
        BulletFlame = new BulletPattern(MortarR,MortarG,MortarB,true,12,0,7,5,func,false,
                false,true);
    }




}
