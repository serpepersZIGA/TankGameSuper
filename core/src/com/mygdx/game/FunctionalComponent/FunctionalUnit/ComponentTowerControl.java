package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentTowerControl extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit){
        unit.TowerControl();
    }
    @Override final
    public void FunctionalIterationClientAnHost(Unit unit){
        unit.TowerControl();

    }
    @Override final
    public void FunctionalIterationAnClient(Unit unit){
    }
    @Override final
    public void FunctionalIterationOtherAnClient(Unit unit){
    }
}
