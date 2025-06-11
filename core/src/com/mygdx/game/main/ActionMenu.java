package com.mygdx.game.main;

import Content.Particle.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Inventory.Inventory;
import com.mygdx.game.Inventory.InventoryInterface;
import com.mygdx.game.Inventory.Item;
import com.mygdx.game.Inventory.ItemRegister;
import com.mygdx.game.block.Block;
import com.mygdx.game.menu.button.Button;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.unit.Unit;

import java.util.Objects;

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
        try {
            if(timer <= 0) {

                if (Keyboard.LeftMouse) {
                    Main.FlameSpawnList.add(new FlameSpawn((float) (Keyboard.MouseX / Zoom + RC.x2), (float) (Keyboard.MouseY / Zoom + RC.y2)));
                    timer = 60;


                }
                if (Keyboard.RightMouse) {
                    //main.Main.bang_obj.add(new particle.bang(mouse_x,mouse_y,new Color(236,124,38),12));
                    Main.LiquidList.add(new Acid((float) (Keyboard.MouseX / Zoom + RC.x2), (float) (Keyboard.MouseY / Zoom + RC.y2)));
                    //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
                    //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
                    //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));

                }
            }
            else{timer-= 1;}
        }
        catch(Exception ignored){

        }
        if(flame_spawn_time > 0){flame_spawn_time-=1;}
        Batch.begin();
        LightSystem.begin(Batch);

        Render.begin();
        Main.RC.render_block();
        Batch.end();
        for (i= 0; i< Main.LiquidList.size(); i++){
            Main.LiquidList.get(i).all_action(i);}
        for (i = 0; i< Main.FlameStaticList.size(); i++){
            Main.FlameStaticList.get(i).all_action(i);}
        for (i = 0; i< Main.FlameList.size(); i++){
            Main.FlameList.get(i).all_action(i);}
        for (i = 0; i< Main.FlameParticleList.size(); i++){
            Main.FlameParticleList.get(i).all_action(i);}
        for (i = 0; i< Main.BulletList.size(); i++){
            if(Main.BulletList.get(i).height == 1) {
                Main.BulletList.get(i).update();
            }
        }
        Render.end();
        Batch.begin();
        Render.begin();
        for (i= 0; i< Main.FlameSpawnList.size(); i++){
            Main.FlameSpawnList.get(i).all_action(i);
        }
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
        Render.end();
        Render.begin();

        for (i = 0; i< Main.BulletList.size(); i++){
            if(Main.BulletList.get(i).height == 2) {
                Main.BulletList.get(i).update();
            }
        }
        for (i= 0; i< Main.UnitList.size(); i++){
            Main.UnitList.get(i).update();
        }
        for (i= 0; i< UnitList.size(); i++){
            UnitList.get(i).update();
        }
//        for (i= 0; i< AirList.size(); i++){
//            for(int i2= 0; i2< AirList.get(i).size(); i2++) {
//                AirList.get(i).get(i2).all_action();
//            }
//        }
        //lighting.begin(Batch, normalMap);
        //LightSystem.end(Batch);
        for (i= 0; i< Main.BangList.size(); i++){
            Main.BangList.get(i).all_action(i);}


        Batch.end();
        Batch.begin();

        for (i = 0;i< ButtonList.size();i++){
            Button but = ButtonList.get(i);
            if(Main.ConfigMenu == but.ConfigMenu) {
                but.render(i);
                if (but.TypeFont) {
                    but.TXTRender2();
                } else {
                    but.TXTRender();
                }
            }
        }

        if(flame_spawn_time <= 0){flame_spawn_time=flame_spawn_time_max;}
        Render.end();
        Batch.end();
        if(GameStart) {
            PacketServer = new PackerServer();
            PacketClient = new Packet_client();
            if (GameHost) {
                try {
                    serverMain = new ServerMain();
                    serverMain.create();
                    ActionGame = new ActionGameHost();
                    Block.passability_detected();
                    SpawnPlayer();
                    KeyboardObj.zoom_const();


                } catch (Exception e) {
                    //throw new RuntimeException(e);
                }
            } else {
                try {
                    Main_client = new ClientMain();
                    Main_client.create();
                    ActionGame = new ActionGameClient();
                    KeyboardObj.zoom_const();
                    //return;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Keyboard.LeftMouseClick = false;
        CycleDayNight.WorkTime();
    }
    public void SpawnPlayer(){
        for (Object[]obj : IDList){
            if(Objects.equals(obj[1], SpawnIDPlayer)){
                Unit unit = (Unit) obj[0];
                unit.UnitAdd(200,200,true,(byte)1,
                        RegisterControl.controllerPlayer,new Inventory(new Item[4][4]));
                UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.MedicineT1);
                UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.MedicineT1);
                UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.MedicineT1);
                UnitList.get(UnitList.size()-1).inventory.ItemAdd(ItemRegister.AK74);
//                unit.inventory.ItemAdd(ItemRegister.MedicineT1);
//                unit.inventory.ItemAdd(ItemRegister.MedicineT1);
//                unit.inventory.ItemAdd(ItemRegister.MedicineT1);

            }
        }
    }





}
