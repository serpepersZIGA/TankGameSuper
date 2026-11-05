package com.mygdx.game.UI.button.ButoonTeam;

import com.mygdx.game.UI.button.Button;
import com.mygdx.game.main.Main;

import static com.mygdx.game.UI.button.ButtonTank.TankChoice.TankChoiceList;
import static com.mygdx.game.main.ActionMenu.LenghtListTeam;

public class CommandAllLoad {
    public static void PlayerCount(){
        int x = 900;
        int y = 0;
        for (int i = 0;i<Main.TeamGlobal.size();i++) {
            Button button = new TeamChoice(x,y,120,85,(byte)i);
            Main.ButtonList.add(button);
            TankChoiceList.add(button);

            y+= 85;
        }
        LenghtListTeam = Main.TeamGlobal.size()*140;
    }
}
