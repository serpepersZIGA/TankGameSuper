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
import java.util.HashMap;

import static Data.DataColor.RGBFlame;
import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.unit.Unit.IDList;

public class MapObject implements Cloneable{
    public static ArrayList<PacketMapObject> PacketMapObjects = new ArrayList<>();
    public static ArrayList<int[]>SpawnerList = new ArrayList<>();
    // {iy,ix} block-grid points where the local player can spawn/respawn -
    // see ActionMenu.SpawnPlayer(), which picks a random one instead of the
    // old hardcoded (200,200) whenever a map defines any
    public static ArrayList<int[]>PlayerSpawnList = new ArrayList<>();
    public int width,height,hp,y,x,ix,iy;
    public int width_render,height_render;
    public String img,CollisionBuff;
    public float distance_lighting,distance_lighting_2;
    public boolean lighting,SpawnUnit,PlayerSpawn;
    public ComponentCollisionSystem Collision;
    public String assets;
    public LightingMainSystem.Light light;
    public int lightOffsetY,lightOffsetX;
    // name of a Sound field on DataSound to play when CollisionBreak crushes
    // this object (see resolveCrushSound) - null/empty means silent
    public String crushSound;
    // true = a solid obstacle (uses Block.passability, same as buildings)
    // instead of the usual slow-and-pass-through decor collision
    public boolean solid;
    // true = picks the metallic clang variant of the solid-wall impact sound
    public boolean metallic;
    // lazily worked out on first render (terrain isn't painted yet when
    // objects are placed, so this can't be baked in at placement time) -
    // whether a lamp shows its snowed-over sprite instead of the plain one
    private boolean climateChecked;
    private boolean isSnowy;
    public static HashMap<String,MapObject> ObjectMapIDList=new HashMap<>();
    public MapObject(){
    }
    public MapObject(String img, int width, int height, int hp, int ix, int iy,
                     String collision,boolean lighting,float distance_lighting,boolean SpawnUnit,boolean PlayerSpawn,String assets,
                     int lightOffsetY,int lightOffsetX,String crushSound,boolean solid,boolean metallic){
        this.ix = ix;
        this.iy = iy;
        this.width = width;
        this.lighting = lighting;
        this.height = height;
        this.width_render = (int) (width*Main.Zoom);
        this.height_render = (int) (height*Main.Zoom);
        this.distance_lighting = distance_lighting;
        this.SpawnUnit = SpawnUnit;
        this.PlayerSpawn = PlayerSpawn;
        this.hp = hp;
        this.img = img;
        this.assets = assets;
        this.lightOffsetY = lightOffsetY;
        this.lightOffsetX = lightOffsetX;
        this.crushSound = crushSound;
        this.solid = solid;
        this.metallic = metallic;
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
            MapObject obj = (MapObject) this.clone();
            BlockList2D.get(y).get(x).objMap = obj;

            obj.x = x*Main.width_block+ix;
            obj.y = y*Main.width_block +iy;

            if(this.lighting){
                obj.lighting = true;
                obj.light = LightSystem.addLight().set(obj.x+lightOffsetX,obj.y+lightOffsetY
                        ,new Color(RGBFlame[0],RGBFlame[1],RGBFlame[2],0.3f),
                        4f,distance_lighting,0.2f);
                obj.light.isStreetLamp = true;
                //light.radius = distance_lighting;

            }
            switch (this.CollisionBuff){
                case "CollisionBreak":{
                    Object[] sound = resolveCrushSound(this.crushSound);
                    obj.Collision = sound == null ? new CollisionBreak(obj.x,obj.y,width,height)
                            : new CollisionBreak(obj.x,obj.y,width,height,(com.badlogic.gdx.audio.Sound) sound[0],(int) sound[1]);
                    break;
                }
                case "CollisionSlow":obj.Collision = new CollisionSlow(obj.x,obj.y,width,height);
                    break;
                case "CollisionVoid":obj.Collision = new CollisionVoid();
                    break;
            }
            // a solid obstacle uses the same wall-collision system as
            // buildings (Block.passability) instead of the per-object
            // Collision above, which only ever slows/passes through
            if(this.solid){
                BlockList2D.get(y).get(x).passability = true;
            }
            if(this.SpawnUnit){
                SpawnerList.add(new int[]{y,x});
            }
            if(this.PlayerSpawn){
                PlayerSpawnList.add(new int[]{y,x});
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    /** {Sound, id} for a name like "hit"/"break_wooden"/"hit_not_penetration" - see SoundRegister.IDSound. Null if none/unknown. */
    private static Object[] resolveCrushSound(String name){
        if(name == null || name.isEmpty()) return null;
        switch (name){
            case "break_wooden": return new Object[]{ContentSound.break_wooden, 3};
            case "hit": return new Object[]{ContentSound.hit, 7};
            case "hit_not_penetration": return new Object[]{ContentSound.hit_not_penetration, 8};
            default: return null;
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
                obj.light = LightSystem.addLight().set(obj.x+lightOffsetX,obj.y+lightOffsetY
                        ,new Color(RGBFlame[0],RGBFlame[1],RGBFlame[2],0.3f),
                        4f,distance_lighting,0.2f);
                obj.light.isStreetLamp = true;
                //light.radius = distance_lighting;

            }
            switch (CollisionBuff){
                case "CollisionBreak":{
                    Object[] sound = resolveCrushSound(this.crushSound);
                    obj.Collision = sound == null ? new CollisionBreak(obj.x,obj.y,width,height)
                            : new CollisionBreak(obj.x,obj.y,width,height,(com.badlogic.gdx.audio.Sound) sound[0],(int) sound[1]);
                    break;
                }
                case "CollisionSlow":obj.Collision = new CollisionSlow(obj.x,obj.y,width,height);
                    break;
                case "CollisionVoid":obj.Collision = new CollisionVoid();
                    break;
            }
            if(SpawnUnit){
                SpawnerList.add(new int[]{iy,ix});
            }
            if(PlayerSpawn){
                PlayerSpawnList.add(new int[]{iy,ix});
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    // ground ring instead of a sprite - these used to reuse pepper_object_map
    // (a random decor image with nothing to do with spawning) as a placeholder.
    // Drawn here, alongside the terrain in Block.update(), so it sits under
    // everything else (units, buildings) like ground paint should.
    private static final float SPAWN_MARKER_RADIUS = 20f;
    private static final Color PLAYER_SPAWN_MARKER_COLOR = new Color(0.25f, 0.85f, 0.35f, 0.35f);
    private static final Color ENEMY_SPAWN_MARKER_COLOR = new Color(0.85f, 0.25f, 0.25f, 0.35f);
    public void render(){
        int[]xy = Main.RC.render_objZoom(this.x,this.y);
        //if(lighting)Block.LightingAirObject(xy[0],xy[1],RGBFlame,distance_lighting*Main.Zoom);

        if (this.PlayerSpawn || this.SpawnUnit) {
            Main.Render.circle(xy[0], xy[1], SPAWN_MARKER_RADIUS*Main.Zoom,
                    this.PlayerSpawn ? PLAYER_SPAWN_MARKER_COLOR : ENEMY_SPAWN_MARKER_COLOR);
            return;
        }

        String spriteName = img;
        if("lamp".equals(assets)){
            if(!climateChecked){
                isSnowy = com.mygdx.game.MapFunction.ProceduralTerrainPainter.climateAt(this.x, this.y)[0] > 0.5f;
                climateChecked = true;
            }
            boolean lit = com.mygdx.game.method.CycleTimeDay.lightTotal < LightingMainSystem.DAY_THRESHOLD;
            spriteName = (isSnowy ? "Snow-City-light-" : "City-light-") + (lit ? "on" : "off");
        }
        RenderMethod.transorm_img(xy[0],xy[1],width_render,height_render,TextureAtl.createSprite(spriteName));
    }
    public static void SpawnWave(){
        for(int[] i : SpawnerList){
            Spawn(BlockList2D.get(i[0]).get(i[1]));
        }
    }
    private static void Spawn(Block block){

        if(rand.rand(2) == 1){
            IDList.get("Pz-2M").UnitAdd(block.x,block.y,true, (byte) 2,
                    RegisterControl.controllerBot,new Inventory(new Item[4][4],1),new Inventory(new Item[2][2],1));
        }
        else{
            IDList.get("Pz-2A").UnitAdd(block.x,block.y,true, (byte) 2,
                    RegisterControl.controllerBot,new Inventory(new Item[4][4],1),new Inventory(new Item[2][2],1));
        }

    }
}
