package com.mygdx.game.object_map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.MapFunction.MapBaseAdd;
import com.mygdx.game.main.Main;
import com.mygdx.game.object_map.MapObject;
import com.mygdx.game.object_map.ObjectMapAssets;
import com.mygdx.game.object_map.component_collision_system.CollisionBreak;
import com.mygdx.game.object_map.component_collision_system.CollisionSlow;
import com.mygdx.game.object_map.component_collision_system.CollisionVoid;
import com.mygdx.game.object_map.component_collision_system.ComponentCollisionSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static com.mygdx.game.MapFunction.MapScan.AsphaltSpawn;
import static com.mygdx.game.MapFunction.MapScan.dataIntilization;
import static com.mygdx.game.main.Main.ContentImage;

public class ObjectLoad {
    public static void MapSpawnObject(String Build, int x, int y, ArrayList<Object> objList) {
        ComponentCollisionSystem col = null;
        boolean conf = false;
        Sprite sprite = null;
        ObjectMapAssets assets = null;
         if (objList.get(6).equals("CollisionBreak")) {
             col = new CollisionBreak((Integer) objList.get(0) + x * Main.width_block,
                     (Integer) objList.get(1) + y * Main.height_block
                     , (Integer) objList.get(3), (Integer) objList.get(4));
         } else if (objList.get(6).equals("CollisionVoid")) {
             col = new CollisionVoid();
         } else if (objList.get(6).equals("CollisionSlow")) {
             col = new CollisionSlow((Integer) objList.get(0) + x * Main.width_block,
                     (Integer) objList.get(1) + y * Main.height_block
                     , (Integer) objList.get(3), (Integer) objList.get(4));
         }

         if (objList.get(7).equals("true")) {
             conf = true;
         } else if (objList.get(7).equals("false")) {
             conf = false;
         }


         if (objList.get(2).equals("pepper_object_map")) {
             sprite = Main.ContentImage.pepper_object_map;
             assets = ObjectMapAssets.pepper;
         } else if (objList.get(2).equals("big_build_wood_1")) {
             sprite = Main.ContentImage.big_build_wood_1;
             assets = ObjectMapAssets.building;
         }

         switch (Build) {
             case "ObjectMap": {
                 Main.BlockList2D.get(y).get(x).objMap = new MapObject((Integer) objList.get(0),
                         (Integer) objList.get(1), sprite, (Integer) objList.get(3),
                         (Integer) objList.get(4), (Integer) objList.get(5), x, y, col, conf, (Integer) objList.get(8), assets);

             }
             break;
             case "Asphalt": {
                 AsphaltSpawn(x, y);
             }
         }
    }
    public static void MapSpawnObject2(String Build, int x, int y, ArrayList<Object> objList) {
        ComponentCollisionSystem col = null;
        boolean conf = false;
        Sprite sprite = null;
        ObjectMapAssets assets = null;
        if (objList.get(6).equals("CollisionBreak")) {
            col = new CollisionBreak((Integer) objList.get(0) + x * Main.width_block,
                    (Integer) objList.get(1) + y * Main.height_block
                    , (Integer) objList.get(3), (Integer) objList.get(4));
        } else if (objList.get(6).equals("CollisionVoid")) {
            col = new CollisionVoid();
        } else if (objList.get(6).equals("CollisionSlow")) {
            col = new CollisionSlow((Integer) objList.get(0) + x * Main.width_block,
                    (Integer) objList.get(1) + y * Main.height_block
                    , (Integer) objList.get(3), (Integer) objList.get(4));
        }

        if (objList.get(7).equals("true")) {
            conf = true;
        } else if (objList.get(7).equals("false")) {
            conf = false;
        }


        if (objList.get(2).equals("pepper_object_map")) {
            sprite = Main.ContentImage.pepper_object_map;
            assets = ObjectMapAssets.pepper;
        } else if (objList.get(2).equals("big_build_wood_1")) {
            sprite = Main.ContentImage.big_build_wood_1;
            assets = ObjectMapAssets.building;
        }

        switch (Build) {
            case "ObjectMap": {
                Main.BlockList2D.get(y).get(x).objMap = new MapObject((Integer) objList.get(0),
                        (Integer) objList.get(1), sprite, (Integer) objList.get(3),
                        (Integer) objList.get(4), (Integer) objList.get(5), x, y, col, conf, (Integer) objList.get(8), assets);

            }
            break;
            case "Asphalt": {
                AsphaltSpawn(x, y);
            }
        }
    }
    public static Sprite ImageLoad(ObjectMapAssets assets){
        Sprite img = null;
        switch (assets){
            case pepper:{img = ContentImage.pepper_object_map;}
            break;
            case building:{img = ContentImage.big_build_wood_1;}
            break;
        }
        return img;

    }
}
