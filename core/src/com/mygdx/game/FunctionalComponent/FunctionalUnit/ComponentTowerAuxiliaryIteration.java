package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentTowerAuxiliaryIteration extends FunctionalComponent {
    private static int TowerX,TowerY;
    @Override final
    public void FunctionalIterationAnHost(Unit unit){
        unit.TargetConstX = unit.x+unit.corpus_width_2;
        unit.TargetConstY = unit.y+unit.corpus_height_2;
        //unit.tower_iteration(unit);
        for (Unit Tower : unit.TowerUnitList){
           // Tower.x = unit.x;
            //Tower.y = unit.y;
            //System.out.println(Tower.difference+"  "+Tower.difference_2);
            Tower.TargetConstX = unit.TargetConstX;
            Tower.TargetConstY = unit.TargetConstY;
            //Tower.rotation_corpus = unit.rotation_corpus;
            //Tower.tower_action();
            //Tower.InertionTowerRotate(unit);
            Tower.functional.FunctionalIterationAnHost(Tower);
            func(Tower);

        }
    }
    @Override final
    public void FunctionalIterationClientAnHost(Unit unit){
        unit.TargetConstX = unit.x+unit.corpus_width_2;
        unit.TargetConstY = unit.y+unit.corpus_height_2;
        //unit.tower_iteration(unit);
        for (Unit Tower : unit.TowerUnitList){
            //Tower.x = unit.x;
            //Tower.y = unit.y;
            //System.out.println(Tower.difference+"  "+Tower.difference_2);
            Tower.TargetConstX = unit.TargetConstX;
            Tower.TargetConstY = unit.TargetConstY;
            //Tower.rotation_corpus = unit.rotation_corpus;
            //Tower.tower_action();
            //Tower.InertionTowerRotate(unit);
            Tower.functional.FunctionalIterationClientAnHost(Tower);
            func(Tower);

        }

    }
    @Override final
    public void FunctionalIterationAnClient(Unit unit){
        TowerIterationClient(unit);
    }
    @Override final
    public void FunctionalIterationOtherAnClient(Unit unit){
        TowerIterationClient(unit);
    }

    private void func(Unit unit){
        TowerX = (int) (unit.tower_x+unit.tower_width_2);
        TowerY = (int) (unit.tower_y+unit.tower_height_2);
        //unit.TargetConstX = unit.x+unit.corpus_width_2;
        //unit.TargetConstY = unit.y+unit.corpus_height_2;
        for (Unit Tower : unit.TrackUnitList){
            //Tower.x = unit.tower_x;
            //Tower.y = unit.tower_y;
            //System.out.println(Tower.difference+"  "+Tower.difference_2);
            Tower.TargetConstX = TowerX;
            Tower.TargetConstY = TowerY;
            //Tower.rotation_corpus = unit.rotation_tower;
            //Tower.tower_action();
            //Tower.InertionTowerRotate(unit);
            //Tower.tower_x_const = TowerX;
            //Tower.tower_y_const = TowerY;
            Tower.functional.FunctionalIterationAnHost(Tower);
            func(Tower);
        }
        for (Unit Tower : unit.TowerUnitList){
            //Tower.x = unit.tower_x;
            //Tower.y = unit.tower_y;
            //System.out.println(Tower.difference+"  "+Tower.difference_2);
            Tower.TargetConstX = TowerX;
            Tower.TargetConstY = TowerY;
            //Tower.rotation_corpus = unit.rotation_tower;
            //Tower.tower_action();
            //Tower.InertionTowerRotate(unit);
            //Tower.x = TowerX;
            //Tower.y = TowerY;
            Tower.functional.FunctionalIterationAnHost(Tower);
            func(Tower);
        }
    }
    private void func2(Unit unit){
        TowerX = (int) (unit.tower_x+unit.tower_width_2);
        TowerY = (int) (unit.tower_y+unit.tower_height_2);
        for (Unit Tower : unit.TrackUnitList){
            //Tower.x = unit.tower_x;
            //Tower.y = unit.tower_y;
            //System.out.println(Tower.difference+"  "+Tower.difference_2);
            Tower.TargetConstX = TowerX;
            Tower.TargetConstY = TowerY;
            //Tower.rotation_corpus = unit.rotation_tower;
            //Tower.tower_action();
            //Tower.InertionTowerRotate(unit);
            Tower.functional.FunctionalIterationClientAnHost(Tower);
            func2(Tower);
        }
        for (Unit Tower : unit.TowerUnitList){
            //Tower.x = unit.tower_x;
            //Tower.y = unit.tower_y;
            //System.out.println(Tower.difference+"  "+Tower.difference_2);
            Tower.TargetConstX = TowerX;
            Tower.TargetConstY = TowerY;
            //Tower.rotation_corpus = unit.rotation_tower;
            //Tower.tower_action();
            //Tower.InertionTowerRotate(unit);
            Tower.functional.FunctionalIterationClientAnHost(Tower);
            func2(Tower);
        }
    }


    public final void TowerIterationClient(Unit unit){
        //unit.TargetConstX = unit.x+unit.corpus_width_2;
        //unit.TargetConstY = unit.y+unit.corpus_height_2;
        for (Unit Tower : unit.TowerUnitList){
            //Tower.tower_action();
            Tower.x = unit.x;
            Tower.y = unit.y;
            //Tower.TargetConstX = unit.TargetConstX;
            //Tower.TargetConstY = unit.TargetConstY;
            Tower.rotation_corpus = unit.rotation_corpus;
            funcTowerIteration(Tower);
        }
    }
    private void funcTowerIteration(Unit unit){
        //TowerX = (int) (unit.tower_x+unit.tower_width_2);
        //TowerY = (int) (unit.tower_y+unit.tower_height_2);
        for (Unit Tower : unit.TrackUnitList){
            //Tower.tower_action();
            Tower.x = unit.tower_x;
            Tower.y = unit.tower_y;
            //Tower.TargetConstX = TowerX;
            //Tower.TargetConstY = TowerY;
            Tower.rotation_corpus = unit.rotation_tower;
            funcTowerIteration(Tower);
        }
        for (Unit Tower : unit.TowerUnitList){
            //Tower.tower_action();
            Tower.x = unit.tower_x;
            Tower.y = unit.tower_y;
            //Tower.TargetConstX = TowerX;
            //Tower.TargetConstY = TowerY;
            Tower.rotation_corpus = unit.rotation_tower;
            funcTowerIteration(Tower);
        }

    }
}
