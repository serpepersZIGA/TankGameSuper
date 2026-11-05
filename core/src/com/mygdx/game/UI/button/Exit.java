package com.mygdx.game.UI.button;

import com.badlogic.gdx.Gdx;

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
