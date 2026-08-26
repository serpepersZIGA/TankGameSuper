package com.mygdx.game.menu.button;

import com.mygdx.game.MapFunction.MapScan;

import java.util.ArrayList;

import static com.mygdx.game.main.Main.KeyboardObj;

public class MapLoad extends Button {
    public static ArrayList<Button> MapChoiceList = new ArrayList<>();
    public MapLoad(int x, int y, int width, int height, String path) {
        this.x = x;
        this.y = y;

        this.path = path;
        this.width = width;
        this.height = height;
        //this.txt = txt;
        this.txt = MapScan.MapName(path);
        DataRect();
        YTxTConst = YTXT;
        yConst = this.y;
        XTXT -= 40;

        this.ConfigMenu = 3;
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
            MapScan.MapSize(path);
            MapScan.MapInput(path);
            KeyboardObj.zoom_const();

            condition = false;
        }
    }
}
