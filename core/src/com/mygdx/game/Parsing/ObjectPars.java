package com.mygdx.game.Parsing;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mygdx.game.object_map.MapObject;
import com.mygdx.game.unit.ClassUnit;
import com.mygdx.game.unit.UnitPattern;

import java.io.*;
import java.util.ArrayList;

public class ObjectPars {

    public static void Pars() {
        FileHandle[] files = Gdx.files.internal("ContentGlobal/ObjectMap").list();
        System.out.println(files.length);
        if (files.length == 0) {
            AddBuilding();
            files = Gdx.files.internal("ContentGlobal/ObjectMap").list();
        }
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

        new MapObject(Image, width, height,HP,X, Y, Collision,LightingConf,Lighting,SpawnUnit
                ,file.name().replace(".json", ""));


    }
    public static void AddBuilding(){
        new File("ContentGlobal").mkdirs();
        new File("ContentGlobal/ObjectMap").mkdirs();
        File Pz2A = new File("ContentGlobal/ObjectMap/pepper.json");
        String data = "{\n" +
                "  \"Image\": \"pepper_object_map\",\n" +
                "  \"Collision\":\"CollisionBreak\",\n" +
                "  \"X\":2,\n" +
                "  \"Y\":2,\n" +
                "  \"width\":20,\n" +
                "  \"height\":20,\n" +
                "  \"HP\":120,\n" +
                "  \"LightingConf\":true,\n" +
                "  \"Lighting\":600,\n" +
                "  \"SpawnUnit\": false\n" +
                "}";
        Create(Pz2A,data);


    }
    private static void Create(File file, String str){
        try {
            file.createNewFile();
        } catch (IOException ignored) {
        }
        try {
            PrintWriter out = new PrintWriter(file);
            out.println(str);
            out.close();
        } catch (IOException ignored) {
        }

    }


}
