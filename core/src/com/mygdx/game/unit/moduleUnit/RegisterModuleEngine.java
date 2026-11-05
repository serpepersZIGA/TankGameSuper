package com.mygdx.game.unit.moduleUnit;


import com.mygdx.game.FunctionalComponent.FunctionalList;

import java.util.ArrayList;

import static com.mygdx.game.main.Main.RegisterFunctionalComponent;

public class RegisterModuleEngine {
    public static ArrayList<Object[]>EngineListID = new ArrayList<>();
    public static Engine Engine1E,Engine2E,Engine3E;
    public static void Create(){
        Engine1E = new Engine("Engine1",4F,-4F,0.2F,1F,true);
        Engine2E = new Engine("Engine2",-4F,0.2F,0.6F,1F,true);
        Engine3E = new Engine("Engine3",-5F,0.2F,2F,1F,true);

    }
}
