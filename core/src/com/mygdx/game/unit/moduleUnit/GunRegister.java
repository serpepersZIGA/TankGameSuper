package com.mygdx.game.unit.moduleUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Fire.FireRegister;

import static com.mygdx.game.FunctionalComponent.FunctionalUnit.FunctionalComponentUnitRegister.FireDrumControl;
import static com.mygdx.game.FunctionalComponent.FunctionalUnit.FunctionalComponentUnitRegister.FireTankCumulativeControl;
import static com.mygdx.game.main.Main.ContentSound;

public class GunRegister{
    public static Gun AK74,Void,flamethrower;
    public static void Create(){
        flamethrower = new Gun(12,2,0,0,0,400
                ,200,
                FireRegister.FireFlame,ContentSound.flame_attack,0,
                4,250,0,
                0,0.2f,2,FireTankCumulativeControl
                ,"flamethrower");
        AK74 = new Gun(20,3,0,0,0
                ,5,200,
                FireRegister.FireBullet,ContentSound.machinegun,
                0,10,30,3,70
                ,0,7,FireDrumControl,"AK74");
        Void = new Gun();

    }
}
