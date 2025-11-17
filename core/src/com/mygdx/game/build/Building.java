package com.mygdx.game.build;
import Content.Particle.FlameParticle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.Shader.LightingMainSystem;
import com.mygdx.game.main.Main;
import static Data.DataColor.*;


import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.method.rand;
import Content.Particle.FlameStatic;

import java.io.Serializable;
import java.util.ArrayList;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.FunctionalComponent.FunctionalBuilding.FunctionalComponentBuildingRegister.FlameBuild;
import static com.mygdx.game.main.Main.*;


public class Building implements Serializable,Cloneable {
    public int width,height,x,y,time_flame,width_2,height_2,x_rend,y_rend,width_render,height_render,brightness_max = 240,brightness,
    rotate,width_render2,height_render2;
    public String build_image;
    public int density_light_x,density_light_y;
    public static int distance_light = width_block_render*2;
    public ArrayList<int[]>xy_light = new ArrayList<>();
    public ArrayList<int[]>xy_light_render = new ArrayList<>();
    public ArrayList<LightingMainSystem.Light>Lighting = new ArrayList<>();
    public String name;
    public String ID;
    public boolean[][]ConstructBuilding;
    public int xMatrix,yMatrix;
    public int RightTopPointX,RightTopPointY;
    public FunctionalList ListFunc;
    public static int SpawnMaxTime = 600,SpawnTotalTime;
    public byte team;
    public Building(int x, int y, String ID, boolean[][]Struct, String Asset, FunctionalList List){
        name = ID;
        this.x = x;
        this.y = y;
        this.ListFunc = List;
        //RenderBuilding = Main.BuildingRegister.Update_big_build_wood1;
        this.build_image = Asset;
        this.ID = ID;
        ConstructBuilding = Struct;
    }
    public Building(int x, int y, String ID, boolean[][]Struct, String Asset, boolean FlameConf){
        name = ID;
        this.x = x;
        this.y = y;
        this.ListFunc = new FunctionalList();
        if(FlameConf) {
            this.ListFunc.Add(FlameBuild);
        }

        //RenderBuilding = Main.BuildingRegister.Update_big_build_wood1;
        this.build_image = Asset;
        this.ID = ID;
        ConstructBuilding = Struct;
    }
    public Building(int x, int y,byte team){
        this.x = x;
        this.y = y;
        this.team = team;
        //RenderBuilding = Main.BuildingRegister.Update_big_build_wood1;
        ConstructBuilding = new boolean[][]{{false}};

    }
    public Building BuildingCreate(int x,int y,int rotation) {
        Building building = this.clone();
        building.Lighting = new ArrayList<>();
//        building.Lighting.addAll(this.Lighting);
        building.x = x;
        building.y = y;
        building.DataBuilding();
        switch(rotation) {
            case 0:{
                break;
            }
            case 1:{
                building.Rotate90();
                building.rotate = 90;
                break;
            }
            case 2:{
                building.Rotate90();
                building.Rotate90();
                building.rotate =180;
                break;

            }
            case 3:{
                building.Rotate90();
                building.Rotate90();
                building.Rotate90();
                building.rotate =270;
                break;
            }
        }
        building.Data();
        //building.rotate = 90;

//        for(LightingMainSystem.Light light : Lighting){
//            light.work = false;
//        }
        //System.out.println(building.ID);
        return building;
    }



    protected void Data(){
        DataCollision();
        for (FunctionalComponent comp : ListFunc.functional){
            comp.FunctionalCreateBuildData(this);
        }

    }
    public void DataBuilding(){
        int ConstructX = ConstructBuilding[0].length;
        int ConstructY =ConstructBuilding.length;
        this.width = width_block*ConstructX+2;
        this.height = width_block*ConstructY+2;
    }
    private void DataCollision(){
        int ConstructX = ConstructBuilding[0].length;
        int ConstructY =ConstructBuilding.length;
//        this.width = Main.width_block*ConstructX;
//        this.height = Main.width_block*ConstructY;
        xMatrix = this.x/ width_block;
        yMatrix = this.y/ width_block;
        this.x = Main.BlockList2D.get(yMatrix).get(xMatrix).x;
        this.y = Main.BlockList2D.get(yMatrix).get(xMatrix).y;
        RightTopPointX = xMatrix +ConstructX;
        RightTopPointY = yMatrix +ConstructY;
        this.width_2 = this.width/2;
        this.height_2 = this.height/2;
    }
    public void size_light(){
        int ConstructX = ConstructBuilding[0].length;
        int ConstructY =ConstructBuilding.length;
        density_light_y=(int)((double)ConstructY*width_block/distance_light);
        density_light_x=(int)((double)ConstructX*width_block/distance_light);
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
    public void all_action(){
        update();
        ListFunc.FunctionalIterationAnHost(this);
    }
    public void Rotate90(){
        //this.rotate = 90;
//        int width = this.width;
//        int height = this.height;
//        this.width = height;
//        this.height = width;
        boolean[][]ConstructBuildingBuffer = new boolean[ConstructBuilding[0].length][ConstructBuilding.length];
        for(int i = 0 ;i<ConstructBuilding.length;i++) {
            for (int i2 = 0; i2 < ConstructBuilding[0].length; i2++) {
                ConstructBuildingBuffer[i2][i] = ConstructBuilding[i][i2];
            }
        }
        ConstructBuilding = ConstructBuildingBuffer;
    }

    public void SpawnUnit(){


    }
    public void iteration_light_build(){
        for (int[] ints : xy_light) {
            xy_light_render.add(Main.RC.WindowSynchronization(ints[0], ints[1]));
        }
    }
    public void update(){
        int[] xy = Building.center_render(x,y);
//        Sprite spr = TextureAtl.createSprite(build_image);
//        spr.setRotation(rotate);
        switch(rotate) {
            case 0:{
                RenderMethod.transorm_img((int) ((xy[0])* Main.Zoom),(int) (xy[1]* Main.Zoom)
                        ,width_render,height_render,rotate,TextureAtl.createSprite(build_image));
                break;
            }
            case 90:{
                RenderMethod.transorm_img((int) ((xy[0])* Main.Zoom),(int) (xy[1]* Main.Zoom)
                        ,width_render,height_render,rotate,TextureAtl.createSprite(build_image),height_render2,height_render2);
                break;
            }
            case 180:{
                RenderMethod.transorm_img((int) ((xy[0])* Main.Zoom),(int) (xy[1]* Main.Zoom)
                        ,width_render,height_render,rotate,TextureAtl.createSprite(build_image),width_render2,height_render2);
                break;

            }
            case 270:{
                RenderMethod.transorm_img((int) ((xy[0])* Main.Zoom),(int) (xy[1]* Main.Zoom)
                        ,width_render,height_render,rotate,TextureAtl.createSprite(build_image),width_render2,width_render2);
                break;
            }
        }



    }
    public void center_render(){
        int[]xy = Main.RC.render_objZoom(this.x,this.y);
        this.x_rend = xy[0];
        this.y_rend = xy[1];
    }
    public static int[] center_render(int x,int y){
        return Main.RC.WindowSynchronization(x,y);
    }


    public void flame_build(){
        if(this.time_flame>0){
            this.time_flame -= 1;
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
                    switch (z) {
                        case 0:{
                            Main.FlameParticleList.add(new FlameParticle(this.x + rand.rand(this.width), this.y + this.height));break;}
                        case 1:{
                            Main.FlameParticleList.add(new FlameParticle(this.x + rand.rand(this.width), this.y));break;}
                        case 2:{
                            Main.FlameParticleList.add(new FlameParticle(this.x + this.width, this.y + rand.rand(height)));break;}
                        case 3:{
                            Main.FlameParticleList.add(new FlameParticle(this.x, this.y + rand.rand(height)));break;}
                    }
            }
        }
        else {
            for(LightingMainSystem.Light light : Lighting){
                light.work = false;
            }
            this.brightness = 0;
        }
    }

    @Override final
    public Building clone() {
        try {
            Building clone = (Building) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
