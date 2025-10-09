package com.mygdx.game.unit.Controller;

import com.mygdx.game.main.Main;
import com.mygdx.game.unit.Unit;

public class ControllerBotSupport extends Controller{
    public void ControllerIteration(Unit unit){
        unit.bypass_hiller();
    }
}
