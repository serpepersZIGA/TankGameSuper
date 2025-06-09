package com.mygdx.game.unit;
import Content.Particle.Blood;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Event.EventGame;
import com.mygdx.game.Inventory.*;
import com.mygdx.game.Sound.SoundPacket;
import com.mygdx.game.Sound.SoundPlay;
import com.mygdx.game.bull.BullPacket;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.*;
import Content.Particle.FlameSpawn;
import Content.Particle.Bang;
import com.mygdx.game.unit.CollisionUnit.TypeCollision;
import com.mygdx.game.unit.Controller.Controller;
import com.mygdx.game.unit.Fire.Fire;
import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.unit.moduleUnit.Cannon;
import com.mygdx.game.unit.moduleUnit.Corpus;
import com.mygdx.game.unit.moduleUnit.Engine;
import com.mygdx.game.unit.moduleUnit.Soldat;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Objects;

import static com.mygdx.game.Sound.SoundRegister.SoundPack;
import static com.mygdx.game.bull.BulletRegister.PacketBull;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.Method.*;
import static com.mygdx.game.method.Option.SoundConst;
import static com.mygdx.game.method.pow2.pow2;
import static com.mygdx.game.unit.ClassUnit.SupportTransport;
import static com.mygdx.game.unit.Fire.FireRegister.FireVoid;
import static com.mygdx.game.unit.TransportRegister.*;
import static java.lang.StrictMath.*;

public abstract class Unit implements Cloneable{
    public static ArrayList<Object[]> IDList = new ArrayList<>();
    public Item GunUse = null;
    public String ID;
    public TypeCollision collision = TypeCollision.rect;
    public UnitType type_unit;
    public int[][]TowerXY;
    public int SizeBullet,TimeBullet,TimeBulletRand;
    public float SpeedBullet,SpeedBulletRand;
    public static boolean AIScan;
    public ClassUnit classUnit = ClassUnit.Transport;
    public Inventory inventory;
    public float x, y;
    public Corpus CorpusUnit;
    public Engine EngineUnit;
    public ArrayList<Cannon> CannonUnitList = new ArrayList<>();
    public Cannon CannonUnit;
    public int  difference,difference_2,hp,max_hp,time_spawn_soldat,time_spawn_soldat_max,x_rend,y_rend,x_tower_rend,y_tower_rend, id_unit, x_tower,y_tower,
    time_max_sound_motor = 20,time_sound_motor = time_max_sound_motor,nConnect;
    public Sound sound_fire;
    public float fire_x;
    public float fire_y;
    public float max_speed=4, min_speed=-4,damage,penetration,damage_fragment,penetration_fragment,t,t_damage,armor,reload_max,acceleration=0.2f,speed, SpeedInert, RotationInert, rotation_tower, speed_tower=0.2f, speed_rotation=0.2f
            , rotation_corpus,tower_x,tower_y
            , tower_x_const, tower_y_const, tower_width_2, tower_height_2,reload,corpus_width,corpus_height,corpus_width_2,corpus_height_2,
            hill, width_tower, height_tower, TargetX, TargetY,corpus_height_3,corpus_width_3;
    protected float slowing = 0.05f;
    public static float speed_minimum = 0.5f;
    public int time_max_relocation = 300,time_relocation = 0;
    public float x_relocation,y_relocation,rotation_relocation,priority_paint = 0,ai_x_const = 24f,ai_y_const = 62f;
    public int range_see=2000,range_see_2 = (int)(range_see*1.5),time_trigger_bull_bot,time_trigger_bull = 700;
    public boolean PlayerConf;
    public float AngleTarget,RadiusTarget;

    public byte behavior,behavior_buffer, medic_help, team,height = 1,trigger_drive;
    private float g;
    public FunctionalList functional = new FunctionalList();
    public static int BorderDetected = 200;
    public boolean host,crite_life;
    public Soldat soldat;

    private int i;
    protected int distance_target = 200;
    public float SpeedCollision = 10;
    protected int distance_target_2 = 230;
    public float difference_x,difference_y,green_len,green_len_reload;
    public float rotation_fire;
    public int corpus_width_zoom, corpus_height_zoom,width_tower_zoom,height_tower_zoom,AmountFragment;
    public static int ai_sost = 200;
    public EventGame EventClear = EventData.eventDeadTransport;
    public ArrayList<int[]>path;
    public ArrayList<Unit> tower_obj;
    public int const_x_corpus,const_y_corpus,const_x_tower,const_y_tower,const_tower_x = 7,const_tower_y = 10;
    public boolean sost_fire_bot,guidance,left_mouse,right_mouse,trigger_attack,trigger_fire;
    public boolean press_w;
    public boolean press_a;
    public boolean press_s;
    public boolean press_d,press_f;
    public Sprite tower_img,corpus_img;
    public Fire fire = FireVoid;
    public Controller control = RegisterControl.controllerVoid;
    public int HPTriggerHill;
    public Unit(){
    }
    public Unit(float x, float y,boolean host, byte team){

    }
    public Unit(float x, float y, float rotation, float speed, float inert_rotation,
                float inert_speed, Sprite corpus, float width, float height, UnitType type){
        this.x = x;
        this.y = y;
        this.rotation_corpus = rotation;
        this.speed = speed;
        this.RotationInert = inert_rotation;
        this.SpeedInert = inert_speed;
        this.corpus_img = corpus;
        this.corpus_width = width;
        this.corpus_height = height;
        this.type_unit = type;

    }
    public Unit(Corpus corpus, Engine engine, ArrayList<Cannon> cannon, int[][]TowerXY,ClassUnit classUnit
    ,int medic_help){

        this.medic_help = (byte) medic_help;
        this.CorpusUnit = corpus.CorpusAdd();
        this.EngineUnit = engine.EngineAdd();
        this.CannonUnitList = cannon;
        this.TowerXY = TowerXY;
        this.classUnit = classUnit;
//        for(int i = 0;i<cannon.size();i++){
//            tower_obj.add(new UnitPattern(cannon.get(i).CannonAdd(this,TowerXY[i][0],TowerXY[i][1]),this));
//        }
    }
    public Unit(Soldat soldat){
        this.soldat = soldat.SoldatAdd();
        this.behavior = (byte) (2+ rand.rand(1));
//        collision = TypeCollision.circle;
        this.EventClear = EventData.eventDeadSoldat;
        this.speed_rotation = 1.2F;
        soldat.SoldatLoad(this);
    }
    public Unit(Corpus corpus,float x,float y,float rotation,float speed,float SpeedInert,float RotationInert){
        this.x = x;
        this.y = y;
        this.rotation_corpus = rotation;
        this.speed = speed;
        this.SpeedInert = SpeedInert;
        this.RotationInert = RotationInert;
        this.CorpusUnit = corpus.CorpusAdd();


    }
    public void UnitDelete(){
        for(Object[] ID : IDList){
            if(Objects.equals(ID[1], this.ID)){
                Unit unit = (Unit) ID[0];
                DebrisList.add(new UnitPattern(unit.CorpusUnit,this.ID,x,y,rotation_corpus,speed,SpeedInert,RotationInert));
                DebrisList.get(DebrisList.size()-1).EventClear = EventData.eventDeadDebris;
            }
        }

    }
    public Unit(Cannon cannon,Unit unit){
        classUnit = ClassUnit.Tower;
        this.CannonUnit = cannon;
    }
    public void UnitAdd(int x, int y, boolean host, byte team, Controller controller,Inventory inventory){
        try {
            Unit unitAdd = (Unit) this.clone();
            unitAdd.x = x;
            unitAdd.inventory =inventory.clone();
            unitAdd.y = y;
            unitAdd.host = host;
            unitAdd.team = team;
            unitAdd.control = controller;
            unitAdd.tower_obj = new ArrayList<>();
            unitAdd.medic_help = this.medic_help;
            unitAdd.classUnit = this.classUnit;
            unitAdd.EventClear = this.EventClear;//EventData.eventDeadSoldat;



            for(int i = 0;i<CannonUnitList.size();i++){
                unitAdd.tower_obj.add(new UnitPattern(CannonUnitList.get(i).
                        CannonAdd(unitAdd,unitAdd.TowerXY[i][0],unitAdd.TowerXY[i][1]),unitAdd));
            }

            unitAdd.corpus_width_zoom = (int)(unitAdd.corpus_width*Main.Zoom);
            unitAdd.corpus_height_zoom = (int)(unitAdd.corpus_height*Main.Zoom);
            unitAdd.width_tower_zoom = (int)(unitAdd.width_tower *Main.Zoom);
            unitAdd.height_tower_zoom = (int)(unitAdd.height_tower *Main.Zoom);
            unitAdd.const_x_corpus = (int)(unitAdd.corpus_width_2*Main.Zoom);
            unitAdd.const_y_corpus = (int)(unitAdd.corpus_height_2*Main.Zoom);
            unitAdd.const_x_tower = (int)(unitAdd.const_tower_x*Main.Zoom);
            this.const_y_tower = (int)(unitAdd.const_tower_y*Main.Zoom);
            for (Unit tower : unitAdd.tower_obj){
                tower.width_tower_zoom = (int)(tower.width_tower *Main.Zoom);
                tower.height_tower_zoom = (int)(tower.height_tower *Main.Zoom);
                tower.const_x_tower = (int)(tower.const_tower_x*Main.Zoom);
                tower.const_y_tower = (int)(tower.const_tower_y*Main.Zoom);
            }

            for(Unit cannons : unitAdd.tower_obj){
                cannons.team = team;
            }
//            for(Unit cannons : tower_obj){
//                unitAdd.tower_obj.add((Unit) cannons.clone());
//                //cannons.team = team;
//            }
//            for(int i = 0;i<unitAdd.CannonUnitList.size();i++){
//                unitAdd.tower_obj.add(new UnitPattern(unitAdd.CannonUnitList.get(i).CannonAdd(unitAdd,unitAdd.TowerXY[i][0],unitAdd.TowerXY[i][1]),unitAdd));
//            }
//            for(Unit cannons : unitAdd.tower_obj){
//                cannons.team = team;
//            }

            UnitList.add(unitAdd);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    public Unit UnitAdd(int x, int y,boolean host,byte team){
        Unit unitAdd;
        try {
            unitAdd = (Unit) this.clone();
            unitAdd.x = x;
            unitAdd.y = y;
            unitAdd.host = host;
            unitAdd.tower_obj = new ArrayList<>();
            unitAdd.tower_x = x;
            unitAdd.tower_y = y;
            for(int i = 0;i<unitAdd.CannonUnitList.size();i++){
                unitAdd.tower_obj.add(new UnitPattern(unitAdd.CannonUnitList.get(i).
                        CannonAdd(unitAdd,unitAdd.TowerXY[i][0],unitAdd.TowerXY[i][1]),unitAdd));
            }
            unitAdd.corpus_width_zoom = (int)(unitAdd.corpus_width*Main.Zoom);
            unitAdd.corpus_height_zoom = (int)(unitAdd.corpus_height*Main.Zoom);
            unitAdd.width_tower_zoom = (int)(unitAdd.width_tower *Main.Zoom);
            unitAdd.height_tower_zoom = (int)(unitAdd.height_tower *Main.Zoom);
            unitAdd.const_x_corpus = (int)(unitAdd.corpus_width_2*Main.Zoom);
            unitAdd.const_y_corpus = (int)(unitAdd.corpus_height_2*Main.Zoom);
            unitAdd.const_x_tower = (int)(unitAdd.const_tower_x*Main.Zoom);
            this.const_y_tower = (int)(unitAdd.const_tower_y*Main.Zoom);
            for (Unit tower : unitAdd.tower_obj){
                tower.width_tower_zoom = (int)(tower.width_tower *Main.Zoom);
                tower.height_tower_zoom = (int)(tower.height_tower *Main.Zoom);
                tower.const_x_tower = (int)(tower.const_tower_x*Main.Zoom);
                tower.const_y_tower = (int)(tower.const_tower_y*Main.Zoom);
            }
            for(Unit cannons : unitAdd.tower_obj){
                cannons.team = team;
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(unitAdd);
        //System.out.println(unitAdd==this);
        return unitAdd;
    }
    protected final void data(){
        //tower_obj = new ArrayList<>();
        path = new ArrayList<>();
        this.id_unit = 10000+rand.rand(89999);
        this.reload = this.reload_max;
        this.hp = this.max_hp;
        this.HPTriggerHill = this.max_hp/3;
        this.time_spawn_soldat = this.time_spawn_soldat_max;
        this.corpus_width_2 = this.corpus_width/2;
        this.corpus_height_2 = this.corpus_height/2;
        corpus_height_3 = (float) (corpus_height_2/1.5);
        corpus_width_3 = (float)(corpus_width_2*1.2);
        if(tower_img != null){
            this.difference_x = this.difference - this.x_tower;
            if(difference_2 != 0) {
                this.difference_y = this.difference_2 - this.y_tower;
            }else {
                this.difference_y = this.difference - this.y_tower;
            }
            this.tower_width_2 = this.width_tower/2;
            this.tower_height_2 = this.height_tower/2;
            this.const_x_tower = (int)(const_tower_x*Main.Zoom);
            this.const_y_tower = (int)(const_tower_y*Main.Zoom);}
        this.corpus_width_zoom = (int)(corpus_width*Main.Zoom);
        this.corpus_height_zoom = (int)(corpus_height*Main.Zoom);
        this.width_tower_zoom = (int)(width_tower *Main.Zoom);
        this.height_tower_zoom = (int)(height_tower *Main.Zoom);
        this.const_x_corpus = (int)(corpus_width_2*Main.Zoom);
        this.const_y_corpus = (int)(corpus_height_2*Main.Zoom);
        green_len = ((float)this.hp/this.max_hp)* Option.size_x_indicator;
    }
    protected final void dataSoldat(){
        classUnit = ClassUnit.Soldat;
        path = new ArrayList<>();
        this.id_unit = 10000+rand.rand(89999);
        this.reload = this.reload_max;
        this.hp = this.max_hp;
        this.HPTriggerHill = this.max_hp/3;
        this.time_spawn_soldat = this.time_spawn_soldat_max;
        this.corpus_width_2 = this.corpus_width/2;
        this.corpus_height_2 = this.corpus_height/2;
        corpus_height_3 = (float) (corpus_height_2/1.5);
        corpus_width_3 = (float)(corpus_width_2*1.2);
        this.corpus_width_zoom = (int)(corpus_width*Main.Zoom);
        this.corpus_height_zoom = (int)(corpus_height*Main.Zoom);
        this.width_tower_zoom = (int)(width_tower *Main.Zoom);
        this.height_tower_zoom = (int)(height_tower *Main.Zoom);
        this.const_x_corpus = (int)(corpus_width_2*Main.Zoom);
        this.const_y_corpus = (int)(corpus_height_2*Main.Zoom);
        green_len = ((float)this.hp/this.max_hp)* Option.size_x_indicator;
    }
    protected final void data_tower(){
        classUnit = ClassUnit.Tower;
        this.reload = this.reload_max;
        this.difference_x = this.difference - this.x_tower;
        if(difference_2 != 0) {
            this.difference_y = this.difference_2 - this.y_tower;
        }else {
            this.difference_y = this.difference - this.y_tower;
        }
        this.tower_width_2 = this.width_tower /2;
        this.tower_height_2 = this.height_tower /2;
        this.width_tower_zoom = (int)(width_tower *Main.Zoom);
        this.height_tower_zoom = (int)(height_tower *Main.Zoom);

        this.const_x_tower = (int)(const_tower_x*Main.Zoom);
        this.const_y_tower = (int)(const_tower_y*Main.Zoom);
    }
    public void UpdateTower(){

    }
    public void UpdateUnit(){

    }
    public final void tower_iteration(Unit unit){
        for (Unit Tower : tower_obj){
            //Tower.tower_action();
            Tower.functional.FunctionalIterationAnHost(Tower);
            Tower.x = unit.x;
            Tower.y = unit.y;
            Tower.rotation_corpus = unit.rotation_corpus;
        }
    }
    public final void behavior_bot(){
        review_field();
        if (!this.trigger_attack) {
            if (this.time_trigger_bull_bot > 0) {
                motor_bot_bypass();
                this.time_trigger_bull_bot -= 1;
            }
        } else {
            motor_bot_bypass();
        }
    }
    protected final void review_field(){
        Object[] sp = detection_near_transport(this);
        if(sp[0]!= null) {
            this.trigger_attack = (int) sp[1] < this.range_see;
        }
        else{
            trigger_attack = false;
        }


    }
    private void peaceful_behavior(){
        this.time_relocation-= 1;
        if(this.time_relocation<0){
            if(rand.rand(2)== 1) {
                this.x_relocation = this.x + (200.0f+rand.rand(300.0f));
            }
            else{this.x_relocation = this.x + (-500f+rand.rand(300f));}
            if(rand.rand(2)== 1) {
                this.y_relocation = this.y + (200.0f+rand.rand(300f));
            }
            else{this.y_relocation = this.y + -500f+rand.rand(300f);}
            for (i = 0; i< Main.BuildingList.size(); i++){
                if(Main.BuildingList.get(i).x- Main.BuildingList.get(i).width_2>this.x_relocation & Main.BuildingList.get(i).x+ Main.BuildingList.get(i).width<this.x_relocation){
                    if(Main.BuildingList.get(i).y- Main.BuildingList.get(i).height_2>this.y_relocation & Main.BuildingList.get(i).y+ Main.BuildingList.get(i).height<this.y_relocation) {
                        if(rand.rand(2)== 1) {
                            this.x_relocation = this.x + (200.0f+rand.rand(300f));
                        }
                        else{this.x_relocation = this.x + (-500f+rand.rand(300f));}
                        if(rand.rand(2)== 1) {
                            this.y_relocation = this.y + (200.0f+rand.rand(300f));
                        }
                        else{this.y_relocation = this.y + (-500f+rand.rand(300f));}
                    }
                }
            }
        }
            //this.rotation_relocation = atan2(this.y-this.y_relocation,this.x-this.x_relocation)*3.1415926535/180;
        this.time_relocation = this.time_max_relocation;
        this.rotation_relocation = (float) ((atan2(this.y-this.y_relocation,this.x-this.x_relocation)/3.1415926535f*180f)-90f);
        //rotation_bot(rotation_relocation,g_left,g_right);
        //g = sqrt(pow(this.x-this.x_relocation,2)+pow(this.y-this.y_relocation,2));
        //bypass_build(Main.build,this.x_relocation,this.y_relocation,this.rotation_relocation,g_right,g_left,i);

    }
    public final void TowerAI() {
        if (this.trigger_attack) {
            Unit unit = detection_near_transport_i(this);
            if(unit != null) {
                this.TargetX = unit.tower_x;
                this.TargetY = unit.tower_y;

                this.sost_fire_bot = fire_bot(unit.tower_x, unit.tower_y);
                //this.left_mouse = sost_fire_bot & trigger_fire;
            }

        } else {
            if (this.rotation_tower > this.rotation_corpus + 180) {
                this.rotation_tower -= this.speed_tower;
            }
            if (this.rotation_tower < this.rotation_corpus + 180) {
                this.rotation_tower += this.speed_tower;
            }
        }
    }
    public final void MotorControl(){
        this.time_sound_motor -= 1;
        if (this.press_w) {
            if (this.time_sound_motor < 0) {
                SoundPlay.sound(Main.ContentSound.motor_back, 1);
                SoundPacket soundPacket = new SoundPacket();
                soundPacket.ix = (int) this.x;
                soundPacket.iy = (int) this.y;
                soundPacket.ID = 1;
                SoundPack.add(soundPacket);
                this.time_sound_motor = this.time_max_sound_motor;

            }
            if (this.max_speed > this.speed) {
                this.speed += this.acceleration;
            }
        }
        if (this.press_s) {
            if (this.time_sound_motor < 0) {
                SoundPlay.sound(Main.ContentSound.motor, 1);
                SoundPacket soundPacket = new SoundPacket();
                soundPacket.ix = (int) this.x;
                soundPacket.iy = (int) this.y;
                soundPacket.ID = 0;
                SoundPack.add(soundPacket);
                this.time_sound_motor = this.time_max_sound_motor;
            }
            if(this.min_speed < this.speed) {
                this.speed -= this.acceleration;
            }

        }

        if (this.press_a){
            for (Unit Tower : tower_obj){
                Tower.rotation_tower += this.speed_rotation;
            }
            this.rotation_corpus += this.speed_rotation;
        }
        if (this.press_d){
            for (Unit Tower : tower_obj){
                Tower.rotation_tower -= this.speed_rotation;
            }
            this.rotation_corpus -= this.speed_rotation;
        }
        if(!this.press_w && !this.press_s) {
            if (this.speed > 0.5) {
                this.speed -= this.slowing;
                //if (this.speed< Unit.speed_minimum){this.speed = 0;}
            } else if (this.speed < -0.5) {
                this.speed += this.slowing;
                //if (this.speed> Unit.speed_minimum){this.speed = 0;}
            } else{
                this.speed = 0;
            }
        }
        move_xy_transport();
        press_w = false;
        press_a = false;
        press_s = false;
        press_d = false;
    }
    public final void move_xy_transport(){
        float rotation_corpus2 = (float) (-this.rotation_corpus*3.1415/180);
        this.x -= move.move_sin2(this.speed, rotation_corpus2);
        this.y -= move.move_cos2(this.speed, rotation_corpus2);
    }
    public final void move_xy_transportInvert(){
        float rotation_corpus2 = (float) (-this.rotation_corpus*3.1415/180);
        this.x += move.move_sin2(this.speed, rotation_corpus2);
        this.y += move.move_cos2(this.speed, rotation_corpus2);
    }
    public void TowerControl() {
        tower(this.tower_x,this.tower_y,TargetX,TargetY, this.speed_tower);
    }
    public void tower(float x, float y, float x_2, float y_2, float speed_tower) {
        int gh = (int) (atan2(y - y_2, x - x_2) / 3.1415926535 *180);
        if(gh>50 && rotation_tower<-50){
            gh= -180;
        }
        if(gh<-50 && rotation_tower>50){
            gh= 180;
        }
        if (rotation_tower > 179){rotation_tower = -179;}
        else if (rotation_tower < -179){rotation_tower = 179;}
        if (rotation_tower < gh) {
            rotation_tower += speed_tower;
        } else if (rotation_tower > gh) {
            rotation_tower -= speed_tower;
        }
        if(abs(rotation_tower-gh)<20 & trigger_fire){
            left_mouse = true;
        }

    }
    public void SoldatRotate(float x, float y, float x_2, float y_2, float speed_tower) {
        int gh = (int) (atan2(y - y_2, x - x_2) / 3.1415926535 *180);
        if(gh>50 && rotation_corpus<-50){
            gh= -180;
        }
        if(gh<-50 && rotation_corpus>50){
            gh= 180;
        }
        if (rotation_corpus > 179){rotation_corpus = -179;}
        else if (rotation_corpus < -179){rotation_corpus = 179;}
        if (rotation_corpus < gh) {
            rotation_corpus += speed_tower;
        } else if (rotation_corpus > gh) {
            rotation_corpus -= speed_tower;
        }
        if(abs(rotation_corpus-gh)<20 & trigger_fire){
            left_mouse = true;
        }

    }
    public void BotSoldatFire(){
        //Unit unit = detection_near_transport_i(this);
        //System.out.println(GunUse);
            if(AIScan) {
                //trigger_fire = true;
                if (GunUse == null) {
                    inventory.ItemUseType(TypeItem.Gun,this);
                }else if(RadiusTarget<650f){
                    inventory.ItemUseTeg(TegItem.intimately,this);
                }
                else{
                    inventory.ItemUseTeg(TegItem.afar,this);
                }
                //this.g = (float) sqrt(pow2((xy[0] - BlockList2D.get(path.get(0)[1]).get(path.get(0)[0]).x_center)) + pow2(xy[1] - BlockList2D.get(path.get(0)[1]).get(path.get(0)[0]).y_center));

            }
            //this.sost_fire_bot = fire_bot_not_tower(unit.tower_x, unit.tower_y);
            //this.sost_fire_bot = fire_bot();
            this.left_mouse = fire_bot() & trigger_fire;
            rotation_tower = rotation_corpus+90;
//            left_mouse = true;
//            System.out.println(left_mouse);
        }

    private boolean enemy_fire_not_tower(){
        if(UnitList.size() != 0) {
            Unit unit = detection_near_transport_i(this);
            return fire_bot_not_tower(unit.x,unit.y);
        }
        return false;
    }

    protected void blade_helicopter(){
        this.rotation_tower += speed_tower;
    }
    public void bull_packets(int i1, int i2){
        BullPacket bullPack = PacketBull.get(i1);
        Bullet bullet = BulletList.get(i2);
        if(bullet != null) {
            bullPack.x = this.fire_x;
            bullPack.y = this.fire_y;
            bullPack.team = this.team;
            bullPack.rotation = bullet.rotation;
            bullPack.time = bullet.time;
            bullPack.speed = bullet.speed;
            bullPack.height = bullet.height;
            bullPack.ID = bullet.ID;
        }
    }

    protected void motor_bot_bypass() {

        Object[]ObjList = less_hp_bot();
        if(ObjList[0] != null) {
            Unit unit = (Unit) ObjList[0];
            g = (float) (atan2(this.y - unit.y, this.x - unit.x) / 3.1415926535f * 180f);
            g -= 90;
            AITransport(g,unit, (Float) ObjList[1]);
        }
    }
    public boolean reload_bot(){
        if(this.reload > 0){
            this.reload -= 1;
            return false;
        }
        return true;

    }
    protected void indicator_hp_2() {
        Render.rect(((this.x_rend - Option.const_hp_x_zoom)), ((this.y_rend - Option.const_hp_y_zoom)), Option.size_x_indicator_zoom, Option.size_y_indicator_zoom
        ,new Color(Option.hp_2_r_indicator, Option.hp_2_g_indicator, Option.hp_2_b_indicator, 0.3F));
        if(!crite_life){
            Render.rect(((this.x_rend - Option.const_hp_x_zoom)), ((this.y_rend - Option.const_hp_y_zoom)), (int) (green_len * Main.Zoom), Option.size_y_indicator_zoom,
                    new Color(Option.hp_r_indicator, Option.hp_g_indicator, Option.hp_b_indicator, 0.3F));
        }
        else{
            Render.rect(((this.x_rend - Option.const_hp_x_zoom)), ((this.y_rend - Option.const_hp_y_zoom)), (int) (green_len * Main.Zoom), Option.size_y_indicator_zoom
            ,new Color(Option.hp_crite_r_indicator, Option.hp_crite_g_indicator, Option.hp_crite_b_indicator, 0.3F));
        }
    }
    protected void indicator_reload(){
        green_len_reload = (this.reload/this.reload_max)* Option.size_x_indicator;
        Render.rect((this.x_tower_rend+ width_tower_zoom-Option.size_x_indicator_zoom),(this.y_tower_rend- height_tower_zoom),
                Option.size_x_indicator_zoom,
                Option.size_y_indicator_zoom,new Color(Option.reload_r_indicator, Option.reload_g_indicator, Option.reload_b_indicator,0.3f));
        Render.rect((this.x_tower_rend+ width_tower_zoom-Option.size_x_indicator_zoom),
                (this.y_tower_rend- height_tower_zoom),(int)(green_len_reload* Main.Zoom),
                Option.size_y_indicator_zoom,new Color(Option.reload_2_r_indicator, Option.reload_2_g_indicator, Option.reload_2_b_indicator,0.3f));
    }
    public void FireControl(){
        if(this.reload_bot() && this.left_mouse){
            fire.FireIteration(this);
            reload = reload_max;
            this.left_mouse = false;
        }
    }

    public void TowerXY(){
    float []xy = Method.tower_xy(this.x,this.y,this.tower_x_const,this.tower_y_const,this.difference,-this.rotation_corpus);
        this.tower_x = xy[0];this.tower_y = xy[1];}
    public void TowerXY2(){
        float []xy = Method.tower_xy_2(this.x,this.y,this.tower_x_const,this.tower_y_const,this.difference,this.difference_2,-this.rotation_corpus);
        this.tower_x = xy[0];this.tower_y = xy[1];}
    protected boolean fire_bot(float obj_x,float obj_y){
        g = (float) (atan2(this.tower_y - obj_y,this.tower_x-obj_x ) / 3.1415926535f * 180f);
        return abs(g - (rotation_tower)) < 20;
    }
    protected boolean fire_bot(){
        //rotation_tower = rotation_corpus;
        return abs(AngleTarget - (rotation_corpus)) < 20;
    }
    protected boolean fire_bot_not_tower(double obj_x,double obj_y){
        g = (float) (atan2(this.tower_y - obj_y,this.tower_x-obj_x ) / 3.1415926535f * 180f);
        sost_fire_bot = abs(g-rotation_tower)<20;
        //sost_fire_bot = true;
        if(reload_bot() & sost_fire_bot){
            this.reload = this.reload_max;
            return true;
        }
        return false;
    }
    private void motor_bot_base(float g,byte behavior){
        this.time_sound_motor -=1;

        if (this.trigger_drive == 1 && !this.crite_life) {
            switch (behavior) {
                case 1:{
                    if (this.speed > this.min_speed) {
                        press_w = true;
                    }
                    break;
                }
                case 2:{
                    if (g > distance_target) {
                        press_w = true;
                        return;
                    }
                    press_s = true;
                    break;

                }
                case 3:{
                    if (g > distance_target) {
                        press_w = true;
                        return;
                    }if (g < distance_target_2) {
                        press_s = true;

                    }
                    break;
                }
            }
        }
    }
    private void motor_bot_base(int g,byte behavior){
        this.time_sound_motor -=1;

        if (this.trigger_drive == 1 && !this.crite_life) {
            switch (behavior) {
                case 1:{
                    if (this.speed > this.min_speed) {
                        this.speed -= this.acceleration;
                        if (this.time_sound_motor < 0) {
                            SoundPlay.sound(Main.ContentSound.motor, 1f-((float) sqrt(pow2(this.x_rend) + pow2((float)this.y_rend))/SoundConst));
                            SoundPacket soundPacket = new SoundPacket();
                            soundPacket.ix = (int) this.x;
                            soundPacket.iy = (int) this.y;
                            soundPacket.ID = 0;
                            SoundPack.add(soundPacket);
                            this.time_sound_motor = this.time_max_sound_motor;
                        }
                    }
                    break;
                }
                case 2:{
                    if (g > distance_target && this.speed < this.max_speed) {
                        this.speed += this.acceleration;
                        if (this.time_sound_motor < 0) {
                            SoundPlay.sound(Main.ContentSound.motor_back, 1f-((float) sqrt(pow2(this.x_rend) + pow2((float)this.y_rend))/SoundConst));
                            SoundPacket soundPacket = new SoundPacket();
                            soundPacket.ix = (int) this.x;
                            soundPacket.iy = (int) this.y;
                            soundPacket.ID = 1;
                            SoundPack.add(soundPacket);
                            this.time_sound_motor = this.time_max_sound_motor;
                        }
                    } else if(this.speed > this.min_speed){
                        this.speed -= this.acceleration;
                        if (this.time_sound_motor < 0) {
                            SoundPlay.sound(Main.ContentSound.motor, 1f-((float) sqrt(pow2(this.x_rend) + pow2((float)this.y_rend))/SoundConst));
                            SoundPacket soundPacket = new SoundPacket();
                            soundPacket.ix = (int) this.x;
                            soundPacket.iy = (int) this.y;
                            soundPacket.ID = 0;
                            SoundPack.add(soundPacket);
                            this.time_sound_motor = this.time_max_sound_motor;
                        }
                    }
                    break;
                }
                case 3:{
                    if (g > distance_target && this.speed < this.max_speed) {
                        this.speed += this.acceleration;
                        if (this.time_sound_motor < 0) {
                            SoundPlay.sound(Main.ContentSound.motor_back, 1f-((float) sqrt(pow2(this.x_rend) + pow2((float)this.y_rend))/SoundConst));
                            this.time_sound_motor = this.time_max_sound_motor;
                        }
                    } else if (g > distance_target_2 && this.speed > this.min_speed) {
                        this.speed -= this.acceleration;
                        if (this.time_sound_motor < 0) {
                            SoundPlay.sound(Main.ContentSound.motor, 1f-((float) sqrt(pow2(this.x_rend) + pow2((float)this.y_rend))/SoundConst));
                            this.time_sound_motor = this.time_max_sound_motor;
                        }

                    } else {
                        if (this.speed < 0) {
                            this.speed -= this.acceleration;
                            if (this.time_sound_motor < 0) {
                                SoundPlay.sound(Main.ContentSound.motor, 1f-((float) sqrt(pow2(this.x_rend) + pow2((float)this.y_rend))/SoundConst));
                                this.time_sound_motor = this.time_max_sound_motor;
                            }
                        } else if (this.speed > 0) {
                            this.speed += this.acceleration;
                            if (this.time_sound_motor < 0) {
                                SoundPlay.sound(Main.ContentSound.motor_back, 1f-((float) sqrt(pow2(this.x_rend) + pow2((float)this.y_rend))/SoundConst));
                                this.time_sound_motor = this.time_max_sound_motor;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
    private void rotation_bot(float g) {

        if (g > 20 && this.rotation_corpus < -180) {
            g = -272;
            //if (this.rotation_corpus <= g) {
                //this.rotation_corpus = 91;
            //}
        }
        else if (g < -160 && this.rotation_corpus > 0) {
            g = 92;
            //if (this.rotation_corpus >= g) {
                //this.rotation_corpus = -271;
            //}
        }
        if (this.rotation_corpus > 91) {
            this.rotation_corpus = -269;
        }
        else if (this.rotation_corpus < -271) {
            this.rotation_corpus = 89;
        }
        if (g > this.rotation_corpus) {
            press_a = true;
        } else if (g < this.rotation_corpus) {
            press_d = true;
        }
        if (abs(g-rotation_corpus)<20 && !this.crite_life){
            this.trigger_drive = 1;
        }
        else{
            this.trigger_drive = 0;
        }
    }
    private void motor_bot_base() {
        this.time_sound_motor -= 1;
        if (this.trigger_drive == 1 && !this.crite_life) {
            press_w = true;
        }
    }

    private void AITransport(float g, Unit Target,float rad) {
        if (AIScan) {
            if (null != findIntersection(this.tower_x, this.tower_y, Target.tower_x, Target.tower_y)) {
                Ai.pathAIAStar(this,Target,this.tower_x,this.tower_y);
            } else {
                path.clear();
                trigger_fire = true;
            }
        }
        if(path.size() > 0) {
                float []xy = Method.tower_xy_2(this.x,this.y,this.ai_x_const,this.ai_y_const,0,0,-this.rotation_corpus);
                this.g = (float) sqrt(pow2((xy[0] - BlockList2D.get(path.get(0)[1]).get(path.get(0)[0]).x_center)) + pow2(xy[1] - BlockList2D.get(path.get(0)[1]).get(path.get(0)[0]).y_center));
                float gr = (float) ((atan2(xy[1] - BlockList2D.get(path.get(0)[1]).get(path.get(0)[0]).y_center,xy[0] - BlockList2D.get(path.get(0)[1]).get(path.get(0)[0]).x_center)/3.1415926535*180)-90);
                rotation_bot(gr);
                motor_bot_base();
                if(this.g< 70){
                    path.remove(0);
                }
            }
            else{
                //float rad = (float) sqrt(pow2((x - Target.x)) + pow2(y - Target.y));
                rotation_bot(g);
                motor_bot_base(rad, this.behavior);
            }
    }
    protected float[] findIntersection(float x0, float y0, float dx, float dy) {
        float x = dx/width_block-1;
        float y = dy/height_block-1;
        dx = x0/width_block-1;
        dy = y0/height_block-1;
        float xy_r = (float)(atan2(y-dy, x-dx));
        float speed_x = (float) cos(xy_r);
        float speed_y = (float) sin(xy_r);
        if (y > dy) {
            if (x > dx) {
                while (x > dx && y > dy) {
                    x -= speed_x;
                    y -= speed_y;
                    if (BlockList2D.get((int)y).get((int)x).passability) {
                        return new float[]{x, y};
                    }

                }
            } else if(x < dx){
                while (x < dx && y > dy) {
                    x -= speed_x;
                    y -= speed_y;
                    if (BlockList2D.get((int)y).get((int)x).passability) {
                        return new float[]{x, y};
                    }
                }
            }
            else{
                while (y > dy) {
                    y -= speed_y;
                    if (BlockList2D.get((int)y).get((int)x).passability) {
                        return new float[]{x, y};
                    }
                }
            }
            return null;
        } else if(y < dy){
            if (x > dx) {
                while (x > dx && y < dy) {
                    x -= speed_x;
                    y -= speed_y;
                    if (BlockList2D.get((int)y).get((int)x).passability) {
                        return new float[]{x, y};
                    }
                }
            } else if (x < dx){
                while (x < dx && y < dy) {
                    x -= speed_x;
                    y -= speed_y;
                    if (BlockList2D.get((int)y).get((int)x).passability) {
                        return new float[]{x, y};
                    }
                }
            }
            else {
                while (y < dy) {
                    y -= speed_y;
                    System.out.println(x+" "+y);
                    if (BlockList2D.get((int)y).get((int)x).passability) {
                        return new float[]{x, y};
                    }
                }
            }
            return null;
        }
        else {
            if (x > dx) {
                while (x > dx) {
                    x -= speed_x;
                    if (BlockList2D.get((int)y).get((int)x).passability) {
                        return new float[]{x, y};
                    }
                }
            } else if (x < dx){
                while (x < dx) {
                    x -= speed_x;
                    if (BlockList2D.get((int)y).get((int)x).passability) {
                        return new float[]{x, y};
                    }
                }
            }
            return null;
        }

    }



    private void speed_balance(){
        if(this.speed<this.acceleration && this.speed>-this.acceleration){
            this.speed = 0;
        }
    }
    public void helicopter_ii(){
        Object[]sp = detection_near_transport(this);
        if(sp[0] != null) {
            Unit unit = (Unit) sp[0];
            g = (float) (atan2(this.y - unit.y, this.x - unit.x) / 3.1415926535 * 180);
            g -= 90;
            rotation_bot(g);
            motor_bot_base((int) sp[1], this.behavior);
        }
        speed_balance();


    }
    private Object[] less_hp_bot(){
        if (this.hp > HPTriggerHill && this.medic_help == 0) {
            return DetectionNearTransport(this);

        } else{
            this.behavior_buffer = this.behavior;
            this.behavior = 3;
            this.medic_help = 1;
            if (this.hp >= this.max_hp - 20) {
                this.medic_help = 0;
                this.behavior = this.behavior_buffer;
            }
            Unit unit2 = null;
            float radius = 0;
            for (Unit unitSupport : UnitList) {
                if(this != unitSupport && unitSupport.classUnit == SupportTransport) {
                    g = (float) sqrt(pow2.pow2(unitSupport.x - this.x) + pow2.pow2(unitSupport.y - this.y));
                    if (radius == 0 || radius > g) {
                        unit2 = unitSupport;
                        radius = g;

                    }
                }
            }
            if(unit2 != null){
                return new Object[]{unit2,radius};
            }
            return DetectionNearTransport(this);
        }

    }
    public void corpus_corpus(ArrayList<Unit> obj_2){
        for (Unit unit : obj_2) {
            if (unit != this) {
                if (rectCollision((int) this.x, (int) this.y, (int) this.corpus_width, (int) this.corpus_height,this.rotation_corpus,
                        (int) unit.x,(int) unit.y, (int) unit.corpus_width, (int) unit.corpus_height, unit.rotation_corpus)
                        && unit.priority_paint == this.priority_paint) {
                    SoundPlay.sound(Main.ContentSound.hit, 1f - ((float) sqrt(pow2(this.x_rend) + pow2(this.y_rend)) / SoundConst));
                    SoundPacket soundPacket = new SoundPacket();
                    soundPacket.ix = (int) this.x;
                    soundPacket.iy = (int) this.y;
                    soundPacket.ID = 7;
                    SoundPack.add(soundPacket);
                    MethodCollision(unit);
                    physicCollision(unit);

                }
            }
        }
    }
    private void physicCollision(Unit unit){
        float x = this.x+corpus_width_2;
        float y = this.y+corpus_height_2;
        float[]xy;
        float v = 4;
        float x_2 = unit.x+ unit.corpus_width_2;
        float y_2 = unit.y+ unit.corpus_height_2;
        xy = Method.tower_xy(x,y,0,0,-corpus_height_2,-rotation_corpus);
        float x_1_2 = xy[0];
        float y_1_2 = xy[1];
        xy = Method.tower_xy(x_2,y_2,0,0,-unit.corpus_height_2,-unit.rotation_corpus);
        float x_2_2 = xy[0];
        float y_2_2 = xy[1];
        if(sqrt(pow2(x_1_2 - x_2_2) + pow2(y_1_2 - y_2_2))<(unit.corpus_width_2+corpus_width_2)*1.4){
            xy = Method.tower_xy_2(x_2,y_2,0,0,-unit.corpus_height_3, unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_2_1 = xy[0];
            float y_2_2_1 = xy[1];
            xy = Method.tower_xy_2(x_2,y_2,0,0,-unit.corpus_height_3,-unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_2_2 = xy[0];
            float y_2_2_2 = xy[1];
            xy = Method.tower_xy_2(x,y,0,0,-corpus_height_3,corpus_width_3,-rotation_corpus);
            float x_1_2_1 = xy[0];
            float y_1_2_1 = xy[1];
            xy = Method.tower_xy_2(x,y,0,0,-corpus_height_3,-corpus_width_3,-rotation_corpus);
            float x_1_2_2 = xy[0];
            float y_1_2_2 = xy[1];
            if(sqrt(pow2(x_2_2_1 - x_1_2) + pow2(y_2_2_1 - y_1_2))<(unit.corpus_width_2+corpus_width_2)/1.5) {
                unit.rotation_corpus += (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_2_2_2 - x_1_2) + pow2(y_2_2_2 - y_1_2))<(unit.corpus_width_2+corpus_width_2)/1.5) {
                unit.rotation_corpus -= (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_1_2_1 - x_2_2) + pow2(y_1_2_1 - y_2_2))<(unit.corpus_width_2+corpus_width_2)/1.5) {
                this.rotation_corpus += (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_1_2_2 - x_2_2) + pow2(y_1_2_2 - y_2_2))<(unit.corpus_width_2+corpus_width_2)/1.5) {
                this.rotation_corpus -= (abs(unit.speed) + 1) * v;
            }
            return;
        }
        xy = Method.tower_xy(x,y,0,0,corpus_height_2,-rotation_corpus);
        float x_1_1 = xy[0];
        float y_1_1 = xy[1];
        xy = Method.tower_xy(x_2,y_2,0,0, unit.corpus_height_2,-unit.rotation_corpus);
        float x_2_1 = xy[0];
        float y_2_1 = xy[1];
        if(sqrt(pow2(x_1_1 - x_2_1) + pow2(y_1_1 - y_2_1))<(unit.corpus_width_2+corpus_width_2)*1.2){
            xy = Method.tower_xy_2(x_2,y_2,0f,0f, unit.corpus_height_3, unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_1_1 = xy[0];
            float y_2_1_1 = xy[1];
            xy = Method.tower_xy_2(x_2,y_2,0,0, unit.corpus_height_3,-unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_1_2 = xy[0];
            float y_2_1_2 = xy[1];
            xy = Method.tower_xy_2(x,y,0,0,corpus_height_3,corpus_width_3,-rotation_corpus);
            float x_1_1_1 = xy[0];
            float y_1_1_1 = xy[1];
            xy = Method.tower_xy_2(x,y,0,0,corpus_height_3,-corpus_width_3,-rotation_corpus);
            float x_1_1_2 = xy[0];
            float y_1_1_2 = xy[1];
            if(sqrt(pow2(x_2_1_1 - x_1_1) + pow2(y_2_1_1 - y_1_1))<(unit.corpus_width_2+corpus_width_2)/1.2) {
                unit.rotation_corpus -= (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_2_1_2 - x_1_1) + pow2(y_2_1_2 - y_1_1))<(unit.corpus_width_2+corpus_width_2)/1.2) {
                unit.rotation_corpus += (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_1_1_1 - x_2_1) + pow2(y_1_1_1 - y_2_1))<(unit.corpus_width_2+corpus_width_2)/1.2) {
                this.rotation_corpus -= (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_1_1_2 - x_2_1) + pow2(y_1_1_2 - y_2_1))<(unit.corpus_width_2+corpus_width_2)/1.2) {
                this.rotation_corpus += (abs(unit.speed) + 1) * v;
            }
            return;
        }
        if(sqrt(pow2(x_1_1 - x_2_2) + pow2(y_1_1 - y_2_2))<(unit.corpus_width_2+corpus_width_2)*1.2){
            xy = Method.tower_xy_2(x_2,y_2,0,0,-unit.corpus_height_3, unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_2_1 = xy[0];
            float y_2_2_1 = xy[1];
            xy = Method.tower_xy_2(x_2,y_2,0,0,-unit.corpus_height_3,-unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_2_2 = xy[0];
            float y_2_2_2 = xy[1];
            xy = Method.tower_xy_2(x,y,0,0,corpus_height_3,corpus_width_3,-rotation_corpus);
            float x_1_1_1 = xy[0];
            float y_1_1_1 = xy[1];
            xy = Method.tower_xy_2(x,y,0,0,corpus_height_3,-corpus_width_3,-rotation_corpus);
            float x_1_1_2 = xy[0];
            float y_1_1_2 = xy[1];
            if(sqrt(pow2(x_2_2_1 - x_1_1) + pow2(y_2_2_1 - y_1_1))<(unit.corpus_width_2+corpus_width_2)/1.2) {
                unit.rotation_corpus -= (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_2_2_2 - x_1_1) + pow2(y_2_2_2 - y_1_1))<(unit.corpus_width_2+corpus_width_2)/1.2) {
                unit.rotation_corpus += (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_1_1_1 - x_2_2) + pow2(y_1_1_1 - y_2_2))<(unit.corpus_width_2+corpus_width_2)/1.2) {
                this.rotation_corpus -= (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_1_1_2 - x_2_2) + pow2(y_1_1_2 - y_2_2))<(unit.corpus_width_2+corpus_width_2)/1.2) {
                this.rotation_corpus += (abs(unit.speed) + 1) * v;
            }
        }




    }
    private void damage_temperature(){
        if(abs(this.t) > 25){
            this.hp -= abs(this.t/10);
            this.t -=0.5;
            this.green_len = ((float) this.hp / this.max_hp) * Option.size_x_indicator;

        }
    }
    public void transportDelete(){
        if(this.hp>0)return;
        if(this.crite_life){
//            Main.DebrisList.add(new DebrisTransport(this.x,this.y,this.rotation_corpus,this.speed,this.RotationInert,this.SpeedInert,
//                    this.corpus_img,this.corpus_width,this.corpus_height,this.type_unit));
            UnitDelete();
            eventDead();
            packetUnitUpdate.ConfUnitList = true;
            ClearUnitList.add(this);
            //UnitList.remove(this);
            return;
        }
        this.crite_life = true;
        this.hp = this.max_hp/2;

    }
    public void DebrisDelete() {
        if (this.hp > 0) return;
        eventDead();
        packetUnitUpdate.ConfDebrisList = true;
        ClearDebrisList.add(this);




    }
    protected void debrisDelete(){
        if(this.hp<0){
            ClearDebrisList.add(this);
            packetUnitUpdate.ConfDebrisList = true;
            //DebrisList.remove(this);

        }
    }
    public void eventDead(){
        for(int i = 0;i<4;i++){
        Main.BangList.add(new Bang(this.x+corpus_width_2 + -20+rand.rand(40),
                this.y+corpus_height_2 +-20+rand.rand(40),5));}
        for(int i = 0;i<5;i++){
            Main.FlameSpawnList.add(new FlameSpawn(this.x + -5+rand.rand(50),this.y + -5+rand.rand(50)));}
        SoundPlay.sound(Main.ContentSound.kill,1-((float) sqrt(pow2(this.x_rend) + pow2(this.y_rend))/SoundConst));

        SoundPacket soundPacket = new SoundPacket();
        soundPacket.ix = (int) this.x;
        soundPacket.iy = (int) this.y;
        soundPacket.ID = 9;
        SoundPack.add(soundPacket);

    }
    private boolean rectCollision(int x1, int y1, int width, int height, double rotation,
                                  int x2, int y2, int width2, int height2, double rotation_2){

        Rectangle rect1 = new Rectangle(x1,y1,width,height); // Прямоугольник 1
        Rectangle rect2 = new Rectangle(x2,y2,width2,height2); // Прямоугольник 2

        // Создаем аффинное преобразование для поворота
        AffineTransform transform1 = new AffineTransform();
        transform1.rotate(Math.toRadians(rotation), rect1.getCenterX(), rect1.getCenterY());
        AffineTransform transform2 = new AffineTransform();
        transform2.rotate(Math.toRadians(rotation_2), rect2.getCenterX(), rect2.getCenterY());

        // Преобразование прямоугольников с учетом поворота
        Area area1 = new Area(rect1);
        area1.transform(transform1);
        Area area2 = new Area(rect2);
        area2.transform(transform2);

        // Вычисление пересечения двух преобразованных прямоугольников
        area1.intersect(area2);

        // Проверка наличия пересечения
        //Rectangle intersection = area1.getBounds();
        //System.out.println("Прямоугольники пересекаются. Результат: " + intersection);
        return !area1.isEmpty();
    }
    private static boolean z = false;
    private static int render_x_max,render_x_min,render_y_max,render_y_min;
    public void build_corpus(){
        render_x_max = (int) ((x + BorderDetected) / Main.width_block);
        render_x_min = (int) (((x - BorderDetected) / Main.width_block));
        if (render_x_min < 0) {
            render_x_min = 0;
        }
        if (render_x_max > RC.block_i_x_max) {
            render_x_max = RC.block_i_x_max;
        }
        render_y_max = (int) ((y + BorderDetected) / Main.height_block);
        render_y_min = (int) ((y - BorderDetected) / Main.height_block);
        if (render_y_min < 0) {
            render_y_min = 0;
        }
        if (render_y_max > RC.block_i_y_max) {
            render_y_max = RC.block_i_y_max;
        }

        for (int iy = render_y_min; iy < render_y_max; iy++) {
            for (int ix = render_x_min; ix < render_x_max; ix++) {
                if (BlockList2D.get(iy).get(ix).passability) {
                    if (rectCollision((int) this.x, (int) this.y, (int) this.corpus_width, (int) this.corpus_height, this.rotation_corpus, BlockList2D.get(iy).get(ix).x, BlockList2D.get(iy).get(ix).y,
                            width_block, height_block, 0)) {
                        if (this.speed > 2 || this.speed < -2) {
                            SoundPlay.sound(Main.ContentSound.break_wooden, 1 - ((float) sqrt(pow2(this.x_rend) + pow2(this.y_rend)) / SoundConst));
                            SoundPacket soundPacket = new SoundPacket();
                            soundPacket.ix = (int) this.x;
                            soundPacket.iy = (int) this.y;
                            soundPacket.ID = 3;
                            SoundPack.add(soundPacket);
                        }
                        MethodCollision(BlockList2D.get(iy).get(ix).x, BlockList2D.get(iy).get(ix).y);
                    }
                } else {
                    BlockList2D.get(iy).get(ix).objMap.Collision.collision(this, ix, iy);
                }
            }
        }
    }
    private void MethodCollision(Unit unit){
        if(this.x< unit.x) {
            this.x -= 2;
            unit.x += 2;
            this.SpeedInert += unit.speed*0.5;
            unit.SpeedInert += this.speed*0.5;
            this.speed *= -0.8;
            unit.speed *= -0.8;
            this.RotationInert = unit.rotation_corpus;
            unit.RotationInert = this.rotation_corpus;
        }
        else if(this.x> unit.x) {
            this.x += 2;
            unit.x -= 2;
            this.SpeedInert += unit.speed*0.5;
            unit.SpeedInert += this.speed*0.5;
            this.speed *= -0.5;
            unit.speed *= -0.7;
            this.RotationInert = unit.rotation_corpus;
            unit.RotationInert = this.rotation_corpus;
        }
        if(this.y< unit.y) {
            this.y -= 2;
            unit.y += 2;
        }
        else if(this.y> unit.y) {
            this.y += 2;
            unit.y -= 2;
        }
    }
    private void MethodCollision(int x, int y){
        if(this.x<x) {
            this.x -= 2;
            this.speed *= -0.8;
            this.SpeedInert *= -0.8;
        }
        else if(this.x>x) {
            this.x += 2;
            this.speed *= -0.8;
            this.SpeedInert *= -0.8;
        }
        if(this.y<y) {
            this.y -= 2;
        }
        else if(this.y>y) {
            this.y += 2;
        }
    }
    public void move_debris(){
        if(speed != 0) {
            this.x -= move.move_sin(this.speed, -this.rotation_corpus);
            this.y -= move.move_cos(this.speed, -this.rotation_corpus);

            if (this.speed < -0.5) {
                this.speed += this.acceleration;
            } else if (this.speed > 0.5) {
                this.speed -= acceleration;
            } else {
                speed = 0;
            }
        }
    }
    protected void inertia_xy() {
        if(SpeedInert != 0) {
            this.x -= move.move_sin(this.SpeedInert, -this.RotationInert);
            this.y -= move.move_cos(this.SpeedInert, -this.RotationInert);
            if (this.SpeedInert > 0.5) {
                this.SpeedInert -= this.acceleration;
            } else if (this.SpeedInert < -0.5) {
                this.SpeedInert += this.acceleration;
            } else {
                SpeedInert = 0;
            }
        }
    }
    public void hill_bot(ArrayList<Unit> obj){
        for (Unit unit : obj) {
            if (sqrt(pow2(this.x - unit.x) + pow2(this.y - unit.y)) < 230 && unit.max_hp > unit.hp) {
                unit.hp += this.hill;
                unit.green_len = ((float) unit.hp / unit.max_hp) * Option.size_x_indicator;
                if(unit.hp >= unit.max_hp -20 && unit.crite_life){
                    unit.crite_life = false;
                }
            }
        }
    }
    public void bypass_hiller() {
        Object[] sp = Method.detectionNearSupportTransport(this);
        Unit unit = (Unit) sp[0];
        if(unit != null) {
            g = (float) (atan2(this.y - unit.y, this.x - unit.x) / 3.1415926535 * 180);
            g -= 90;
            AITransport(g, unit, (Float)sp[1]);
        }
    }
    public void spawn_soldat(){
        this.time_spawn_soldat -= 1;
        if(this.time_spawn_soldat <= 0){
            this.time_spawn_soldat = this.time_spawn_soldat_max;
            packetUnitUpdate.ConfUnitList = true;
            Inventory inventory1 = new Inventory(new Item[3][4]);
            inventory1.ItemAdd(ItemRegister.AK74);
            inventory1.ItemAdd(ItemRegister.flamethrower);
            switch(rand.rand(2)){
                case 0:{
                    Veteran.UnitAdd((int) this.x, (int) this.y,true, (byte) 2, RegisterControl.controllerSoldatBot,inventory1);
                    break;
                }
                case 1:{
                    Jaeger.UnitAdd((int) this.x, (int) this.y,true, (byte) 2,Main.RegisterControl.controllerSoldatBot,inventory1);
                    break;
                }
                //case 3->{soldat.add(new soldat_(this.x,this.y));}
            }
        }
    }
    public void SoldatRotateBot() {
        if (AngleTarget > 20 && this.rotation_corpus < -180) {
            AngleTarget = -272;
        } else if (AngleTarget < -160 && this.rotation_corpus > 0) {
            AngleTarget = 92;
        }
        if (this.rotation_corpus > 91) {
            this.rotation_corpus = -269;
        } else if (this.rotation_corpus < -271) {
            this.rotation_corpus = 89;
        }
        if (AngleTarget > this.rotation_corpus) {
            //this.rotation_corpus += this.speed_rotation;
            //System.out.println(press_a);
            this.press_a = true;
        } else if (AngleTarget < this.rotation_corpus) {
            //System.out.println(press_a);
            this.press_d = true;
        }
    }
    public void SoldatMoveBot(){
        if(abs(AngleTarget-rotation_corpus)<20 & RadiusTarget<200){
            this.press_s = true;
        }
        else if(abs(AngleTarget-rotation_corpus)<20 & RadiusTarget>250){
            this.press_w = true;
        }
    }
    public void SoldatMovePathBot(){
        if(abs(AngleTarget-rotation_corpus)<20){
            this.press_w = true;
        }
    }
    public final void SoldatControl(){
        //this.time_sound_motor -= 1;
        if (this.press_w) {
            if (this.time_sound_motor < 0) {
                this.time_sound_motor = this.time_max_sound_motor;

            }
            if (this.max_speed > this.speed) {
                this.speed += this.acceleration;
            }
        }
        if (this.press_s) {
            if (this.time_sound_motor < 0) {
                this.time_sound_motor = this.time_max_sound_motor;
            }
            if(this.min_speed < this.speed) {
                this.speed -= this.acceleration;
            }

        }
        if (this.press_a){
            this.rotation_corpus += this.speed_rotation;
        }
        if (this.press_d){
            this.rotation_corpus -= this.speed_rotation;
        }

        if (this.speed > 0 && !this.press_w && !this.press_s) {
            this.speed -= this.slowing;
            if (this.speed< Unit.speed_minimum){this.speed = 0;}
        } else if (this.speed < 0 && !this.press_w && !this.press_s) {
            this.speed += this.slowing;
            if (this.speed> Unit.speed_minimum){this.speed = 0;}
        }
        move_xy_transport();
        press_w = false;
        press_a = false;
        press_s = false;
        press_d = false;
    }
    public void hustle(){
        for (Unit value : UnitList) {
            if(rect_bull((int) value.x, (int) value.y, (int) value.corpus_width, (int) value.corpus_height,
                    (int) this.x, (int) this.y, (int) this.corpus_width, value.rotation_corpus)) {
                this.hp = 0;
            }
        }
    }
    public void AiSoldat(Unit unit){
        //Unit obj = detection_near_transport_i(this);
        AISoldatPath(unit);

    }
    public void AISoldatPath(Unit iEnemy){
        if (AIScan) {
            if (null != findIntersection(x,y, iEnemy.tower_x, iEnemy.tower_y)) {
                path.clear();
                Ai.pathAIAStar(this,iEnemy, x, y);
                trigger_fire = false;
            } else {
                path.clear();
                trigger_fire = true;
            }
        }
        if(path.size() > 0) {
            RadiusTarget = (float) sqrt(pow2((x - BlockList2D.get(path.get(0)[1]).get(path.get(0)[0]).x_center)) + pow2(y - BlockList2D.get(path.get(0)[1]).get(path.get(0)[0]).y_center));
            AngleTarget = (float) ((atan2(y - BlockList2D.get(path.get(0)[1]).get(path.get(0)[0]).y_center,x - BlockList2D.get(path.get(0)[1]).get(path.get(0)[0]).x_center)/3.1415926535*180)-90);
            SoldatRotateBot();
            SoldatMovePathBot();
            if(RadiusTarget< 70){
                path.remove(0);
            }
        }
        else {
            RadiusTarget = (float) sqrt(pow2((x - iEnemy.tower_x)) + pow2(y - iEnemy.tower_y));
            AngleTarget = (float) ((atan2(y - iEnemy.tower_y,x - iEnemy.tower_x)/3.1415926535*180)-90);
            SoldatRotateBot();
            SoldatMoveBot();
        }
    }
    public void clearSoldat(){
        if(this.hp <0){
            for(int i1 =0;i1<12;i1++){
                Main.LiquidList.add(new Blood(this.x+i1, this.y));}
            packetUnitUpdate.ConfUnitList = true;
            ClearUnitList.add(this);
            //UnitList.remove(this);
            EnumerationList = true;
        }
    }
    public boolean rect_bull(int x1,int y1,int width,int height,int x,int y,int size,float rotation){
        Rectangle2D rect1 = new Rectangle2D.Double(x1,y1,width,height);
        AffineTransform transform1 = new AffineTransform();
        transform1.rotate(Math.toRadians(-rotation), rect1.getCenterX(), rect1.getCenterY());
        Area area1 = new Area(rect1);
        area1.transform(transform1);

        Ellipse2D circle = new Ellipse2D.Double(x,y,size,size);

        return area1.intersects(circle.getBounds2D());
    }
    public void all_action(){
        damage_temperature();
        inertia_xy();
        control.ControllerIteration(this);
        functional.FunctionalIterationAnHost(this);
        EventClear.EventIteration(this);
    }
    public void all_action_client(){
        this.green_len = (float) this.hp /this.max_hp * Option.size_x_indicator;
        control.ControllerIterationClientAnHost(this);
        functional.FunctionalIterationClientAnHost(this);
        EventClear.EventIteration(this);

    }
    public void all_action_client_1(){
        this.green_len = (float) this.hp /this.max_hp * Option.size_x_indicator;
        move_xy_transport();
        control.ControllerIterationClientAnClient(this);
        functional.FunctionalIterationAnClient(this);
    }
    public void all_action_client_2(){
        this.green_len = (float) this.hp /this.max_hp * Option.size_x_indicator;
        move_xy_transport();
        functional.FunctionalIterationAnClient(this);
    }
    public void tower_action_client(float x,float y,float rotation,boolean sost_fire_bot){

    }
    public void tower_action_client(float x,float y,float rotation,boolean sost_fire_bot,boolean sost_fire_bot_2){

    }
    public void tower_action_client_1(float x,float y,float rotation,boolean sost_fire_bot){

    }
    public void tower_action_client_2(float x,float y,float rotation,boolean sost_fire_bot){

    }
    public void tower_action(){

    }

    public void update(){

    }
    public void updateTower(){

    }
    protected void center_render(){
        float[]xy = Main.RC.render_objZoom(this.x,this.y);
        this.x_rend = (int)xy[0];
        this.y_rend = (int)xy[1];
        xy = Main.RC.render_objZoom(this.tower_x,this.tower_y);
        this.x_tower_rend = (int)xy[0];
        this.y_tower_rend = (int)xy[1];

    }
    protected void center_corpus_render(){
        float[]xy = Main.RC.render_objZoom(this.x,this.y);
        this.x_rend = (int)xy[0];
        this.y_rend = (int)xy[1];
    }
    public void center_render_tower(){
        float[]xy = Main.RC.render_objZoom(this.tower_x,this.tower_y);
        this.x_tower_rend = (int)xy[0];
        this.y_tower_rend = (int)xy[1];

    }
}

