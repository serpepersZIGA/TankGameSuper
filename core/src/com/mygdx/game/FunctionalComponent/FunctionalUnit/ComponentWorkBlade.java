package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentWorkBlade extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit){
        unit.rotation_tower += unit.speed_tower;
    }
    @Override final
    public void FunctionalIterationClientAnHost(Unit unit){
        unit.rotation_tower += unit.speed_tower;
    }
    @Override final
    public void FunctionalIterationAnClient(Unit unit){
        unit.rotation_tower += unit.speed_tower;
    }
    @Override final
    public void FunctionalIterationOtherAnClient(Unit unit){
        unit.rotation_tower += unit.speed_tower;
    }
}
