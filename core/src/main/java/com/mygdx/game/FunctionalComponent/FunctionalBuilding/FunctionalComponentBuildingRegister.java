package com.mygdx.game.FunctionalComponent.FunctionalBuilding;

import com.mygdx.game.FunctionalComponent.FunctionalBullet.ComponentColorFire;
import com.mygdx.game.FunctionalComponent.FunctionalComponent;

public class FunctionalComponentBuildingRegister {
    public static FunctionalComponent FlameBuild;
    public static void FunctionalComponentBuildingRegisters(){
        FlameBuild = new ComponentFlameBuild();
    }
}
