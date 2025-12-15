package com.mygdx.game.unit.Controller;

import com.mygdx.game.unit.Unit;

public class ControllerHelicopterBot extends Controller{
    public void ControllerIteration(Unit unit){
        unit.BotHelicopter();
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
