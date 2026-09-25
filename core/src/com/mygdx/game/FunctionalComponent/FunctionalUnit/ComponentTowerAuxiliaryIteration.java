package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

public class ComponentTowerAuxiliaryIteration extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit){
        unit.TargetConstX = unit.x+unit.corpus_width_2;
        unit.TargetConstY = unit.y+unit.corpus_height_2;
        //unit.tower_iteration(unit);
        for (Unit Tower : unit.TowerUnitList){
            Tower.x = unit.x;
            Tower.y = unit.y;
            //System.out.println(Tower.difference+"  "+Tower.difference_2);
            Tower.TargetConstX = unit.TargetConstX;
            Tower.TargetConstY = unit.TargetConstY;
            Tower.rotation_corpus = unit.rotation_corpus;
            //Tower.tower_action();
            //Tower.InertionTowerRotate(unit);
            Tower.functional.FunctionalIterationAnHost(Tower);
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
            Tower.TargetConstX = (float) (unit.x+unit.corpus_width_2+Tower.difference_2*sin(-unit.rotation_corpus));
            Tower.TargetConstY = (float) (unit.y+unit.corpus_height_2+Tower.difference*cos(-unit.rotation_corpus));
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
