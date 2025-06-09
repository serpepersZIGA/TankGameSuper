package com.mygdx.game.unit.PlayerSpawnList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.main.Main;
import com.mygdx.game.menu.button.ButtonTank.ListTankPlayerAdd;
import com.mygdx.game.menu.button.ButtonTank.TankChoice;

import static com.mygdx.game.unit.SpawnPlayer.PlayerSpawnListData.SpawnList;

public class PlayerAllLoad {
    public static void PlayerCount(){
        FileHandle[] files = Gdx.files.internal("PlayerAllSpawnList").list();
        if(files.length== 0){
            System.out.println(files.length);
            ListTankPlayerAdd.AddListTank();
            files = Gdx.files.internal("PlayerAllSpawnList").list();
        }
        int x = 1200;
        int y = 200;
        for (FileHandle file: files) {
            System.out.println(file.readString());
            SpawnList.add(file.name());
            Main.ButtonList.add(new TankChoice(x,y,260,140,file.name()));
            y+= 140;
        }
    }
}
