package com.mygdx.game.object_map;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.Inventory.Inventory;
import com.mygdx.game.Inventory.Item;
import com.mygdx.game.Network.PacketMapObject;
import com.mygdx.game.Shader.LightingMainSystem;
import com.mygdx.game.block.Block;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.method.rand;
import com.mygdx.game.object_map.component_collision_system.CollisionBreak;
import com.mygdx.game.object_map.component_collision_system.CollisionSlow;
import com.mygdx.game.object_map.component_collision_system.CollisionVoid;
import com.mygdx.game.object_map.component_collision_system.ComponentCollisionSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static Data.DataColor.RGBFlame;
import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.unit.Unit.IDList;

public class MapObject implements Cloneable{
    public static ArrayList<PacketMapObject> PacketMapObjects = new ArrayList<>();
    public static ArrayList<int[]>SpawnerList = new ArrayList<>();
    public int width,height,hp,y,x,ix,iy;
    public int width_render,height_render,id;
    public String img,CollisionBuff;
    public float distance_lighting,distance_lighting_2;
    public boolean lighting,SpawnUnit;
    public ComponentCollisionSystem Collision;
    public String assets;
    public LightingMainSystem.Light light;
    public static HashMap<String,MapObject> ObjectMapIDList=new HashMap<>();
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

    public MapObject(String img, int width, int height, int hp, int ix, int iy,
                     String collision,boolean lighting,float distance_lighting,boolean SpawnUnit,String assets){
        this.ix = ix;
        this.iy = iy;
        this.width = width;
        this.lighting = lighting;
        this.height = height;
        this.width_render = (int) (width*Main.Zoom);
        this.height_render = (int) (height*Main.Zoom);
        this.distance_lighting = distance_lighting;
        this.SpawnUnit = SpawnUnit;
        this.hp = hp;
        this.img = img;
        this.assets = assets;
        System.out.println(assets);
        ObjectMapIDList.put(assets,this);
        CollisionBuff = collision;
        //center_render();
//        switch (collision){
//            case "CollisionBreak":Collision = new CollisionBreak(ix,iy,width,height);
//            case "CollisionSlow":Collision = new CollisionSlow(ix,iy,width,height);
//            case "CollisionVoid":Collision = new CollisionVoid();
//        }
    }
    public void MapObjectAdd(int x,int y){
        try {
            BlockList2D.get(y).get(x).objMap = (MapObject) this.clone();
            MapObject obj = BlockList2D.get(y).get(x).objMap;

            obj.x = x*Main.width_block+ix;
            obj.y = y*Main.height_block+iy;

            if(lighting){
                obj.light = LightSystem.addLight().set(obj.x,obj.y
                        ,new Color(RGBFlame[0],RGBFlame[1],RGBFlame[2],0.3f),
                        4f,distance_lighting,0.2f);
                //light.radius = distance_lighting;

            }
            switch (CollisionBuff){
                case "CollisionBreak":obj.Collision = new CollisionBreak(obj.x,obj.y,width,height);
                break;
                case "CollisionSlow":obj.Collision = new CollisionSlow(obj.x,obj.y,width,height);
                    break;
                case "CollisionVoid":obj.Collision = new CollisionVoid();
                    break;
            }
            if(SpawnUnit){
                SpawnerList.add(new int[]{y,x});
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    public void MapObjectAdd(float x,float y){
        try {
            int ix = (int) (x/width_block+1);
            int iy = (int) (y/width_block+1);
            BlockList2D.get(iy).get(ix).objMap = (MapObject) this.clone();
            MapObject obj = BlockList2D.get(iy).get(ix).objMap;
            obj.x = (int) x;
            obj.y = (int) y;
            if(lighting){
                obj.light = LightSystem.addLight().set(obj.x,obj.y
                        ,new Color(RGBFlame[0],RGBFlame[1],RGBFlame[2],0.3f),
                        4f,distance_lighting,0.2f);
                //light.radius = distance_lighting;

            }
            switch (CollisionBuff){
                case "CollisionBreak":obj.Collision = new CollisionBreak(obj.x,obj.y,width,height);
                    break;
                case "CollisionSlow":obj.Collision = new CollisionSlow(obj.x,obj.y,width,height);
                    break;
                case "CollisionVoid":obj.Collision = new CollisionVoid();
                    break;
            }
            if(SpawnUnit){
                SpawnerList.add(new int[]{iy,ix});
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(){
        int[]xy = Main.RC.render_objZoom(this.x,this.y);
        //if(lighting)Block.LightingAirObject(xy[0],xy[1],RGBFlame,distance_lighting*Main.Zoom);

        RenderMethod.transorm_img(xy[0],xy[1],width_render,height_render,TextureAtl.createSprite(img));
    }
    public static void SpawnWave(){
        for(int[] i : SpawnerList){
            Spawn(BlockList2D.get(i[0]).get(i[1]));
        }
    }
    private static void Spawn(Block block){

        if(rand.rand(2) == 1){
            IDList.get("Pz-2M").UnitAdd(block.x,block.y,true, (byte) 2,
                    RegisterControl.controllerBot,new Inventory(new Item[4][4]));
        }
        else{
            IDList.get("Pz-2A").UnitAdd(block.x,block.y,true, (byte) 2,
                    RegisterControl.controllerBot,new Inventory(new Item[4][4]));
        }

    }
}
