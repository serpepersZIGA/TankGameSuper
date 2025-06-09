package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.main.Main;
import com.mygdx.game.unit.Unit;

public class ComponentHill extends FunctionalComponent {
    public void FunctionalIterationAnHost(Unit unit){
        unit.hill_bot(Main.UnitList);
    }
}
