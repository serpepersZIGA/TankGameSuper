package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentTowerAuxiliaryIteration extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit){
        //unit.tower_iteration(unit);
        for (Unit Tower : unit.TowerUnitList){
            //Tower.tower_action();
            //Tower.InertionTowerRotate(unit);
            Tower.functional.FunctionalIterationAnHost(Tower);
            Tower.x = unit.x;
            Tower.y = unit.y;
            Tower.TargetConstX = Tower.x+unit.corpus_width_2;
            Tower.TargetConstY = Tower.y+unit.corpus_height_2;
            Tower.rotation_corpus = unit.rotation_corpus;
        }
    }
    @Override final
    public void FunctionalIterationClientAnHost(Unit unit){
        for (Unit Tower : unit.TowerUnitList){
            //Tower.tower_action();
            //Tower.InertionTowerRotate(unit);
            Tower.functional.FunctionalIterationClientAnHost(Tower);
            Tower.x = unit.x;
            Tower.y = unit.y;
            Tower.TargetConstX = Tower.x+Tower.corpus_width_2;
            Tower.TargetConstY = Tower.y+Tower.corpus_height_2;
            Tower.rotation_corpus = unit.rotation_corpus;
        }

    }
    @Override final
    public void FunctionalIterationAnClient(Unit unit){
        unit.TowerIterationClient(unit);
    }
    @Override final
    public void FunctionalIterationOtherAnClient(Unit unit){
        unit.TowerIterationClient(unit);
    }
}
