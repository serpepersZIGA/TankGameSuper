package com.mygdx.game.menu.button;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.MapFunction.MapScan;
import com.mygdx.game.Shader.LightingMainSystem;
import com.mygdx.game.block.Block;
import com.mygdx.game.main.ClientMain;
import com.mygdx.game.main.Main;
import com.mygdx.game.main.ServerMain;


import static com.mygdx.game.MapFunction.MapScan.MapSize;
import static com.mygdx.game.bull.Bullet.BulletListDown;
import static com.mygdx.game.bull.Bullet.BulletListUp;
import static com.mygdx.game.main.ActionGame.ActionGameH;
import static com.mygdx.game.main.ActionGame.ActionMenu;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.main.ServerMain.nConnect;
import static com.mygdx.game.method.RenderCenter.IndBuilding;
import static java.awt.SystemColor.window;

public class ExitPlay extends Button{
    public ExitPlay(int x, int y, int width, int height, String txt,byte ConfigMenu){
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
            if(ActionGameTotal == ActionGameH){
                ServerMain.Server.close();
                nConnect = 0;
                ServerMain.Server = null;
            }
            else{
                ClientMain.Client.close();
                ClientMain.Client = null;
            }
            ActionGameMain = ActionMenu;
            ActionGameTotal = null;
            Main.ConfigMenu = 0;
            IndBuilding.clear();
            Block.passability_detected2();
            UnitList.clear();
            BuildingList.clear();
            BulletList.clear();
            BulletListUp.clear();
            BulletListDown.clear();
            LightSystem.lights.clear();
            LightSystem.lightsRender.clear();
            BlockList2D.clear();
            MapSize("Map/maps/MapBase.mapt");
            MapScan.MapInput("Map/maps/MapBase.mapt");
            condition = false;
        }
    }
}

