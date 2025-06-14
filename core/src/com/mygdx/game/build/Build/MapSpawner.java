package com.mygdx.game.build.Build;

import com.mygdx.game.build.BuildType;
import com.mygdx.game.build.Building;
import com.mygdx.game.main.Main;

public class MapSpawner extends Building {
    public MapSpawner(int x, int y,byte team) {
        super(x,y,team);
        this.x = x;
        this.y = y;
        this.team = team;
        this.SpawnMaxTime = 600;
        RenderBuilding = Main.BuildingRegister.Update_big_build_wood1;
        ConstructBuilding = new boolean[][]{{false}};

        super.Data();


    }

    @Override final
    public void all_action() {
        super.all_action();
        super.SpawnUnit();

    }

    @Override
    final
    public void update() {
        RenderBuilding.render(this.x, this.y, this.width_render, this.height_render);
    }
}
