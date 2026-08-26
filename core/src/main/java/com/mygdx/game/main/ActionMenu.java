package com.mygdx.game.main;

import Content.Particle.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Inventory.Equipment.EquipmentInterface;
import com.mygdx.game.Inventory.Inventory;
import com.mygdx.game.Inventory.InventoryInterface;
import com.mygdx.game.Inventory.Item;
import com.mygdx.game.Inventory.ItemRegister;
import com.mygdx.game.Inventory.Shop.ShopInterface;
import com.mygdx.game.Network.PackerServer;
import com.mygdx.game.Network.Packet_client;
import com.mygdx.game.Shader.FlameShader;
import com.mygdx.game.Shader.LiquidShader;
import com.mygdx.game.block.Block;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.menu.button.Button;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.Inventory.Item.IDListItem;
import static com.mygdx.game.Weather.WeatherMainSystem.*;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.unit.Unit.IDList;

public class ActionMenu extends ActionGame {
    private int i;
    private int timer = 0;
    public static boolean ActionGameChoiceConf;
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
        LiquidShader.BloodShaderIteration();

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
        if(ActionGameTotal != ActionGameH) {
            for (i = 0; i < Main.BulletList.size(); i++) {
                Bullet bullet = BulletList.get(i);
                if(bullet != null) {
                    if (bullet.height == 1) {
                        Main.BulletList.get(i).update();
                    }
                }
            }
        }

        for(i = 0; i< Main.UnitList.size(); i++) {
            Main.UnitList.get(i).UpdateUnitMenu();
            //Main.UnitList.get(i).all_action_client_2();
        }


        for (i= 0; i< Main.DebrisList.size(); i++){
            Main.DebrisList.get(i).UpdateUnit();
        }

        for (i = 0;i< BulletList.size();i++){
            Bullet bullet = BulletList.get(i);
            if(bullet != null){
                if(bullet.height == 1) {
                    BulletList.get(i).update();
                }
            }

        }
        Render.polyBatch.flush();

//        for(Unit unit : UnitList) {
//            unit.all_action_client();
//        }


        RC.BuildingUpdate();


        //Batch.draw(TextureAtl.createSprite("BottleFlame"),20,20,10,10);
//        Render.end();
//        Render.begin();
        for (i = 0;i< BulletList.size();i++){
            Bullet bullet = BulletList.get(i);
            if(bullet != null) {
                if (bullet.height == 2) {
                    BulletList.get(i).update();
                }
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
        // ConfigMenu 4 is the in-game pause overlay (Play2/ExitPlay); the old
        // pre-game menu screens (main menu/tank select/map select) have been
        // replaced by the Scene2D screens in com.mygdx.game.ui and no longer
        // run through this switch.
        if (ConfigMenu == 4 && Keyboard.ClickEsc) {
            Main.ActionGameMain = ActionGameTotal;
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
            GameStart = false;
            PacketServer = new PackerServer();
            PacketClient = new Packet_client();
            boolean started;
            if (GameHost) {
                try {
                    serverMain = new ServerMain();
                    serverMain.create();
                    ActionGameMain = ActionGameH;
                    ActionGameTotal = ActionGameH;
                    Block.passability_detected();
                    SpawnPlayer();
                    KeyboardObj.zoom_const();
                    started = true;

                } catch (Exception e) {
                    Gdx.app.error("ActionMenu", "Failed to start hosting a game, returning to menu.", e);
                    com.mygdx.game.ui.NetworkStatusBanner.INSTANCE.show(com.mygdx.game.ui.Localization.INSTANCE.tr("network.hostfailed"));
                    started = false;
                }
            } else {
                try {
                    LightSystem.lights.clear();
                    Main_client = new ClientMain();
                    Main_client.create();
                    ActionGameMain = ActionGameCl;
                    ActionGameTotal = ActionGameCl;
                    ActionGameClient.ActionGameClientIteration();
                    KeyboardObj.zoom_const();
                    started = true;
                } catch (Exception e) {
                    Gdx.app.error("ActionMenu", "Failed to connect to the server, returning to menu.", e);
                    com.mygdx.game.ui.NetworkStatusBanner.INSTANCE.show(com.mygdx.game.ui.Localization.INSTANCE.tr("network.joinfailed"));
                    started = false;
                }
            }
            // Only advance into the game once the host/client setup actually
            // succeeded - otherwise ActionGameMain may still be null/stale and
            // the game should stay on the menu instead of crashing.
            if (started) {
                ActionGameChoiceConf = true;
                ActionGameMain.ThreadAllAdd();
            } else {
                // Send the player back to a screen they can actually interact
                // with - otherwise they'd be stuck staring at the world with
                // no menu and no input processor pointed at any UI.
                ActionGameMain = com.mygdx.game.ui.HostJoinScreen.INSTANCE;
                com.mygdx.game.ui.HostJoinScreen.INSTANCE.show();
            }
        }
        com.mygdx.game.ui.NetworkStatusBanner.INSTANCE.render();
        Keyboard.LeftMouseClick = false;
        CycleDayNight.WorkTime();
        //Batch.setShader(null);
    }
    public void SpawnPlayer(){

        Inventory inventory = new Inventory(new Item[4][4],1);
        Inventory equipment = new Inventory(new Item[4][2],1);
        Unit unit = IDList.get(SpawnIDPlayer);
        unit = unit.UnitAdd(200,200,true,(byte)1,
                RegisterControl.controllerPlayer,inventory,equipment);
        unit.inventory.ItemAdd(ItemRegister.MedicineT1);
        unit.inventory.ItemAdd(ItemRegister.MedicineT1);
        unit.inventory.ItemAdd(ItemRegister.MedicineT1);
        unit.inventory.ItemAdd(ItemRegister.AK74);
        unit.inventory.ItemAdd(IDListItem.get("armorB1"));
        RC.MainUnit = unit;
        equipmentMain = new EquipmentInterface(equipment);
        inventoryMain = new InventoryInterface(inventory);

//    IDList.get("Helicopter-2Z").UnitAdd(200,200,true,(byte)2,
//            RegisterControl.controllerHelicopter,new Inventory(new Item[4][4]));
//    unit.inventory.ItemAdd(ItemRegister.MedicineT1);
//    unit.inventory.ItemAdd(ItemRegister.MedicineT1);
//    unit.inventory.ItemAdd(ItemRegister.MedicineT1);



    }





}
