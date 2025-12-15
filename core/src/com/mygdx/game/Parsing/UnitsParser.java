package com.mygdx.game.Parsing;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mygdx.game.unit.ClassUnit;
import com.mygdx.game.unit.UnitPattern;

import java.io.*;
import java.util.ArrayList;

public class UnitsParser {
    public static String Engine, Corpus, Soldat;
    public static ArrayList<String> Cannon;
    public static int [][] TowerXY;
    public static ClassUnit classUnit;
    public static int medic_help,height;

    public static void Pars() {
        FileHandle[] files = Gdx.files.internal("ContentGlobal/Unit").list();
        if (files.length == 0) {
            AddBuilding();
            files = Gdx.files.internal("ContentGlobal/Unit").list();
        }
        for (FileHandle file : files) {
            //System.out.println(file.path());
            try {
                JSON(file.path());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            new UnitPattern(file.name().replace(".json", ""),
                    Corpus, Engine, Cannon, TowerXY, classUnit, medic_help,height);


        }


    }


    public static void JSON(String JSON) throws IOException {
        FileHandle file = Gdx.files.internal(JSON);
        String TxT = file.readString();
        // Чтение JSON-файла и создание объекта Person
        ObjectMapper objectMapper = new ObjectMapper();

        buff obj = objectMapper.readValue(TxT, buff.class);
        Cannon = new ArrayList<>();
        Cannon.addAll(obj.Cannon);
        Engine = obj.Engine;
        Corpus = obj.Corpus;
        TowerXY = obj.TowerXY;
        medic_help = obj.MedicConf;
        height = obj.Height;
    }
    public static void AddBuilding(){
        new File("ContentGlobal").mkdirs();
        new File("ContentGlobal/Unit").mkdirs();
        File Pz2A = new File("ContentGlobal/Unit/Pz-2A.json");
        File Pz2AC = new File("ContentGlobal/Unit/Pz-2AC.json");
        File Pz2F = new File("ContentGlobal/Unit/Pz-2F.json");
        File Pz2M = new File("ContentGlobal/Unit/Pz-2M.json");
        File Helicopter2Z = new File("ContentGlobal/Unit/Helicopter-2Z.json");
        String data = "{\n" +
                "  \"Engine\": \"V2A\",\n" +
                "  \"Corpus\": \"HelicopterCorpus1\",\n" +
                "  \"Cannon\": [\"Blade\",\"DP-27\",\"DP-27\"],\n" +
                "  \"TowerXY\": [[0,50],[25,50],[-25,50]],\n" +
                "  \"MedicConf\": 0,\n" +
                "  \"Height\": 2\n" +
                "}";

        Create(Helicopter2Z,data);
        String data = "{\n" +
                "  \"Engine\": \"V2A\",\n" +
                "  \"Corpus\": \"Panzer1\",\n" +
                "  \"Cannon\": [\"Kwk12ML\",\"Flk4CL\",\"Kwk12M\"],\n" +
                "  \"TowerXY\": [[-10,50],[10,50],[1,18]],\n" +
                "  \"MedicConf\": 0\n" +
                "}";

        Create(Pz2A,data);
        data = "{\n" +
                "  \"Engine\": \"V2A\",\n" +
                "  \"Corpus\": \"Panzer1\",\n" +
                "  \"Cannon\": [\"Ack2AL\",\"Ack2AL\",\"Ack2A\"],\n" +
                "  \"TowerXY\": [[-10,50],[10,50],[1,18]],\n" +
                "  \"MedicConf\": 0\n" +
                "}";
        Create(Pz2AC,data);
        data = "{\n" +
                "  \"Engine\": \"V2A\",\n" +
                "  \"Corpus\": \"Panzer1\",\n" +
                "  \"Cannon\": [\"Flk4CL\",\"Flk4CL\",\"Flk4C\"]," +
                "  \"TowerXY\": [[-10,50],[10,50],[1,18]],\n" +
                "  \"MedicConf\": 0\n" +
                "}";
        Create(Pz2F,data);
        data = "{\n" +
                "  \"Engine\": \"V2A\",\n" +
                "  \"Corpus\": \"Panzer1\",\n" +
                "  \"Cannon\": [\"Kwk12ML\",\"Flk4CL\",\"Kwk12M\"]," +
                "  \"TowerXY\": [[-10,50],[10,50],[1,18]],\n" +
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
