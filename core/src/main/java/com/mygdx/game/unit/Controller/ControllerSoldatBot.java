package com.mygdx.game.unit.Controller;

import com.mygdx.game.unit.Unit;

import static com.mygdx.game.method.Method.detection_near_transport_i;

public class ControllerSoldatBot extends Controller{
    public void ControllerIteration(Unit unit){
        unit.tower_x = unit.x;
        unit.tower_y = unit.y;
        unit.rotation_tower = unit.rotation_corpus;
        //System.out.println(unit.tower_x);
        Unit obj = detection_near_transport_i(unit);
        if(obj != null) {
            unit.AISoldatPath(obj);
            unit.BotSoldatFire();
        }
        else{
            unit.trigger_fire = false;
        }
        //unit.TowerControlBot();
        //unit.SoldatMoveBot();
        //unit.SoldatRotateBot();
    }
//    public void ControllerIterationClientAnClient(Unit unit){
//        unit.tower_x = unit.x;
//        unit.tower_y = unit.y;
//        unit.AiSoldat();
//        unit.bot_fire();
//        //unit.TowerControlBot();
//        //unit.SoldatMoveBot();
//        //unit.SoldatRotateBot();
//    }
//    public void ControllerIterationClientAnHost(Unit unit){
//        unit.tower_x = unit.x;
//        unit.tower_y = unit.y;
//        unit.AiSoldat();
//        unit.bot_fire();
//        //unit.TowerControlBot();
//        //unit.SoldatMoveBot();
//        //unit.SoldatRotateBot();
//    }

}
