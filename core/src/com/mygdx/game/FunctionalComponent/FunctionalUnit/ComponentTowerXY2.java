package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentTowerXY2 extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit){
        unit.TowerXY2();
    }
    @Override final
    public void FunctionalIterationClientAnHost(Unit unit){
        unit.TowerXY2();
    }
    @Override final
    public void FunctionalIterationAnClient(Unit unit){
        unit.TowerXY2();
    }
    @Override final
    public void FunctionalIterationOtherAnClient(Unit unit){
        unit.TowerXY2();
    }
}
