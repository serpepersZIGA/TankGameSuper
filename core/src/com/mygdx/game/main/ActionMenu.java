package com.mygdx.game.main;

import Content.Particle.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Inventory.Inventory;
import com.mygdx.game.Inventory.Item;
import com.mygdx.game.Inventory.ItemRegister;
import com.mygdx.game.Network.PackerServer;
import com.mygdx.game.Network.Packet_client;
import com.mygdx.game.Shader.FlameShader;
import com.mygdx.game.Shader.LiquidShader;
import com.mygdx.game.block.Block;
import com.mygdx.game.menu.button.Button;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.Weather.WeatherMainSystem.*;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.unit.Unit.IDList;

public class ActionMenu extends ActionGame {
    private int i;
    private int timer = 0;
    @Override final
    public void action() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Main.RC.method();
        if(Keyboard.PressW){
            Main.RC.y += 10;
        }
        if(Keyboard.PressS){
            Main.RC.y -= 10;
        }
        if(Keyboard.PressA){
            Main.RC.x -= 10;
        }
        if(Keyboard.PressD){
            Main.RC.x += 10;
        }
            if(timer <= 0) {

                if (Keyboard.LeftMouse) {
                    //Main.FlameSpawnList.add(new FlameSpawn((float) (Keyboard.MouseX / Zoom + RC.x2), (float) (Keyboard.MouseY / Zoom + RC.y2)));
                    BloodList.add(new Blood((float) (Keyboard.MouseX / Zoom + RC.x2), (float) (Keyboard.MouseY / Zoom + RC.y2)));
                    //timer = 60;


                }
                if (Keyboard.RightMouse) {
                    //main.Main.bang_obj.add(new particle.bang(mouse_x,mouse_y,new Color(236,124,38),12));
                    Main.LiquidList.add(new Acid((float) (Keyboard.MouseX / Zoom + RC.x2), (float) (Keyboard.MouseY / Zoom + RC.y2)));

                    //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
                    //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));

                }
            }
            else{timer-= 1;}

        if(flame_spawn_time > 0){flame_spawn_time-=1;}
        Batch.begin();

        Render.polyBatch.begin();
        Main.RC.render_block();
        RippleIteration(Batch);

        LiquidShader.AcidShaderIteration();
        for (i= 0; i< Main.LiquidList.size(); i++){
            Main.LiquidList.get(i).all_action();}
        Batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        LiquidShader.BloodShaderIteration();
        for (i= 0; i< BloodList.size(); i++){
            Main.BloodList.get(i).all_action();}
        Batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //LightSystem.begin(Batch);
        //Batch.setShader(LightSystem.shader);
//        for (Particle part : LiquidList){
//            part.update();
//        }
        //Batch.setShader(LightSystem.shader);
        //liquidGlobal();
        //liquidGlobal();


        for (i = 0; i< Main.FlameStaticList.size(); i++){
            Main.FlameStaticList.get(i).all_action();}
        for (i = 0; i< Main.FlameList.size(); i++){
            Main.FlameList.get(i).all_action();}
        for (i = 0; i< Main.FlameParticleList.size(); i++){
            Main.FlameParticleList.get(i).all_action();}
        Render.polyBatch.flush();
//        Render.end();
//        Render.begin();

        FlameShader.FlameShaderIteration();
        for (i= 0; i< Main.FlameSpawnList.size(); i++){
            Main.FlameSpawnList.get(i).all_action();
        }

        Batch.setShader(LightSystem.shader);
        for (i = 0; i< Main.BulletList.size(); i++){
            if(Main.BulletList.get(i).height == 1) {
                Main.BulletList.get(i).update();
            }
        }
        Render.polyBatch.flush();

        for(i = 0; i< Main.UnitList.size(); i++) {
            Main.UnitList.get(i).UpdateUnit();
            Main.UnitList.get(i).all_action_client_2();
        }


        for (i= 0; i< Main.DebrisList.size(); i++){
            Main.DebrisList.get(i).all_action_client();
        }
        for(Unit unit : UnitList) {
            unit.all_action_client();
        }
        RC.BuildingIteration();
        //Batch.draw(TextureAtl.createSprite("BottleFlame"),20,20,10,10);
//        Render.end();
//        Render.begin();

        for (i = 0; i< Main.BulletList.size(); i++){
            if(Main.BulletList.get(i).height == 2) {
                Main.BulletList.get(i).update();
            }
        }
        for (i= 0; i< Main.UnitList.size(); i++){
            Main.UnitList.get(i).update();
        }
        for (i= 0; i< Main.BangList.size(); i++){
            Main.BangList.get(i).all_action();}

        for (i = 0;i< ButtonList.size();i++){
            Button but = ButtonList.get(i);
            if(Main.ConfigMenu == but.ConfigMenu) {
                if (but.TypeFont) {
                    but.TXTRender2();
                }
                else{
                    but.TXTRender();
                }
                but.render(i);
            }
        }
//        for (i = 0;i< ButtonList.size();i++){
//            Button but = ButtonList.get(i);
//            if(Main.ConfigMenu == but.ConfigMenu) {
//                if (!but.TypeFont) {
//                    but.TXTRender();
//                }
//                but.render(i);
//            }
//        }
//        Batch.draw(WeatherMainSystem.fbo.getColorBufferTexture(), 0, 0, fbo.getWidth(), fbo.getHeight());
//        Batch.draw(LightingMainSystem.fbo.getColorBufferTexture(), 0, 0, fbo.getWidth(), fbo.getHeight());

        if(flame_spawn_time <= 0){flame_spawn_time=flame_spawn_time_max;}

        Render.polyBatch.end();


        Batch.end();
        //Batch.setShader(null);
        //RainRippleShader.begin(Batch);
        WeatherIteration(Batch);
        LightSystem.begin(Batch);
        if(GameStart) {
            PacketServer = new PackerServer();
            PacketClient = new Packet_client();
            if (GameHost) {
                try {
                    serverMain = new ServerMain();
                    serverMain.create();
                    ActionGame = ActionGameH;
                    Block.passability_detected();
                    SpawnPlayer();
                    KeyboardObj.zoom_const();


                } catch (Exception e) {
                    //throw new RuntimeException(e);
                }
            } else {
                try {
                    LightSystem.lights.clear();
                    Main_client = new ClientMain();
                    Main_client.create();
                    ActionGame = ActionGameCl;
                    ActionGameClient.ActionGameClientIteration();
                    KeyboardObj.zoom_const();
                    //return;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Keyboard.LeftMouseClick = false;
        CycleDayNight.WorkTime();
        //Batch.setShader(null);
    }
    public void SpawnPlayer(){
    Unit unit = IDList.get(SpawnIDPlayer);
    unit.UnitAdd(200,200,true,(byte)1,
            RegisterControl.controllerPlayer,new Inventory(new Item[4][4]));
    UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.MedicineT1);
    UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.MedicineT1);
    UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.MedicineT1);
    UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.AK74);
//    unit.inventory.ItemAdd(ItemRegister.MedicineT1);
//    unit.inventory.ItemAdd(ItemRegister.MedicineT1);
//    unit.inventory.ItemAdd(ItemRegister.MedicineT1);



    }





}
