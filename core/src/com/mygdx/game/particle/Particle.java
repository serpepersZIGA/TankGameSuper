package com.mygdx.game.particle;

import Content.Particle.Flame;
import Content.Particle.FlameParticle;
import com.mygdx.game.Shader.LightingMainSystem;
import com.mygdx.game.Sound.SoundPlay;
import com.mygdx.game.block.UpdateRegister;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.*;

import java.util.LinkedList;

import static com.mygdx.game.main.Main.LightSystem;
import static com.mygdx.game.method.Option.SoundConst;
import static com.mygdx.game.method.pow2.pow2;
import static java.lang.StrictMath.atan2;
import static java.lang.StrictMath.sqrt;

public abstract class Particle {
    public float x,y,size,size_2,speed_x,speed_y,interval_rise_size,size_3;
    protected float r;
    protected float g;
    protected float b;
    public static float rad;
    protected int time_delete;
    public static int brightness = 200;
    private int sound_time;
    private static final int sound_time_max = 300;
    public static final float r_m =(float)(3.0/255);
    public static final float g_m =(float)(3.0/255);
    public static final float b_m =(float)(1.0/255);
    protected int time_spawn,time_spawn_max;
    public int x_rend,y_rend,size_render;
    public LightingMainSystem.Light light;
    public float[]rgb;

    protected void timer(LinkedList<Particle> obj){
        this.time_delete -= 1;
        if(this.time_delete <= 0){
            obj.remove(this);
        }
    }
    protected void timerFlame(LinkedList<Particle> obj){
        this.time_delete -= 1;
        if(this.time_delete <= 0){
            obj.remove(this);
            LightSystem.removeLight(light);
        }
    }
    protected void spawn_flame(){
        if(Main.flame_spawn_time <=0){
            Main.FlameList.add(new Flame((int)((this.x+ -20+rand.rand(40))),(int)((this.y+-20+rand.rand(40)))));
        }
    }
    protected void sound_play(){
        sound_time +=1;
        if(sound_time_max == sound_time) {
            float[]xy = Main.RC.WindowSynchronization(this.x,this.y);
            rad = 1-((float) sqrt(pow2(xy[0]) + pow2(xy[1]))/SoundConst);
            sound_time = 0;
            if(rad>0) {
                SoundPlay.sound(Main.ContentSound.flame_sound,rad);
            }
        }
    }
    protected final void grass_delete(){
        int ix = (int) (x/Main.width_block)-1;
        int iy = (int) (y/Main.height_block)-1;
        try {
            if(Main.BlockList2D.get(iy).get(ix).render_block == UpdateRegister.GrassUpdate) {
                Main.BlockList2D.get(iy).get(ix).render_block = UpdateRegister.DirtUpdate;
            }
        }
        catch (IndexOutOfBoundsException ignored){
        }
    }
    protected void size_update(){
        this.size_2 = this.size/2;
        this.size_3 = this.size_2-2;
    }
    public void liquid_const(){
        int i = Main.LiquidList.size()-1;
        if(i> 1) {
            int g;
            float r;
            int radius = 0;
            for (int i2 = 0; i2 < Main.LiquidList.size(); i2++) {
                g = (int)sqrt(pow2.pow2(Main.LiquidList.get(i).x - Main.LiquidList.get(i2).x) + pow2.pow2(Main.LiquidList.get(i).y - Main.LiquidList.get(i2).y));
                if(g<Main.LiquidList.get(i).size_2+Main.LiquidList.get(i2).size_2){
                    r = (float) (atan2(Main.LiquidList.get(i).y - Main.LiquidList.get(i2).y, Main.LiquidList.get(i).x - Main.LiquidList.get(i2).x) / 3.14 * 180);
                    Main.LiquidList.get(i).x -= move.move_sin2(7, r);
                    Main.LiquidList.get(i).y -= move.move_cos2(7, r);
                    Main.LiquidList.get(i2).x += move.move_sin2(7, r);
                    Main.LiquidList.get(i2).y += move.move_cos2(7, r);
                    if (radius > g || radius == 0) {
                        if (Main.LiquidList.get(i2).x != Main.LiquidList.get(i).x && Main.LiquidList.get(i2).y != Main.LiquidList.get(i).y) {
                            radius = g;
                        }
                    }
                }
            }
        }

    }
    protected void center_render(){
        float[]xy = Main.RC.render_objZoom(this.x,this.y);
        this.x_rend = (int)xy[0];
        this.y_rend = (int)xy[1];
    }
    protected void size_rise(){
        this.size += this.interval_rise_size;
        this.size_2 = this.size/2;
        this.size_3 = this.size_2/2;
    }
    protected void size_rise_delete(){

        this.size -= this.interval_rise_size;
        if(this.size < 4){
            Main.LiquidList.remove(this);
        }
        this.size_2 = this.size/2;
        this.size_3 = this.size_2/2;
    }
    protected void color_fire(){
        if(this.r> 0.1){this.r-= r_m;}
        if(this.g> 0.1){this.g -=g_m;}
        if(this.b> 0.1){this.b -= b_m;}
    }
    protected void move_particle(){
        this.x += this.speed_x;
        this.y += this.speed_y;
    }
    protected void create_flame_particle(LinkedList<Particle>obj){
        this.time_spawn -= 1;
        if(this.time_spawn <= 0){
            obj.add(new FlameParticle(this.x,this.y));
            this.time_spawn = time_spawn_max;
        }
    }
    public void all_action(int i){}
    public void update(){
    }





}
