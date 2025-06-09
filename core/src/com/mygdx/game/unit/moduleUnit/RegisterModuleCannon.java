package com.mygdx.game.unit.moduleUnit;

import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.unit.Fire.FireRegister;

import static com.mygdx.game.main.Main.*;

public class RegisterModuleCannon {
    public static Cannon CannonFlame,CannonMortar,CannonFlameAuxiliary, CannonMachineGun,CannonMachineGunAuxiliary,CannonAcid;
    public static void Create(){
        FunctionalList func = new FunctionalList();
        func.Add(RegisterFunctionalComponent.TowerXY);
        func.Add(RegisterFunctionalComponent.TowerControl);
        func.Add(RegisterFunctionalComponent.FireControl);
        CannonFlame = new Cannon(55,35,34,17,1f,
                 10,6,0,0,1.5f,2
                ,2,4,85,15,20, FireRegister.FireFlame,ContentImage.tower_enemy
                ,func,ContentSound.flame_attack,0);
        CannonFlameAuxiliary = new Cannon(20,15,12,7,1f,
                10,4,0,0,1.5f,2
                ,2,4,85,15,20, FireRegister.FireFlame,ContentImage.tower_enemy_auxiliary_1
                ,func, ContentSound.flame_attack,0);
        CannonMortar = new Cannon(55,35,34,17,1f,
                320,15,22,25,1.5f,2
                ,400,4,400,15,20, FireRegister.FireMortar,ContentImage.tower_enemy
                ,func,ContentSound.cannon,120);
        CannonMachineGun = new Cannon(55,35,34,17,1.5f,
                70,22,22,25,0f,2
                ,5,12,650,15,20, FireRegister.FireBullet,ContentImage.tower_enemy
                ,func,ContentSound.machinegun,2);
        CannonMachineGunAuxiliary = new Cannon(20,15,12,7,1f,
                70,22,0,0,1.5f,2
                ,5,12,650,15,20, FireRegister.FireBullet,ContentImage.tower_enemy_auxiliary_1
                ,func, ContentSound.machinegun,2);
        CannonAcid = new Cannon(55,35,34,17,1f,
                20,6,0,0,0,2
                ,2,4,85,15,20, FireRegister.FireAcid,ContentImage.tower_enemy
                ,func,ContentSound.acid_attack,0);
//        CannonMortar = new Cannon(55,35,34,17,1f,
//                20,6,20,25,1.5f,2
//                ,2,400,40,15,20, FireRegister.FireFlame,ContentImage.tower_player
//                ,func,ContentSound.flame_attack);

    }
}
