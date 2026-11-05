package com.mygdx.game.FunctionalComponent.FunctionalBuilding;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.build.Building;

public class ComponentFlameBuild extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Building building) {
        building.flame_build();
    }
    @Override final
    public void FunctionalCreateBuildData(Building building) {
        building.size_light();
    }
}
