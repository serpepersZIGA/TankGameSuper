package com.mygdx.game.unit.Controller;

import com.mygdx.game.unit.Unit;

public class ControllerSoldatTransport extends Controller{
    @Override
    public void ControllerIteration(Unit unit) {
        unit.behavior_bot();
    }
}
