package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentTowerXY extends FunctionalComponent {
    public void FunctionalIterationAnHost(Unit unit){
        unit.TowerXY2();
    }
    public void FunctionalIterationClientAnHost(Unit unit){
        unit.TowerXY2();
    }
    public void FunctionalIterationAnClient(Unit unit){
        unit.TowerXY2();
    }
    public void FunctionalIterationOtherAnClient(Unit unit){
        unit.TowerXY2();
    }
}
