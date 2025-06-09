package com.mygdx.game.menu.button;

import com.mygdx.game.MapFunction.MapScan;

import static com.mygdx.game.main.Main.KeyboardObj;

public class MapLoad extends Button {
    public MapLoad(int x, int y, int width, int height, String path) {
        this.x = x;
        this.y = y;

        this.path = path;
        this.width = width;
        this.height = height;
        //this.txt = txt;
        DataRect();
        this.txt = MapScan.MapName(path);
        this.ConfigMenu = 3;
        XTXT -= 40;
        TypeFont = true;

    }

    @Override
    public void render(int i) {
        super.render(i);
        XYDetectedButtonRect();
        ActionButton1();
        ActionButton();
        RenderButtonRect();
    }

    protected void ActionButton() {
        if (condition) {
            MapScan.MapInput(path);
            KeyboardObj.zoom_const();

            condition = false;
        }
    }
}
