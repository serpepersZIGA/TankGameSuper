package com.mygdx.game.menu.button;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.main.Main;

import static java.awt.SystemColor.window;

public class Exit extends Button{
    public Exit(int x, int y, int width, int height, String txt,byte ConfigMenu){
        this.x = x;this.y = y;
        this.ConfigMenu = ConfigMenu;
        this.width = width;this.height = height;
        this.txt = txt;
        DataRect();
        //XTXT -=40;

    }
    @Override
    public void render(int i) {
        super.render(i);
        XYDetectedButtonRect();
        ActionButton1();
        ActionButton();
        RenderButtonRect();
    }
    protected void ActionButton(){
        if(condition) {
            Gdx.app.exit();
        }
    }
}
