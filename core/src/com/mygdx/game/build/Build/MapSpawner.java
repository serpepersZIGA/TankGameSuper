package com.mygdx.game.build.Build;

import com.mygdx.game.build.BuildType;
import com.mygdx.game.build.Building;
import com.mygdx.game.main.Main;

public class MapSpawner extends Building {
    public MapSpawner(int x, int y,byte team) {
        super(x,y,team);
        //ListFunc
        super.Data();
    }

    @Override final
    public void all_action() {
        super.all_action();
        //super.SpawnUnit();

    }

    @Override final
    public void update() {
    }
}
