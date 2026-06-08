package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentNotTowerControl  extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit){
        unit.NotTowerControl();
    }
    @Override final
    public void FunctionalIterationClientAnHost(Unit unit){
        unit.NotTowerControl();
    }
}
