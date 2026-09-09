package com.mygdx.game.Parsing;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import tools.jackson.databind.ObjectMapper;

import com.mygdx.game.object_map.MapObject;

import java.io.*;
import java.util.ArrayList;

public class ObjectPars {

    public static void Pars() {
        FileHandle[] files = Gdx.files.internal("ContentGlobal/ObjectMap").list();
        System.out.println(files.length);
        for (FileHandle file : files) {
            System.out.println(file.name());
            //System.out.println(file.path());
            try {
                JSON(file.path());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }



        }


    }


    public static void JSON(String JSON) throws IOException {
        FileHandle file = Gdx.files.internal(JSON);
        String TxT = file.readString();
        // Чтение JSON-файла и создание объекта Person
        ObjectMapper objectMapper = new ObjectMapper();

        buffObjMap obj = objectMapper.readValue(TxT, buffObjMap.class);
        String Image = obj.Image;
        String Collision = obj.Collision;
        int X = obj.X;
        int Y = obj.Y;
        int HP = obj.HP;
        int width = obj.width;
        int height = obj.height;
        int Lighting = obj.Lighting;
        boolean LightingConf = obj.LightingConf;
        boolean SpawnUnit = obj.SpawnUnit;
        boolean PlayerSpawn = obj.PlayerSpawn;
        int LightOffsetY = obj.LightOffsetY;
        int LightOffsetX = obj.LightOffsetX;
        String CrushSound = obj.CrushSound;
        boolean Solid = obj.Solid;
        boolean Metallic = obj.Metallic;

        new MapObject(Image, width, height,HP,X, Y, Collision,LightingConf,Lighting,SpawnUnit,PlayerSpawn
                ,file.name().replace(".json", ""),LightOffsetY,LightOffsetX,CrushSound,Solid,Metallic);


    }
}
