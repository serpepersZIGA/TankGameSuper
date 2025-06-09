package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentFireControl extends FunctionalComponent {
    @Override
    public void FunctionalIterationAnHost(Unit unit) {
        unit.FireControl();
    }
    @Override
    public void FunctionalIterationClientAnHost(Unit unit) {
        unit.FireControl();
    }
}
