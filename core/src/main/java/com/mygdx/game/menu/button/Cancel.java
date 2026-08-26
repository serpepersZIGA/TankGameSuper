package com.mygdx.game.menu.button;

import com.mygdx.game.main.Main;

public class Cancel extends Button{
    public Cancel(int x, int y, int width, int height, String txt,byte ConfigMenu){
        this.x = x;this.y = y;
        this.ConfigMenu = ConfigMenu;
        this.width = width;this.height = height;
        this.txt = txt;
        DataRect();
        XTXT -=40;

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
            Main.ConfigMenu = 0;

            condition = false;
        }
    }
}
