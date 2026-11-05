package com.mygdx.game.unit.Controller;

import com.mygdx.game.main.Main;
import com.mygdx.game.unit.Unit;

public class ControllerBotSupport extends Controller{
    public void ControllerIteration(Unit unit){
        unit.bypass_hiller();

        for (Unit Tower : unit.TowerUnitList){
            Tower.TargetX = unit.TargetX;
            Tower.TargetY = unit.TargetY;
            Tower.trigger_fire = unit.trigger_fire;
            func(Tower);
            //Tower.tower_ii();
            //Tower.tower_ii();
        }
    }
    private void func(Unit unit){
        for (Unit Tower : unit.TowerUnitList){
            Tower.TargetX = unit.TargetX;
            Tower.TargetY = unit.TargetY;
            Tower.trigger_fire = unit.trigger_fire;
            func(Tower);
            //Tower.tower_ii();
            //Tower.tower_ii();
        }
        for (Unit Tower : unit.TrackUnitList){
            Tower.TargetX = unit.TargetX;
            Tower.TargetY = unit.TargetY;
            Tower.trigger_fire = unit.trigger_fire;
            func(Tower);
            //Tower.tower_ii();
            //Tower.tower_ii();
        }
    }
}
