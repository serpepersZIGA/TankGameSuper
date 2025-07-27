package com.mygdx.game.Parsing;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.mygdx.game.MapFunction.MapBaseAdd;
import com.mygdx.game.unit.ClassUnit;
import com.mygdx.game.unit.UnitPattern;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        if (files.length == 0) {
            //AddBuilding();
            files = Gdx.files.internal("ContentGlobal/Unit").list();
        }
        for (FileHandle file : files) {
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

        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(JSON))) {
            result.append(br.readLine());

        } catch (IOException e) {
            e.printStackTrace();
            MapBaseAdd.AddMap();
            try {
                BufferedReader br = new BufferedReader(new FileReader(JSON));
                result.append(br.readLine());
            } catch (IOException ignored) {
            }
        }
        FileHandle file = Gdx.files.internal(JSON);



        String TxT = file.readString();
        // Чтение JSON-файла и создание объекта Person
        ObjectMapper objectMapper = new ObjectMapper();


        // Преобразуем строку JSON в JsonNode
        JsonNode jsonNode = objectMapper.readTree(TxT);
        //Motor = jsonNode.get("Engine").asText();

        //Corpus = jsonNode.get("Corpus").asText();
        buff obj = objectMapper.readValue(TxT, buff.class);  ;
        Cannon = new ArrayList<>();
        Cannon.addAll(obj.Cannon);
        Engine = obj.Engine;
        Corpus = obj.Corpus;
        TowerXY = obj.TowerXY;
        medic_help = obj.MedicConf;

        //for (String node : strings) {

        //}
        //Cannon.addAll(strings);
        //Cannon.add("ZX");

//        List<String> cannon = jsonNode.findValuesAsText("Cannon");
//        System.out.println(cannon);
//        Cannon = new ArrayList<>(cannon);
        //List<String> towerXY  = jsonNode.readValue("TowerXY");
        //System.out.println(towerXY);



        //int age = jsonNode.get("age").asInt();

        // Вывод данных на экран
//            System.out.println("Name: " + person.getName());
//            System.out.println("Age: " + person.getAge());
//            System.out.println("Email: " + person.getEmail());


    }


}
