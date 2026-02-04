package com.mygdx.game.unit.moduleUnit;

import com.mygdx.game.unit.Fire.FireRegister;

import static com.mygdx.game.main.Main.ContentSound;

public class GunRegister{
    public static Gun AK74,Void,flamethrower;
    public static void Create(){
        flamethrower = new Gun(12,2,0,0,0,5,200,
                FireRegister.FireFlame,ContentSound.flame_attack,0,4);
        AK74 = new Gun(20,3,0,0,0,5,200,
                FireRegister.FireBullet,ContentSound.machinegun,0,7);
        Void = new Gun();

    }
}
