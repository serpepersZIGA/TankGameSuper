package com.mygdx.game.bull;

import com.mygdx.game.FunctionalComponent.FunctionalList;

public class BulletPattern extends Bullet{
    public BulletPattern(float r, float g, float b, boolean TypeShape, int width, int height, int SizeRandom, int ID, FunctionalList func
            ,boolean BangSpawn,boolean AcidSpawn,boolean FlameSpawn){

        super(r,g,b,TypeShape,width,height,SizeRandom,ID,func,BangSpawn,AcidSpawn,FlameSpawn);
    }

    public void BulletAdd(float x, float y, float rotation, float damage, float penetration, float damage_fragment,
                          float penetration_fragment, byte type_team, byte height,int tDamage,
                          float speed,int AmountFragment,int time){

        super.BulletAdd(x,y,rotation,damage,penetration,damage_fragment,penetration_fragment,
                type_team,height,tDamage,speed,AmountFragment,time);

    }
    public void all_action(){
        super.all_action();

    }


}
