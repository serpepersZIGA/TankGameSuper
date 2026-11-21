package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Inventory.InventoryInterface;
import com.mygdx.game.Inventory.ItemObject;
import com.mygdx.game.Network.DebrisPacket;
import com.mygdx.game.Network.PacketUnitUpdate;
import com.mygdx.game.Network.TowerPack;
import com.mygdx.game.Network.TransportPacket;
import com.mygdx.game.Shader.FlameShader;
import com.mygdx.game.Shader.LiquidShader;
import com.mygdx.game.UI.button.Button;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.method.CycleTimeDay;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.object_map.MapObject;
import com.mygdx.game.unit.AI;
import com.mygdx.game.unit.Squad;
import com.mygdx.game.unit.Unit;

import java.util.ArrayList;

import static com.mygdx.game.Inventory.ItemObject.ItemList;
import static com.mygdx.game.Sound.SoundRegister.SoundPack;
import static com.mygdx.game.UI.button.ButtonTank.TankChoice.TankChoiceList;
import static com.mygdx.game.UI.button.MapLoad.MapChoiceList;
import static com.mygdx.game.Weather.WeatherMainSystem.RippleIteration;
import static com.mygdx.game.Weather.WeatherMainSystem.WeatherIteration;
import static com.mygdx.game.build.BuildRegister.PacketBuilding;
import static com.mygdx.game.bull.BulletRegister.PacketBull;
import static com.mygdx.game.main.ActionGameHost.server_packet;
import static com.mygdx.game.main.ActionMenu.*;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.main.ServerMain.Server;
import static com.mygdx.game.method.Option.SoundProcent;
import static com.mygdx.game.unit.TransportRegister.*;
import static com.mygdx.game.unit.Unit.AIScan;

public class ActionMenuHost extends ActionGame{
    protected Thread ThreadIterationDebris;
    protected Thread ThreadIterationBullet;
    protected Thread ThreadIterationUnit;
    private int e = 0;
    private float TimeGlobals = 0f;

    //private static IterationBullet IterationBullet = new IterationBullet();

    private int i;
    @Override final
    public void action() {
        if(Unit.ai_sost != 0){
            Unit.ai_sost-=1;
            Unit.AIScan = false;}
        else {
            Squad.SquadCreate();
            Unit.ai_sost=700;
//            for (ArrayList<Block> i2:Main.BlockList2D){
//                for (Block i3:i2){
//                    i3.render_block = BlockID.get(1);
//                }
//            }
            Unit.AIScan = true;
        }

        //Unit.yu = 0;
        ThreadIterationDebris = new Thread(new ActionGameHost.IterationDebris());
        ThreadIterationBullet = new Thread(new ActionGameHost.IterationBullet());
        ThreadIterationUnit = new Thread(new ActionGameHost.IterationUnit());

        ThreadIterationDebris.start();
        ThreadIterationBullet.start();
        ThreadIterationUnit.start();
        //executor = Executors.newFixedThreadPool(1);
        //ThreadAllAddHost();
        //Future<ArrayList<Bullet>>BulletFutureIteration = executor.submit(BulletThreadIteration);
        //BulletFutureIteration.get();


        //UnitFutureIteration.get();
        //Future<ArrayList<Unit>>DebrisFutureIteration = executor.submit(DebrisThreadIteration);

        //BulletFutureIteration.get();
        //DebrisList = DebrisThreadIteration.Debris;

        //DebrisFutureIteration.get();




        //Gdx.gl.glClearColor(0.2f, 0.4f, 0.8f, 1f); // R, G, B, Alpha
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RC.method();
        //if(RC.UnitCamera==null){
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
        if(Keyboard.ClickEsc){
            ActionGameMain = ActionGameH;
            ConfigMenu = 4;
        }
//            try {
//                if(timer <= 0) {
//                    if (Keyboard.LeftMouse) {
//
//                        Main.FlameSpawnList.add(new FlameSpawn(Keyboard.MouseX / Zoom + RC.x2,Keyboard.MouseY / Zoom + RC.y2));
//                        timer = 30;
//
//

//                    }
//                    if (Keyboard.RightMouse) {
//                        //main.Main.bang_obj.add(new particle.bang(mouse_x,mouse_y,new Color(236,124,38),12));
//                        Main.LiquidList.add(new Acid(Keyboard.MouseX / Zoom + RC.x2,Keyboard.MouseY / Zoom + RC.y2));
//                        //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
//                        //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
//                        //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
//
//                    }
//                }
//                else{timer-= 1;}
//            }
//            catch(Exception ignored){
//
//            }
        //}
        //boolean[]mouse_e = new metod.mouse_control().mouse_event();
        //Main.player_obj.get(1).all_action_client(Main.left_mouse_client, Main.right_mouse_client, Main.mouse_x_client,
        //Main.mouse_y_client, Main.press_w_client, Main.press_a_client, Main.press_s_client, Main.press_d_client);


        if(flame_spawn_time > 0){flame_spawn_time-=1;}
        Batch.begin();
        Render.polyBatch.begin();
        Main.RC.render_block();
        RippleIteration(Batch);

        LiquidShader.AcidShaderIteration();

        //LiquidShader.BloodShaderIteration();


        //liquidGlobal();
//        for (i = 0; i< Main.FlameStaticList.size(); i++){
//            Main.FlameStaticList.get(i).all_action();}
//        for (i = 0; i< Main.FlameList.size(); i++){
//            Main.FlameList.get(i).all_action();}
//        for (i = 0; i< Main.FlameParticleList.size(); i++){
//            Main.FlameParticleList.get(i).all_action();}
        Render.polyBatch.flush();

        FlameShader.FlameShaderIteration();
        Batch.setShader(LightSystem.shader);

        for(int i = 0;i<ItemList.size();i++){
            ItemList.get(i).IterationItem();
        }

        Render.polyBatch.flush();

//        for(i = 0;i< DebrisList.size();i++) {
//            Unit debris = DebrisList.get(i);
//            packet_debris_server(debris,i);
//            debris.all_action();
//            debris.corpus_corpus(Main.UnitList);
//            debris.corpus_corpus(Main.DebrisList);
//        }
//        for(i = 0;i< UnitList.size();i++) {
//            Unit unit = UnitList.get(i);
//            packet_player_server(unit);
//            if (unit.host || unit.control == RegisterControl.controllerBot
//                    || unit.control == RegisterControl.controllerBotSupport
//                    || unit.control == RegisterControl.controllerSoldatTransport
//                    || unit.control == RegisterControl.controllerSoldatBot
//                    || unit.control == RegisterControl.controllerHelicopter) {
//                unit.all_action();
//            } else {
//                unit.all_action_client();
//            }
//        }



        //IterationBullet.start();

//        IterationBullet.start();
        for (Integer ind : Bullet.BulletListInd) {
            Bullet bullet = BulletList.get(ind);
            if(bullet.height == 1) {
                bullet.update();
            }

        }


        for(int i = 0;i< UnitList.size();i++) {
            Unit unit = UnitList.get(i);
            if(unit.height == 1) {
                unit.UpdateUnit();
                unit.update();
                for (Unit tower : unit.TowerUnitList){
                    tower.updateTower();
                }
            }
        }
        for(int i = 0;i< DebrisList.size();i++){
            Unit unit = DebrisList.get(i);
            unit.UpdateUnit();
        }

        RC.BuildingUpdate();
        for (Integer ind : Bullet.BulletListInd) {
            Bullet bullet = BulletList.get(ind);
            if(bullet.height == 2) {
                bullet.update();
            }

        }


        for(i = 0;i< UnitList.size();i++) {
            Unit unit = UnitList.get(i);
            if(unit.height == 2) {
                unit.UpdateUnit();
                unit.update();
                for (Unit tower : unit.TowerUnitList){
                    tower.updateTower();
                }
            }
        }
        shopMain.InventoryIteration();
        equipmentMain.InventoryIteration();
        inventoryMain.InventoryIteration();
        if(InventoryInterface.SlotBuffer != null){
            InventoryInterface.SlotBuffer.SlotXY();
            InventoryInterface.SlotBuffer.SlotRender();
            InventoryInterface.SlotBuffer.SlotPaste();
        }
        if(InventoryInterface.WindowName.conf){
            InventoryInterface.WindowName.RenderWindow();
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
        if(Server == null){
            Render.polyBatch.end();
            Batch.end();
            return;
        }

        switch (ConfigMenu) {
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
            case 4:
                SliderSound.AllAction();
                SoundProcent = SliderSound.PercentageGet() * 0.2f;
                if(Keyboard.ClickEsc){
                    Main.ActionGameMain = ActionGameTotal;
                }
                break;
        }

        Render.polyBatch.end();
        Batch.end();
        WeatherIteration(Batch);
        LightSystem.begin(Batch);
        if(flame_spawn_time <= 0){flame_spawn_time=flame_spawn_time_max;}
        CycleDayNight.WorkTime();
        while (ThreadIterationBullet.isAlive()||ThreadIterationUnit.isAlive()||ThreadIterationDebris.isAlive()){

        }
        ActionGameHost.server_packet();
        Keyboard.LeftMouseClick = false;

    }
}
