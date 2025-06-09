package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.method.Method;
import com.mygdx.game.unit.Unit;

public class ComponentSoldatCorrect extends FunctionalComponent {
    @Override
    public void FunctionalIterationAnHost(Unit unit) {
        unit.tower_x = unit.x;
        unit.tower_y = unit.y;
    }
}
