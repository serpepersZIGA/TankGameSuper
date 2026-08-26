package com.mygdx.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.main.Main;
import com.mygdx.game.menu.button.Button;
import com.mygdx.game.menu.button.MapLoad;


import static com.mygdx.game.main.ActionMenu.LenghtListMap;
import static com.mygdx.game.menu.button.MapLoad.MapChoiceList;

public class MapAllLoad{
    public static void MapCount(){
        FileHandle[] files = Gdx.files.internal("Map/maps").list();
        int x = 1200;
        int y = 0;
        for (FileHandle file: files) {
//            System.out.println(file.path());
            Button button = new MapLoad(x,y,260,140,file.path());
            Main.ButtonList.add(button);
            MapChoiceList.add(button);
            y+= 140;
        }
        LenghtListMap = files.length*140;
    }
}
