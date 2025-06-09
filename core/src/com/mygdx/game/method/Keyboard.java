package com.mygdx.game.method;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.Inventory.ItemObject;
import com.mygdx.game.Shader.LightingMainSystem;
import com.mygdx.game.block.Block;
import com.mygdx.game.build.Building;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.main.Main;
import com.mygdx.game.menu.button.Button;
import com.mygdx.game.particle.Particle;
import com.mygdx.game.unit.Unit;

import java.util.ArrayList;

import static com.mygdx.game.block.Block.lighting;
import static com.mygdx.game.block.Block.lighting_zoom;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.Inventory.InventoryInterface.InventoryConf;
import static com.mygdx.game.Inventory.InventoryInterface.InventoryConfMoving;

public class Keyboard extends InputAdapter{
    public static boolean PressW,PressA,PressS,PressD,PressE,PressUP,PressDown,PressF;
    public static boolean LeftMouse, RightMouse,LeftMouseClick, RightMouseClick,MiddleMouse;
    public static int MouseX,MouseY;
    private static float ZoomMax,ZoomMin;
    public static void ZoomMaxMin(){
        ZoomMax = 2;
        ZoomMin = (float) (0.4);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.W) {
            PressW = true;
        }
        if (keycode ==Input.Keys.S ) {
            PressS = true;
        }
        if (keycode ==Input.Keys.A) {
            PressA = true;
        }
        if (keycode ==Input.Keys.D) {
            PressD = true;
        }
        if (keycode ==Input.Keys.F) {
            PressF = true;
        }
        if (keycode ==Input.Keys.E) {
            PressE = true;
        }
        if (keycode == Input.Keys.UP) {
            Button.YList += 4;
            PressUP = true;
        }
        if (keycode == Input.Keys.DOWN) {
            Button.YList -= 4;
            PressDown = true;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode ==Input.Keys.W) {
            PressW = false;
        }
        if (keycode ==Input.Keys.S) {
            PressS = false;
        }
        if (keycode ==Input.Keys.A) {
            PressA = false;
        }
        if (keycode ==Input.Keys.D) {
            PressD = false;
        }
        if (keycode ==Input.Keys.F) {
            PressF = false;
        }
        if (keycode ==Input.Keys.E) {
            PressE = false;
            InventoryConf = !InventoryConf;
        }
        if (keycode == Input.Keys.UP) {
            PressUP = false;
        }
        if (keycode == Input.Keys.DOWN) {
            PressDown = false;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT) {
            LeftMouse = true;
        }
        if(button == Input.Buttons.MIDDLE) {
            if(InventoryConf) {
                inventoryMain.CollisionMouseItem();
            }
            MiddleMouse = true;
        }
        if(button == Input.Buttons.RIGHT) {
            if(InventoryConf & inventoryMain.CollisionMouseInvert()){
                InventoryConfMoving = true;
            }
           RightMouse = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT) {
            LeftMouse = false;
            LeftMouseClick = true;
        }
        if(button == Input.Buttons.RIGHT) {
            if(InventoryConfMoving){
                InventoryConfMoving = false;
            }
            RightMouse = false;
            RightMouseClick = true;
        }
        if(button == Input.Buttons.MIDDLE) {
            MiddleMouse = false;
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        MouseX = screenX;
        MouseY = (screenY-Main.screenHeight)*-1;
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        MouseX = screenX;
        MouseY = (screenY-Main.screenHeight)*-1;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        MouseX = screenX;
        MouseY = (screenY-Main.screenHeight)*-1;
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if(amountY > 0) {
            if (Main.Zoom >ZoomMin) {
                Main.Zoom -= 0.1;
                zoom_const();
            }
        } else {
            if (Main.Zoom < ZoomMax) {
                Main.Zoom += 0.1;
                zoom_const();
            }
        }

        return false;
    }
    public void zoom_const(){
        for(Particle particle : Main.FlameParticleList){
            particle.size_render = (int)(particle.size* Main.Zoom);
        }
        ItemObject.widthRender = (int)(ItemObject.width* Main.Zoom);
        ItemObject.heightRender = (int)(ItemObject.height* Main.Zoom);

        RC.WidthRenderZoom = RC.WidthRender /Main.Zoom;
        RC.HeightRenderZoom = RC.HeightRender /Main.Zoom;
        RC.WidthRenderZoom2 = RC.WidthRenderZoom /2;
        RC.HeightRenderZoom2 = RC.HeightRenderZoom /2;
        RC.width_2_zoom = RC.width_2/Main.Zoom;
        RC.height_2_zoom = RC.height_2/Main.Zoom;
        RC.CameraMapConf();
        RC.cam_x_width = (int) (RC.WidthRenderZoom/Main.width_block);
        RC.cam_y_height= (int) (RC.HeightRenderZoom/Main.height_block);

        for(LightingMainSystem.Light light : LightSystem.lights){
            light.radiusZoom = light.radius*Main.Zoom;
        }

        Main.width_block_zoom = (int) (Main.width_block_render * Main.Zoom);
        Main.height_block_zoom = (int) (Main.height_block_render * Main.Zoom);
        radius_air_max_zoom = radius_air_max*Main.Zoom;
        lighting_zoom = lighting*Main.Zoom;
        Block.lighting_zoom_2 = lighting_zoom / 2;
        for (ArrayList<Block> blocks : BlockList2D) {
            for (Block block : blocks) {
                if (block.objMap != null) {
                    block.objMap.width_render = (int) (block.objMap.width * Main.Zoom);
                    block.objMap.height_render = (int) (block.objMap.height * Main.Zoom);
                }
            }
        }
//        for (ArrayList<Block> air1 : AirList){
//            for(Block air : air1){
//                if(air.radius!= 0) {
//                    air.rad = air.radius / lighting_zoom;
//                }
//            }
//        }
        for(Building building : Main.BuildingList){
            building.width_render = (int)(building.width* Main.Zoom);
            building.height_render = (int)(building.height* Main.Zoom);
        }
        for(Particle particle : Main.FlameList){
           particle.size_render = (int)(particle.size* Main.Zoom);
        }
        for(Particle particle : Main.FlameStaticList){
            particle.size_render = (int)(particle.size* Main.Zoom);
        }
        for(Particle particle : Main.LiquidList){
            particle.size_render = (int)(particle.size* Main.Zoom);
        }
        for(Particle particle : Main.BangList){
            particle.size_render = (int)(particle.size* Main.Zoom);
        }
        for(Particle particle : Main.FlameParticleList){
            particle.size_render = (int)(particle.size* Main.Zoom);
        }
        for(Particle particle : Main.BangList){
            particle.size_render = (int)(particle.size* Main.Zoom);
        }

        for(Bullet bull : Main.BulletList){
            if(bull != null) {
                bull.size_render = (int) (bull.size * Main.Zoom);
            }
        }
        for(Unit tr : Main.UnitList){
            tr.corpus_width_zoom = (int)(tr.corpus_width*Main.Zoom);
            tr.corpus_height_zoom = (int)(tr.corpus_height*Main.Zoom);
            tr.width_tower_zoom = (int)(tr.width_tower *Main.Zoom);
            tr.height_tower_zoom = (int)(tr.height_tower *Main.Zoom);
            tr.const_x_corpus = (int)(tr.corpus_width_2*Main.Zoom);
            tr.const_y_corpus = (int)(tr.corpus_height_2*Main.Zoom);
            tr.const_x_tower = (int)(tr.const_tower_x*Main.Zoom);
            tr.const_y_tower = (int)(tr.const_tower_y*Main.Zoom);
            for (Unit tower : tr.tower_obj){
                tower.width_tower_zoom = (int)(tower.width_tower *Main.Zoom);
                tower.height_tower_zoom = (int)(tower.height_tower *Main.Zoom);
                tower.const_x_tower = (int)(tower.const_tower_x*Main.Zoom);
                tower.const_y_tower = (int)(tower.const_tower_y*Main.Zoom);
            }

        }
        for(Unit tr : Main.UnitList){
            tr.corpus_width_zoom = (int)(tr.corpus_width*Main.Zoom);
            tr.corpus_height_zoom = (int)(tr.corpus_height*Main.Zoom);
            tr.width_tower_zoom = (int)(tr.width_tower *Main.Zoom);
            tr.height_tower_zoom = (int)(tr.height_tower *Main.Zoom);
            tr.const_x_corpus = (int)(tr.corpus_width_2*Main.Zoom);
            tr.const_y_corpus = (int)(tr.corpus_height_2*Main.Zoom);
            tr.const_x_tower = (int)(tr.const_tower_x*Main.Zoom);
            tr.const_y_tower = (int)(tr.const_tower_y*Main.Zoom);
            for (Unit tower : tr.tower_obj){
                tower.width_tower_zoom = (int)(tower.width_tower *Main.Zoom);
                tower.height_tower_zoom = (int)(tower.height_tower *Main.Zoom);
                tower.const_x_tower = (int)(tower.const_tower_x*Main.Zoom);
                tower.const_y_tower = (int)(tower.const_tower_y*Main.Zoom);
            }

        }
        for(Unit tr : Main.DebrisList){
            tr.corpus_width_zoom = (int)(tr.corpus_width*Main.Zoom);
            tr.corpus_height_zoom = (int)(tr.corpus_height*Main.Zoom);
            tr.const_x_corpus = (int)(tr.corpus_width_2*Main.Zoom);
            tr.const_y_corpus = (int)(tr.corpus_height_2*Main.Zoom);

        }
        Main.Option.size_y_indicator_zoom = (int) (Main.Option.size_y_indicator* Main.Zoom);
        Main.Option.size_x_indicator_zoom = (int) (Main.Option.size_x_indicator* Main.Zoom);

        Main.Option.const_hp_x_zoom = (int)(Main.Option.const_hp_x* Main.Zoom);
        Main.Option.const_hp_y_zoom= (int)(Main.Option.const_hp_y* Main.Zoom);
        Main.Option.const_reload_x_zoom = (int)(Main.Option.const_reload_x* Main.Zoom);
        Main.Option.const_reload_y_zoom = (int)(Main.Option.const_reload_y* Main.Zoom);
    }
    public void ZoomConstTransport(){
        for(Unit tr : Main.UnitList){
            tr.corpus_width_zoom = (int)(tr.corpus_width*Main.Zoom);
            tr.corpus_height_zoom = (int)(tr.corpus_height*Main.Zoom);
            tr.width_tower_zoom = (int)(tr.width_tower *Main.Zoom);
            tr.height_tower_zoom = (int)(tr.height_tower *Main.Zoom);
            tr.const_x_corpus = (int)(tr.corpus_width_2*Main.Zoom);
            tr.const_y_corpus = (int)(tr.corpus_height_2*Main.Zoom);
            tr.const_x_tower = (int)(tr.const_tower_x*Main.Zoom);
            tr.const_y_tower = (int)(tr.const_tower_y*Main.Zoom);
            for (Unit tower : tr.tower_obj){
                tower.width_tower_zoom = (int)(tower.width_tower *Main.Zoom);
                tower.height_tower_zoom = (int)(tower.height_tower *Main.Zoom);
                tower.const_x_tower = (int)(tower.const_tower_x*Main.Zoom);
                tower.const_y_tower = (int)(tower.const_tower_y*Main.Zoom);
            }


        }
        for(Unit tr : Main.DebrisList){
            tr.corpus_width_zoom = (int)(tr.corpus_width*Main.Zoom);
            tr.corpus_height_zoom = (int)(tr.corpus_height*Main.Zoom);
            tr.const_x_corpus = (int)(tr.corpus_width_2*Main.Zoom);
            tr.const_y_corpus = (int)(tr.corpus_height_2*Main.Zoom);

        }
    }
}