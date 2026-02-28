package com.mygdx.game.menu.button;

import com.mygdx.game.main.Main;

import static com.mygdx.game.main.Main.ActionGameTotal;

public class Play2 extends Button{
    public Play2(int x, int y, int width, int height, String txt, byte ConfigMenu){
        this.x = x;this.y = y;
        this.ConfigMenu = ConfigMenu;
        this.width = width;this.height = height;
        this.txt = txt;
        DataRect();

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
            Main.ActionGameMain = ActionGameTotal;

            condition = false;
        }
    }
}
