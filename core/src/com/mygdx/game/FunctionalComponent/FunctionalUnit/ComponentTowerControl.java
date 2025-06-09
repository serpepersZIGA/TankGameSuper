package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentTowerControl extends FunctionalComponent {
    public void FunctionalIterationAnHost(Unit unit){
        unit.TowerControl();
    }
    public void FunctionalIterationClientAnHost(Unit unit){
        unit.TowerControl();

    }
    public void FunctionalIterationAnClient(Unit unit){
    }
    public void FunctionalIterationOtherAnClient(Unit unit){
    }
}
