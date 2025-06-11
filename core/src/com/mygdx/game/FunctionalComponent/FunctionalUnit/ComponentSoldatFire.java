package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

import java.awt.*;

public class ComponentSoldatFire extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit){
        unit.SoldatControl();
    }
}
