package com.mygdx.game.bull;
import com.mygdx.game.Network.BullPacket;
import com.mygdx.game.bull.Updater.UpdateRegister;
import com.mygdx.game.bull.Updater.UpdaterBullet;
import com.mygdx.game.main.Main;

import com.mygdx.game.method.move;
import com.mygdx.game.method.rand;
import Content.Particle.Acid;
import Content.Particle.FlameSpawn;
import Content.Particle.Bang;
import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.unit.Unit;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

import static com.mygdx.game.bull.BulletRegister.*;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.pow2.pow2;
import static java.lang.StrictMath.abs;

public abstract class Bullet implements Serializable,Cloneable {
    public float x,y,speed,damage,penetration,rotation,speed_x,speed_y,damage_fragment,penetration_fragment,t_damage,time;
    public int size,AmountFragment,x_rend,y_rend,size_render,x2,y2,sizeRandom;
    public static float r_wane = 4f/255f,g_wane= 1.7f/255f,b_wane= 1f/255f;
    public UpdaterBullet updaterBullet;
    public float  r,g,b;
    public boolean AcidSpawn,TypeShape;
    public int ID;
    public int xMap,yMap;
    public boolean BangSpawn;
    public boolean FlameSpawn,clear_sost;
    public byte type_team,height;
    public static ArrayList<Bullet> BulletListDown=new ArrayList<Bullet>(),BulletListUp=new ArrayList<Bullet>();
    public FunctionalList functionalList;
    public Bullet(float x, float y, float rotation, float damage, float penetration, float damage_fragment, float penetration_fragment, byte type_team, byte height) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.damage = damage;
        this.penetration = penetration;
        this.type_team = type_team;
        this.height = height;
        this.damage_fragment = damage_fragment;
        this.penetration_fragment = penetration_fragment;
        speed_save();

    }
    public Bullet(float x, float y, float rotation, float damage, float penetration, byte type_team, byte height){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.damage = damage;
        this.penetration = penetration;
        this.type_team = type_team;
        this.height = height;
        speed_save();


    }
    public Bullet(float x, float y, float rotation, float damage, float penetration, float damage_fragment,
                  float penetration_fragment, byte type_time, byte height,int tDamage,
                  float speed,int AmountFragment){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.damage = damage;
        this.penetration = penetration;
        this.damage_fragment = damage_fragment;
        this.penetration_fragment = penetration_fragment;
        this.type_team = type_time;
        this.height = height;
        this.speed = speed;
        this.AmountFragment = AmountFragment;
        this.t_damage = tDamage;
        speed_save();

    }
    public Bullet(float r,float g,float b,boolean TypeShape,int width,int height,int SizeRandom,int ID,FunctionalList func
            ,boolean BangSpawn,boolean AcidSpawn,boolean FlameSpawn){
        this.r = r;
        this.g = g;
        this.b = b;
        this.ID = ID;
        this.functionalList = func.clone();
        this.TypeShape = TypeShape;
        this.sizeRandom = SizeRandom;
        this.BangSpawn = BangSpawn;
        this.AcidSpawn = AcidSpawn;
        this.FlameSpawn = FlameSpawn;
        IDBullet.add(new Object[]{this,ID});
        if(TypeShape){
            updaterBullet = UpdateRegister.CircleBullet;
            size = width;
        }
        else{
            updaterBullet = UpdateRegister.RectBullet;
            this.x2 = width;
            this.y2 = height;
        }
    }
    public void BulletAdd(float x, float y, float rotation, float damage, float penetration, float damage_fragment,
                        float penetration_fragment, byte type_team, byte height,float tDamage,
                        float speed,int AmountFragment,float time){
        Bullet bullet;
        try {
            bullet = (Bullet) this.clone();
            bullet.x = x;
            bullet.y = y;
            bullet.rotation = rotation;
            bullet.damage = damage;
            bullet.penetration = penetration;
            bullet.damage_fragment = damage_fragment;
            bullet.penetration_fragment = penetration_fragment;
            bullet.type_team = type_team;
            bullet.height = height;
            bullet.speed = speed;
            bullet.AmountFragment = AmountFragment;
            bullet.t_damage = tDamage;
            bullet.time = time;
            if(bullet.TypeShape){
                bullet.size += rand.rand(bullet.sizeRandom);
            }
            else {
                bullet.x2 +=rand.rand(bullet.sizeRandom);
                bullet.y2 +=rand.rand(bullet.sizeRandom);
                bullet.size = bullet.x2;
            }
            bullet.size_render = (int)(bullet.size*Main.Zoom);
            bullet.speed_save();
            switch (bullet.height){
                case 1:
                    BulletListDown.add(bullet);
                    break;
                case 2:
                    System.out.println("eeee");
                    BulletListUp.add(bullet);
                    break;
            }
            BulletList.add(bullet);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    public Bullet BulletAdd2(float x, float y, float rotation, float damage, float penetration, float damage_fragment,
                          float penetration_fragment, byte type_team, byte height,float tDamage,
                          float speed,int AmountFragment,int time){
        Bullet bullet;
        try {
            bullet = (Bullet) this.clone();
            bullet.x = x;
            bullet.y = y;
            bullet.rotation = rotation;
            bullet.damage = damage;
            bullet.penetration = penetration;
            bullet.damage_fragment = damage_fragment;
            bullet.penetration_fragment = penetration_fragment;
            bullet.type_team = type_team;
            bullet.height = height;
            bullet.speed = speed;
            bullet.AmountFragment = AmountFragment;
            bullet.t_damage = tDamage;
            bullet.time = time;
            if(bullet.TypeShape){
                bullet.size += rand.rand(bullet.sizeRandom);
            }
            else {
                bullet.x2 +=rand.rand(bullet.sizeRandom);
                bullet.y2 +=rand.rand(bullet.sizeRandom);
                bullet.size = bullet.x2;
            }
            bullet.size_render = (int)(bullet.size*Main.Zoom);
            bullet.speed_save();
            return bullet;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }
//    public Bullet(float x, float y, float rotation, float damage, float penetration, byte type_time, byte height){
//
//    }
    protected final void speed_save(){
        this.speed_x = move.move_sin(this.speed,this.rotation);
        this.speed_y = move.move_cos(this.speed,this.rotation);
    }
    protected final void bull_move_xy(){
        this.x += this.speed_x * TimeGlobalBullet;
        this.y += this.speed_y * TimeGlobalBullet;
    }
    protected final void color_fire(){
        if(this.b >0){this.b-= b_wane;}
        if(this.g> 0){this.g -=g_wane;}
        if(this.r > 0){this.r-= r_wane;}
    }
    protected final void spawn_flame(){
        if(1 == rand.rand(16)){
            Main.FlameSpawnList.add(new FlameSpawn(this.x,this.y));

        }
    }
    public final void center_render(){
        float[] xy = RC.render_objZoom(this.x,this.y);
        this.x_rend = (int) xy[0];
        this.y_rend = (int) xy[1];
    }
    protected final void spawn_acid(){
        if(1 == rand.rand(3)){
            Main.LiquidList.add(new Acid(this.x,this.y));
        }
    }
    protected final void clear(){
        if(this.clear_sost){
            if(BangSpawn) {
                Main.BangList.add(new Bang(this.x, this.y, 4));
            }
            if(FlameSpawn) {spawn_flame();}
            if(AcidSpawn){spawn_acid();}
            for (int l = 0; l<AmountFragment; l++) {
                Bullet bullet = BulletFragment.BulletAdd2(this.x,this.y,rand.rand(360),damage_fragment,penetration_fragment,
                        0,0,type_team,height,0,12+rand.rand(6),0,
                        65+rand.rand(35));
                PackBulletFragment(bullet);
                Main.BulletList.add(bullet);
                switch (bullet.height){
                    case 1:
                        BulletListDown.add(bullet);
                        break;
                    case 2:
                        BulletListUp.add(bullet);
                        break;
                }
            }
            switch (this.height){
                case 1:
                    BulletListDown.remove(this);
                    break;
                case 2:
                    BulletListUp.remove(this);
                    break;
            }
            Main.BulletList.remove(this);
        }
    }
    protected final void clearClient(){
        if(this.clear_sost){
            if(BangSpawn) {
                Main.BangList.add(new Bang(this.x, this.y, 4));
            }
            if(FlameSpawn) {spawn_flame();}
            if(AcidSpawn){spawn_acid();}
            switch (this.height){
                case 1:
                    BulletListDown.remove(this);
                    break;
                case 2:
                    BulletListUp.remove(this);
                    break;
            }
            Main.BulletList.remove(this);
        }
    }
    public void all_action(){
//        this.update();
        this.functionalList.FunctionalIterationAnHost(this);
        this.bull_move_xy();
        this.CorpusBullet();
        this.BuildBulletCollision();
        this.bullClearTime();
        this.clear();

    }
    public void all_action_client(){
//        this.update();
        this.functionalList.FunctionalIterationAnClient(this);
        this.bull_move_xy();
        this.CorpusBulletClient();
        this.BuildBulletCollision();
        this.bullClearTime();
        this.clearClient();
    }
    public void update(){
        updaterBullet.Update(this);
    }
    protected final void bullClearTime(){
        time -= TimeGlobalBullet;
        if(time<0){this.clear_sost = true;}
    }
    protected void BuildBulletCollision(){
        yMap = (int) (y/ Main.width_block)-1;
        xMap = (int) (x/width_block)-1;
        if(xMap>-1&yMap>-1&xMap< Main.xMap&yMap<Main.yMap) {
            if (BlockList2D.get(yMap).get(xMap).passability & height == 1){this.clear_sost = true;
                if(FlameSpawn) {
                    BuildingList.get(BlockList2D.get(yMap).get(xMap).iBuilding).time_flame += 10;
                }
            }}
        else{this.clear_sost = true;}
    }
    public void PackBulletFragment(Bullet bullet){
        BullPacket pack = new BullPacket();
        pack.x = bullet.x;
        pack.y = bullet.y;
        pack.team = bullet.type_team;
        pack.rotation = bullet.rotation;
        pack.time = bullet.time;
        pack.speed = bullet.speed;
        pack.height = bullet.height;
        pack.ID = bullet.ID;
        PacketBull.add(pack);

    }
//    protected final void CorpusBullet(){
//        for(int i = 0;i< UnitList.size();i++) {
//            Unit unit = UnitList.get(i);
//            if (this.type_team != unit.team & rect_bull((int)unit.x,(int)unit.y,(int)unit.corpus_width,
//                   (int)unit.corpus_height,(int)this.x,(int)this.y,this.size,-unit.rotation_corpus)) {
//               unit.time_trigger_bull_bot = unit.time_trigger_bull;
//               armor_damage(unit);
//               unit.green_len = ((float) unit.hp / unit.max_hp) * Option.size_x_indicator;
//               return;
//           }
//        }
//        for(int i = 0;i< DebrisList.size();i++) {
//            Unit unit = DebrisList.get(i);
//            if (rect_bull((int)unit.x, (int)unit.y,(int)unit.corpus_width,(int)unit.corpus_height,(int)this.x,(int)this.y,
//                    this.size,-unit.rotation_corpus)) {
//                unit.time_trigger_bull_bot = unit.time_trigger_bull;
//                armor_damage(unit);
//                unit.green_len = ((float) unit.hp / unit.max_hp) * Option.size_x_indicator;
//                return;
//            }
//        }
//    }
    protected final void CorpusBullet(){
        for(int i = 0;i< UnitList.size();i++) {
            Unit unit = UnitList.get(i);
            if (this.type_team != unit.team & abs(unit.XMap - this.xMap) < 3 & abs(unit.YMap - this.yMap) < 3) {
                if (this.type_team != unit.team & rect_bull((int) unit.x, (int) unit.y, (int) unit.corpus_width,
                        (int) unit.corpus_height, (int) this.x, (int) this.y, this.size, -unit.rotation_corpus)) {
                    armor_damage(unit);
                    unit.green_len = ((float) unit.hp / unit.max_hp) * Option.size_x_indicator;
                    return;
                }
            }
        }
        for(int i = 0;i< DebrisList.size();i++) {
            Unit unit = DebrisList.get(i);
            if (abs(unit.XMap - this.xMap) < 3 & abs(unit.YMap - this.yMap) < 3) {
                if (rect_bull((int) unit.x, (int) unit.y, (int) unit.corpus_width, (int) unit.corpus_height, (int) this.x, (int) this.y,
                        this.size, -unit.rotation_corpus)) {
                    armor_damage(unit);
                    //unit.green_len = ((float) unit.hp / unit.max_hp) * Option.size_x_indicator;
                    return;
                }
            }
        }
    }
    protected final void CorpusBulletClient(){
        for(int i = 0;i< UnitList.size();i++) {
            Unit unit = UnitList.get(i);
            if (this.type_team != unit.team & abs(unit.XMap - this.xMap) < 3 & abs(unit.YMap - this.yMap) < 3) {
                if (rect_bull((int) unit.x, (int) unit.y, (int) unit.corpus_width,
                        (int) unit.corpus_height, (int) this.x, (int) this.y, this.size, -unit.rotation_corpus)) {
                    this.clear_sost = true;
                    return;
                }
            }
        }
        for(int i = 0;i< DebrisList.size();i++) {
            Unit unit = DebrisList.get(i);
            if (abs(unit.XMap - this.xMap) < 3 & abs(unit.YMap - this.yMap) < 3) {
                if (rect_bull((int) unit.x, (int) unit.y, (int) unit.corpus_width, (int) unit.corpus_height, (int) this.x, (int) this.y,
                        this.size, -unit.rotation_corpus)) {
                    this.clear_sost = true;
                    return;
                }
            }
        }
    }
    protected final void armor_damage(Unit unit){
        unit.hp -= (int) (this.damage-((this.damage/100*(unit.armor-this.penetration))));
        unit.t += this.t_damage;
        this.clear_sost = true;
    }

    protected final boolean rect_bull(int x1,int y1,int width,int height,int x,int y,int size,double rotation){
        Rectangle2D rect1 = new Rectangle2D.Double(x1,y1,width,height);
        AffineTransform transform1 = new AffineTransform();
        transform1.rotate(Math.toRadians(-rotation), rect1.getCenterX(), rect1.getCenterY());
        Area area1 = new Area(rect1);
        area1.transform(transform1);

        Ellipse2D circle = new Ellipse2D.Double(x,y,size,size);
        return area1.intersects(circle.getBounds2D());
    }
}
