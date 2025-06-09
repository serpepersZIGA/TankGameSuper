package com.mygdx.game.unit.moduleUnit;


import com.mygdx.game.FunctionalComponent.FunctionalList;

import java.util.ArrayList;

import static com.mygdx.game.main.Main.RegisterFunctionalComponent;

public class RegisterModuleEngine {
    public static Engine Engine1E,Engine2E,Engine3E;
    public static void Create(){
        FunctionalList functional = new FunctionalList();
        functional.Add(RegisterFunctionalComponent.MotorControl);
        Engine1E = new Engine(4F,-4F,0.2F,1F,functional);
        Engine2E = new Engine(6F,-4F,0.2F,0.6F,functional);
        Engine3E = new Engine(16F,-5F,0.2F,2F,functional);

    }
}
