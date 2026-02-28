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
import com.mygdx.game.menu.slider.Slider;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.Inventory.Item.IDListItem;
import static com.mygdx.game.Weather.WeatherMainSystem.*;
import static com.mygdx.game.bull.Bullet.BulletListDown;
import static com.mygdx.game.bull.Bullet.BulletListUp;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.menu.button.ButtonTank.TankChoice.TankChoiceList;
import static com.mygdx.game.menu.button.MapLoad.MapChoiceList;
import static com.mygdx.game.method.Option.SoundProcent;
import static com.mygdx.game.unit.Unit.IDList;

public class ActionMenu extends ActionGame {
    private int i;
    private int timer = 0;
    private static final Slider SliderSound = new Slider(1200,500,300,35,(byte) 0,"Sound",true);
    private static final Slider SliderListTank = new Slider(1460,0,35,980,(byte) 1,"",false);
    private static final Slider SliderListMap = new Slider(1460,0,35,980,(byte) 3,"",false);
    public static int LenghtListTank,LenghtListMap;
    public static int TotalPointListTank,TotalPointListMap;
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
        if(ActionGameTotal != ActionGameH) {
            for (i = 0; i < Main.BulletList.size(); i++) {
                Main.BulletList.get(i).all_action();
            }
        }

        for(i = 0; i< Main.UnitList.size(); i++) {
            Main.UnitList.get(i).UpdateUnit();
            //Main.UnitList.get(i).all_action_client_2();
        }


        for (i= 0; i< Main.DebrisList.size(); i++){
            Main.DebrisList.get(i).all_action_client();
        }

        for (i = 0;i< BulletListDown.size();i++){
            BulletListDown.get(i).update();

        }
        Render.polyBatch.flush();

//        for(Unit unit : UnitList) {
//            unit.all_action_client();
//        }


        RC.BuildingUpdate();


        //Batch.draw(TextureAtl.createSprite("BottleFlame"),20,20,10,10);
//        Render.end();
//        Render.begin();
        for (i = 0;i< BulletListUp.size();i++){
            BulletListUp.get(i).update();

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
        switch (ConfigMenu) {
            case 4:
            case 0:{
                SliderSound.AllAction();
                SoundProcent = SliderSound.PercentageGet() * 0.2f;
            }
            break;
            case 1:{
                SliderListTank.AllAction2();
                TotalPointListTank = (int) (LenghtListTank * SliderListTank.PercentageGet() * 0.5F);
                for (Button button : TankChoiceList) {
                    button.YTXT = button.YTxTConst - TotalPointListTank;
                    button.y = button.yConst - TotalPointListTank;
                    button.heightXY = button.yConst + button.height - TotalPointListTank;
                }
            }break;
            case 3:{
                SliderListMap.AllAction2();
                TotalPointListMap = (int) (LenghtListMap * SliderListMap.PercentageGet() * 0.5F);
                for (Button button : MapChoiceList) {
                    button.YTXT = button.YTxTConst - TotalPointListMap;
                    button.y = button.yConst - TotalPointListMap;
                    button.heightXY = button.yConst + button.height - TotalPointListMap;
                }
            }break;
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
            if (GameHost) {
                try {
                    serverMain = new ServerMain();
                    serverMain.create();
                    ActionGameMain = ActionGameH;
                    ActionGameTotal = ActionGameH;
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
                    ActionGameMain = ActionGameCl;
                    ActionGameTotal = ActionGameCl;
                    ActionGameClient.ActionGameClientIteration();
                    KeyboardObj.zoom_const();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            ActionGameChoiceConf = true;
            ActionGameMain.ThreadAllAdd();
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
    UnitList.get(UnitList.size()-1).inventory.ItemAdd(IDListItem.get("armorB1"));

//    IDList.get("Helicopter-2Z").UnitAdd(200,200,true,(byte)2,
//            RegisterControl.controllerHelicopter,new Inventory(new Item[4][4]));
//    unit.inventory.ItemAdd(ItemRegister.MedicineT1);
//    unit.inventory.ItemAdd(ItemRegister.MedicineT1);
//    unit.inventory.ItemAdd(ItemRegister.MedicineT1);



    }





}
