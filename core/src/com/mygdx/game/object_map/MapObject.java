package com.mygdx.game.object_map;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.Network.PacketMapObject;
import com.mygdx.game.Shader.LightingMainSystem;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.object_map.component_collision_system.ComponentCollisionSystem;

import java.util.ArrayList;

import static Data.DataColor.RGBFlame;
import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.LightSystem;

public class MapObject {
    public static ArrayList<PacketMapObject> PacketMapObjects = new ArrayList<>();
    public int width,height,hp,y,x;
    public int width_render,height_render;
    public String img;
    public float distance_lighting,distance_lighting_2;
    public boolean lighting;
    public ComponentCollisionSystem Collision;
    public String assets;
    public LightingMainSystem.Light light;
    public MapObject(){
    }
    public MapObject(int x, int y, String img, int width, int height, int hp, int ix, int iy,
        ComponentCollisionSystem collision,boolean lighting,float distance_lighting,String assets){
        this.x = x+ix*Main.width_block;
        this.y = y+iy*Main.height_block;
        this.width = width;
        this.lighting = lighting;
        this.height = height;
        this.width_render = (int) (width*Main.Zoom);
        this.height_render = (int) (height*Main.Zoom);
        this.distance_lighting = distance_lighting;
        this.hp = hp;
        this.img = img;
        this.assets = assets;
        if(lighting){
            light = LightSystem.addLight().set(this.x,this.y,new Color(RGBFlame[0],RGBFlame[1],RGBFlame[2],0.3f),
                    4f,distance_lighting,0.2f);
            //light.radius = distance_lighting;

        }
        //center_render();

        Collision = collision;
    }
    public void render(){
        int[]xy = Main.RC.render_objZoom(this.x,this.y);
        //if(lighting)Block.LightingAirObject(xy[0],xy[1],RGBFlame,distance_lighting*Main.Zoom);

        RenderMethod.transorm_img(xy[0],xy[1],width_render,height_render,TextureAtl.createSprite(img));
    }
}
