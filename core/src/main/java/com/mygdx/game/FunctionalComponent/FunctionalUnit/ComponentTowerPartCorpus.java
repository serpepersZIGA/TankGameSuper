package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentTowerPartCorpus extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit){
        unit.rotation_tower = unit.rotation_corpus+unit.RotateBase;
    }
    @Override final
    public void FunctionalIterationClientAnHost(Unit unit){
        unit.rotation_tower = unit.rotation_corpus+unit.RotateBase;
    }
}
