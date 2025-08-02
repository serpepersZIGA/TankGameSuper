package com.mygdx.game.Parsing;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.mygdx.game.MapFunction.MapBaseAdd;
import com.mygdx.game.unit.ClassUnit;
import com.mygdx.game.unit.UnitPattern;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UnitsParser {
    public static String Engine, Corpus, Soldat;
    public static ArrayList<String> Cannon;
    public static int [][] TowerXY;
    public static ClassUnit classUnit;
    public static int medic_help;

    public static void Pars() {
        FileHandle[] files = Gdx.files.internal("ContentGlobal/Unit").list();
        System.out.println(files.length);
        if (files.length == 0) {
            AddBuilding();
            files = Gdx.files.internal("ContentGlobal/Unit").list();
        }
        for (FileHandle file : files) {
            System.out.println(file.name());
            //System.out.println(file.path());
            try {
                JSON(file.path());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            for (int i = file.name().length()-4;i<file.name().length();i++){
//                Name = file.name().replace(".bld");
//            }
//            StructBuffer = new boolean[][]{{true,true},
//                    {true,true}};

            new UnitPattern(file.name().replace(".json", ""),
                    Corpus, Engine, Cannon, TowerXY, classUnit, medic_help);


            //List.Clear();
            //System.out.println(BuildRegister.BuildingID.size());

        }


    }


    public static void JSON(String JSON) throws IOException {
        FileHandle file = Gdx.files.internal(JSON);
        String TxT = file.readString();
        // Чтение JSON-файла и создание объекта Person
        ObjectMapper objectMapper = new ObjectMapper();

        buff obj = objectMapper.readValue(TxT, buff.class);  ;
        Cannon = new ArrayList<>();
        Cannon.addAll(obj.Cannon);
        Engine = obj.Engine;
        Corpus = obj.Corpus;
        TowerXY = obj.TowerXY;
        medic_help = obj.MedicConf;
    }
    public static void AddBuilding(){
        new File("ContentGlobal").mkdirs();
        new File("ContentGlobal/Unit").mkdirs();
        File Pz2A = new File("ContentGlobal/Unit/Pz2A.json");
        File Pz2AC = new File("ContentGlobal/Unit/Pz2AC.json");
        File Pz2F = new File("ContentGlobal/Unit/Pz2F.json");
        File Pz2M = new File("ContentGlobal/Unit/Pz2M.json");
        String data = "{\n" +
                "  \"Engine\": \"V2A\",\n" +
                "  \"Corpus\": \"Panzer1\",\n" +
                "  \"Cannon\": [\"Kwk12ML\",\"Flk4CL\",\"Kwk12M\"],\n" +
                "  \"TowerXY\": [[-12,52],[12,52],[1,18]],\n" +
                "  \"MedicConf\": 0\n" +
                "}";
        Create(Pz2A,data);
        data = "{\n" +
                "  \"Engine\": \"V2A\",\n" +
                "  \"Corpus\": \"Panzer1\",\n" +
                "  \"Cannon\": [\"Ack2AL\",\"Ack2AL\",\"Ack2A\"],\n" +
                "  \"TowerXY\": [[-12,52],[12,52],[1,18]],\n" +
                "  \"MedicConf\": 0\n" +
                "}";
        Create(Pz2AC,data);
        data = "{\n" +
                "  \"Engine\": \"V2A\",\n" +
                "  \"Corpus\": \"Panzer1\",\n" +
                "  \"Cannon\": [\"Flk4CL\",\"Flk4CL\",\"Flk4C\"]," +
                "  \"TowerXY\": [[-12,52],[12,52],[1,18]],\n" +
                "  \"MedicConf\": 0\n" +
                "}";
        Create(Pz2F,data);
        data = "{\n" +
                "  \"Engine\": \"V2A\",\n" +
                "  \"Corpus\": \"Panzer1\",\n" +
                "  \"Cannon\": [\"Kwk12ML\",\"Flk4CL\",\"Kwk12M\"]," +
                "  \"TowerXY\": [[-12,52],[12,52],[1,18]],\n" +
                "  \"MedicConf\": 0\n" +
                "}";
        Create(Pz2M,data);


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
