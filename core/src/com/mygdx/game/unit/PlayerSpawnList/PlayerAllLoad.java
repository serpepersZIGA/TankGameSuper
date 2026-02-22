package com.mygdx.game.unit.PlayerSpawnList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.main.Main;
import com.mygdx.game.menu.button.Button;
import com.mygdx.game.menu.button.ButtonTank.ListTankPlayerAdd;
import com.mygdx.game.menu.button.ButtonTank.TankChoice;

import static com.mygdx.game.main.ActionMenu.LenghtListTank;
import static com.mygdx.game.menu.button.ButtonTank.TankChoice.TankChoiceList;
import static com.mygdx.game.unit.SpawnPlayer.PlayerSpawnListData.SpawnList;

public class PlayerAllLoad {
    public static void PlayerCount(){
        FileHandle[] files = Gdx.files.internal("PlayerAllSpawnList").list();
        if(files.length== 0){
            ListTankPlayerAdd.AddListTank();
            files = Gdx.files.internal("PlayerAllSpawnList").list();
        }
        int x = 1200;
        int y = 0;
        for (FileHandle file: files) {
            SpawnList.add(file.name());
            Button button = new TankChoice(x,y,260,140,file.name());
            Main.ButtonList.add(button);
            TankChoiceList.add(button);

            y+= 140;
        }
        LenghtListTank = files.length*140;
    }
}
