package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;

public class FunctionalComponentUnitRegister {
    public FunctionalComponent TowerXY,MotorControl,BuildCollision,Hill,SoldatSpawn,TowerIteration,FireControl;
    public FunctionalComponent SoldatControl,SoldatCorrect,WorkBlade,MoveDebris,TowerControl;
    public FunctionalComponentUnitRegister(){
        TowerControl = new ComponentTowerControl();
        MoveDebris = new ComponentMoveDebris();
        WorkBlade = new ComponentWorkBlade();
        FireControl = new ComponentFireControl();
        SoldatCorrect = new ComponentSoldatCorrect();
        TowerXY = new ComponentTowerXY();
        MotorControl = new ComponentMotorControl();
        BuildCollision = new ComponentBuildingCollision();
        Hill = new ComponentHill();
        SoldatSpawn = new ComponentSpawnSoldat();
        TowerIteration = new ComponentTowerAuxiliaryIteration();
        SoldatControl = new ComponentSoldatControl();
    }
}
