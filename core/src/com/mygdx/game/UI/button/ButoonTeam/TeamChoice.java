package com.mygdx.game.UI.button.ButoonTeam;

import com.mygdx.game.UI.button.Button;
import com.mygdx.game.unit.Unit;

import java.util.ArrayList;

import static com.mygdx.game.main.Main.KeyboardObj;
import static com.mygdx.game.main.Main.SpawnIDPlayer;

public class TeamChoice extends Button {
    public static ArrayList<Button> TeamChoiceList = new ArrayList<>();
    public TeamChoice(int x, int y, int width, int height,byte Team) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.Team = Team;
        this.txt = String.valueOf(Team);
        DataRect();
        this.ConfigMenu = 1;
        YTxTConst = YTXT;
        yConst = this.y;
        //XTXT -= 40;
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
            Unit.TeamPlayer = Team;

        }
        //KeyboardObj.zoom_const();

        condition = false;
    }
}
