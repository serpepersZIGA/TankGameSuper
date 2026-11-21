package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Inventory.InventoryInterface;
import com.mygdx.game.Shader.FlameShader;
import com.mygdx.game.Shader.LiquidShader;
import com.mygdx.game.UI.button.Button;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.particle.Particle;
import com.mygdx.game.unit.SpawnPlayer.SpawnPlayerPack;
import com.mygdx.game.unit.Unit;

import java.util.concurrent.ExecutionException;

import static com.mygdx.game.Inventory.ItemObject.ItemList;
import static com.mygdx.game.Sound.SoundPlay.soundPlayClient;
import static com.mygdx.game.UI.button.ButtonTank.TankChoice.TankChoiceList;
import static com.mygdx.game.UI.button.MapLoad.MapChoiceList;
import static com.mygdx.game.Weather.WeatherMainSystem.RippleIteration;
import static com.mygdx.game.Weather.WeatherMainSystem.WeatherIteration;
import static com.mygdx.game.main.ActionGameClient.ConfBulletAdd;
import static com.mygdx.game.main.ActionMenu.*;
import static com.mygdx.game.main.ClientMain.Client;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.main.Main.Batch;
import static com.mygdx.game.main.ServerMain.Server;
import static com.mygdx.game.method.Option.SoundProcent;

public class ActionMenuClient extends ActionGame{
    private Thread ThreadIterationDebris;
    private Thread ThreadIterationBullet;
    private Thread ThreadIterationUnit;

    public static void ActionGameClientIteration(){
        SpawnPlayerPack pack = new SpawnPlayerPack();
        //inventoryMain = new InventoryInterface(new Inventory(new Item[4][4]),200,200,500,400);
        pack.ID = SpawnIDPlayer;
        pack.Team = Unit.TeamPlayer;
        Client.sendTCP(pack);
    }
    int i;
    private static int timer = 0;
    @Override final
    public void action() {
        ThreadIterationBullet = new Thread(new ActionGameClient.IterationBullet());
        ThreadIterationDebris = new Thread(new ActionGameClient.IterationDebris());
        ThreadIterationUnit = new Thread(new ActionGameClient.IterationUnit());
        ThreadIterationBullet.start();
        ThreadIterationDebris.start();
        ThreadIterationUnit.start();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RC.method();
        //if(RC.UnitCamera==null){
        if(Keyboard.PressW){
            RC.y += 10;
        }
        if(Keyboard.PressS){
            RC.y -= 10;
        }
        if(Keyboard.PressA){
            RC.x -= 10;
        }
        if(Keyboard.PressD){
            RC.x += 10;}
        if(Keyboard.ClickEsc){
            ActionGameMain = ActionGameCl;
            ConfigMenu = 4;
        }
//        try {
//                if(timer <= 0) {
//
//                    if (Keyboard.LeftMouse) {
//                        FlameSpawnList.add(new FlameSpawn(Keyboard.MouseX / Zoom + RC.x2,Keyboard.MouseY / Zoom + RC.y2));
//                        timer = 60;
//
//
//                    }
//                    if (Keyboard.RightMouse) {
//                        //main.Main.bang_obj.add(new particle.bang(mouse_x,mouse_y,new Color(236,124,38),12));
//                        LiquidList.add(new Acid(Keyboard.MouseX / Zoom + RC.x2,Keyboard.MouseY / Zoom + RC.y2));
//                        //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
//                        //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
//                        //main.Main.liquid_obj.add(new particle.acid(mouse_x/1.23,mouse_y/1.23));
//
//                    }
//                }
//                else{timer-= 1;}
//            }
//            catch(Exception ignored){

        //}

        //}



        Batch.begin();
        Render.polyBatch.begin();
        RC.render_block();
        RippleIteration(Batch);

        if(flame_spawn_time > 0){flame_spawn_time-=1;}
        LiquidShader.AcidShaderIteration();

        //LiquidShader.BloodShaderIteration();

//        for (i = 0; i< FlameStaticList.size(); i++){
//            FlameStaticList.get(i).all_action();}
//        for (i = 0; i< FlameList.size(); i++){
//            FlameList.get(i).all_action();}
//        for (i = 0; i< FlameParticleList.size(); i++){
//            FlameParticleList.get(i).all_action();}

        Render.polyBatch.flush();

        FlameShader.FlameShaderIteration();
        Batch.setShader(LightSystem.shader);
        //Batch.flush();
        for(int i = 0;i<ItemList.size();i++){
            ItemList.get(i).IterationItemClient();
        }
        Render.polyBatch.flush();

        for (Integer ind : Bullet.BulletListInd) {
            Bullet bullet = BulletList.get(ind);
            if(bullet.height == 1) {
                bullet.update();
            }

        }
        for(i = 0;i< UnitList.size();i++) {
            Unit unit = UnitList.get(i);
            if(unit.height == 1) {
                unit.UpdateUnit();
                unit.update();
                for (Unit tower : unit.TowerUnitList){
                    tower.updateTower();
                }
            }
        }
        for(i = 0;i< DebrisList.size();i++) {
            Unit unit = DebrisList.get(i);
            unit.UpdateUnit();

        }

        soundPlayClient();



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
        shopMain.InventoryIterationClient();
        equipmentMain.InventoryIterationClient();
        inventoryMain.InventoryIterationClient();


        if(InventoryInterface.SlotBuffer != null){
            InventoryInterface.SlotBuffer.SlotXY();
            InventoryInterface.SlotBuffer.SlotRender();
            InventoryInterface.SlotBuffer.SlotPasteClient();
        }
        if(InventoryInterface.WindowName.conf){
            InventoryInterface.WindowName.RenderWindow();
        }

        for (i= 0; i< BangList.size(); i++){
            BangList.get(i).all_action();
        }
        if(flame_spawn_time < 0){flame_spawn_time=flame_spawn_time_max;}
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
        if(Client == null){
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
        //PackUpdateUnit();
        while (ThreadIterationBullet.isAlive()||ThreadIterationUnit.isAlive()||ThreadIterationDebris.isAlive()){

        }
        for(Bullet bullet : Bullet.BulletListClear){
            bullet.clearClient();
            //Bullet.BulletListClear.remove(bullet);
        }
        for (int i2 = 0;i2<BulletAddList.size();i2++){
            Bullet bullet = BulletAddList.get(i2);
            ConfBulletAdd = false;
            for(int i = 0;i<BulletList.size();i++) {
                //Bullet bull = BulletList.get(i);
                if(BulletList.get(i) == null) {
                    bullet.ind = i;
                    Bullet.BulletListInd.add(bullet.ind);
                    BulletList.set(i,bullet);
                    ConfBulletAdd = true;
                    break;
                }
            }
            if(!ConfBulletAdd) {
                bullet.ind = BulletList.size();
                Bullet.BulletListInd.add(bullet.ind);
                BulletList.add(bullet);
            }
        }
        Bullet.BulletListClear.clear();
        BulletAddList.clear();
        for (Particle particle : Particle.ClearLiquidList){
            LiquidList.remove(particle);
        }
        Particle.ClearLiquidList.clear();
        Keyboard.LeftMouseClick = false;

    }
}
