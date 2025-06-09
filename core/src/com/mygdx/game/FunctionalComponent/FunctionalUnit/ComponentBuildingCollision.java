package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentBuildingCollision extends FunctionalComponent {
    public void FunctionalIterationAnHost(Unit unit){
        unit.build_corpus();
    }
    public void FunctionalIterationClientAnHost(Unit unit){
        unit.build_corpus();
    }
}
