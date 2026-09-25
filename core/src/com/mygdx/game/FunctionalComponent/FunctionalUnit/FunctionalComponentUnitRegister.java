package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;

public class FunctionalComponentUnitRegister {
    public static FunctionalComponent TowerXY,MotorControl,BuildCollision,Hill,SoldatSpawn,TowerIteration,FireControl;
    public static FunctionalComponent SoldatControl,SoldatCorrect,WorkBlade,MoveDebris,TowerControl,NotTowerControl,
            TowerPartCorpus,FireDrumControl,FireTankCumulativeControl;
    public FunctionalComponentUnitRegister(){
        TowerControl = new ComponentTowerControl();
        NotTowerControl = new ComponentNotTowerControl();
        TowerPartCorpus = new ComponentTowerPartCorpus();
        MoveDebris = new ComponentMoveDebris();
        WorkBlade = new ComponentWorkBlade();
        FireControl = new ComponentFireClassisControl();
        FireDrumControl = new ComponentFireDrumControl();
        FireTankCumulativeControl = new ComponentFireTankCumulativeControl();

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
