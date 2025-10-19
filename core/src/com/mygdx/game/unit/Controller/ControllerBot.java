package com.mygdx.game.unit.Controller;

import com.mygdx.game.unit.Unit;

public class ControllerBot extends Controller{
    public void ControllerIteration(Unit unit){
        unit.behavior_bot();
        unit.TowerXY2();
        unit.TowerAI();
        //unit.bot_fire();
        for (Unit Tower : unit.tower_obj){
            Tower.TargetX = unit.TargetX;
            Tower.TargetY = unit.TargetY;
            Tower.trigger_fire = unit.trigger_fire;
            //Tower.tower_ii();
            //Tower.tower_ii();
        }
    }
}
