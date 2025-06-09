package com.mygdx.game.build;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Shader.LightingMainSystem;
import com.mygdx.game.block.Block;
import com.mygdx.game.main.Main;
import static Data.DataColor.*;


import com.mygdx.game.method.rand;
import Content.Particle.FlameStatic;
import com.mygdx.game.particle.Particle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import static com.mygdx.game.main.Main.LightSystem;


public abstract class Building implements Serializable {
    public int width,height,x,y,time_flame,width_2,height_2,x_rend,y_rend,width_render,height_render,brightness_max = 240,brightness;
    public static float[]rgb = {(float)1/255 * 236, (float) 1/255 * 124, (float) 1/255 * 38};
    //public float[]rgb = {(float)1/255 * rand.rand(20,250), (float) 1/255 * rand.rand(20,250), (float) 1/255 * rand.rand(20,250)};
    public Sprite build_image;
    private int distance_light,density_light_x,density_light_y;
    public ArrayList<int[]>xy_light = new ArrayList<>();
    public ArrayList<int[]>xy_light_render = new ArrayList<>();
    public ArrayList<LightingMainSystem.Light>Lighting = new ArrayList<>();
    public BuildType name;
    public boolean[][]ConstructBuilding;
    public int xMatrix,yMatrix;
    public UpdateBuilding RenderBuilding;
    public int RightTopPointX,RightTopPointY;



    protected void Data(){
        DataCollision();
        distance_light = 200;
        density_light_y=(int)((double)height/distance_light);
        density_light_x=(int)((double)width/distance_light);
        size_light();
    }
    private void DataCollision(){
        int ConstructX = ConstructBuilding[0].length;
        int ConstructY =ConstructBuilding.length;
        this.width = Main.width_block*ConstructX;
        this.height = Main.height_block*ConstructY;
        xMatrix = this.x/Main.width_block;
        yMatrix = this.y/Main.height_block;
        this.x = Main.BlockList2D.get(yMatrix).get(xMatrix).x;
        this.y = Main.BlockList2D.get(yMatrix).get(xMatrix).y;
        RightTopPointX = xMatrix +ConstructX;
        RightTopPointY = yMatrix +ConstructY;
        this.width_2 = this.width/2;
        this.height_2 = this.height/2;
    }
    private void size_light(){
        int x_light = x;
        int y_light = y;
        for(int i = 0;i<density_light_x;i++){
            x_light += distance_light;
            for(int j = 0;j<density_light_y;j++){
                y_light += distance_light;
                //System.out.println(x_light+" "+y_light);
                LightingMainSystem.Light light = LightSystem.addLight().set(x_light,y_light
                        ,new Color(RGBFlame[0],RGBFlame[1],RGBFlame[2],0.3f),
                        2f,420,0.2f);
                light.work = false;
                Lighting.add(light);
//                LightSystem.addLight().set(this.x,this.y,new Color(RGBFlame[0],RGBFlame[1]
//                        ,RGBFlame[2],0.3f),3.2f,420,0.2f);
            }

            y_light = y;
        }
    }
    public void all_action(int i){
        this.update();
    }
    public void iteration_light_build(){
        for (int[] ints : xy_light) {
            xy_light_render.add(Main.RC.WindowSynchronization(ints[0], ints[1]));
        }
    }
    public void update(){
    }
    public void center_render(){
        int[]xy = Main.RC.render_objZoom(this.x,this.y);
        this.x_rend = xy[0];
        this.y_rend = xy[1];
    }
    public static int[] center_render(int x,int y){
        return Main.RC.WindowSynchronization(x,y);
    }


    public void flame_build(LinkedList<Particle> part){
        if(this.time_flame>0){
            //iteration_light_build();
//            for (int[] ints : xy_light_render) {
//                Block.LightingAir((int) (ints[0] * Main.Zoom), (int) (ints[1] * Main.Zoom), rgb);
//            }
            for(LightingMainSystem.Light light : Lighting){
                light.work = true;
            }
            this.brightness = brightness_max;
            if(rand.rand(4)== 1) {
                int z = rand.rand(4);
                this.time_flame -= 1;
                    switch (z) {
                        case 0:{
                            part.add(new FlameStatic(this.x + rand.rand(this.width), this.y + this.height));break;}
                        case 1:{
                            part.add(new FlameStatic(this.x + rand.rand(this.width), this.y));break;}
                        case 2:{
                            part.add(new FlameStatic(this.x + this.width, this.y + rand.rand(height)));break;}
                        case 3:{
                            part.add(new FlameStatic(this.x, this.y + rand.rand(height)));break;}
                    }
            }
        }
        else{
            for(LightingMainSystem.Light light : Lighting){
                light.work = false;
            }
            this.brightness = 0;
        }
    }

}
