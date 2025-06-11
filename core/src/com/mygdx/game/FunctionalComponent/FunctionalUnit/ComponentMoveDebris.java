package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentMoveDebris extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit){
        unit.move_debris();
    }

}
