package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

public class ComponentMotorControl extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit){
        unit.MotorControl();
    }
    @Override final
    public void FunctionalIterationClientAnHost(Unit unit){
        unit.MotorControl();
    }
    @Override final
    public void FunctionalIterationAnClient(Unit unit){
        unit.move_xy_transport();
    }
    @Override final
    public void FunctionalIterationOtherAnClient(Unit unit){
        unit.move_xy_transport();
    }
}
