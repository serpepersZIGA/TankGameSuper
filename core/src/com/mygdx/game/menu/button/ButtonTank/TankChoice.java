package com.mygdx.game.menu.button.ButtonTank;

import com.mygdx.game.Inventory.Inventory;
import com.mygdx.game.Inventory.Item;
import com.mygdx.game.main.Main;
import com.mygdx.game.menu.button.Button;
import com.mygdx.game.unit.Unit;

import java.util.Objects;

import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.unit.SpawnPlayer.PlayerSpawnListData.*;
import static com.mygdx.game.unit.Unit.IDList;

public class TankChoice extends Button {
    public TankChoice(int x, int y, int width, int height, String TankName) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        //this.txt = txt;
        DataRect();
        this.txt = TankName;
        this.TankName = TankName;
        this.ConfigMenu = 1;
        XTXT -= 40;
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
            SpawnIDPlayer = TankName;
//            for (Object[] obj : IDList) {
//                if (Objects.equals(TankName, obj[1])) {
//                    Unit unit = (Unit) obj[0];
//                    unit.UnitAdd(200, 200, true, (byte) 1, RegisterControl.controllerPlayer
//                            , new Inventory(new Item[4][4]));
//                }
//            }

        }
        KeyboardObj.zoom_const();

        condition = false;
    }
}


