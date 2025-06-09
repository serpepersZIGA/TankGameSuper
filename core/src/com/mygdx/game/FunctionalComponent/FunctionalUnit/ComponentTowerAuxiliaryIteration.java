package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentTowerAuxiliaryIteration extends FunctionalComponent {
    public void FunctionalIterationAnHost(Unit unit){
        //unit.tower_iteration(unit);
        for (Unit Tower : unit.tower_obj){
            //Tower.tower_action();
            Tower.functional.FunctionalIterationAnHost(Tower);
            Tower.x = unit.x;
            Tower.y = unit.y;
            Tower.rotation_corpus = unit.rotation_corpus;
        }
    }
    public void FunctionalIterationClientAnHost(Unit unit){
        for (Unit Tower : unit.tower_obj){
            //Tower.tower_action();
            Tower.functional.FunctionalIterationClientAnHost(Tower);
            Tower.x = unit.x;
            Tower.y = unit.y;
            Tower.rotation_corpus = unit.rotation_corpus;
        }

    }
    public void FunctionalIterationAnClient(Unit unit){
        unit.tower_iteration(unit);
    }
    public void FunctionalIterationOtherAnClient(Unit unit){
        unit.tower_iteration(unit);
    }
}
