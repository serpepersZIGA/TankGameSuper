package com.mygdx.game.Parsing;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mygdx.game.Inventory.Item;
import com.mygdx.game.Inventory.TegItem;
import com.mygdx.game.unit.ClassUnit;
import com.mygdx.game.unit.UnitPattern;

import java.io.*;
import java.util.ArrayList;

public class ParserItem {
    public static String Image;
    public static ArrayList<String> Cannon;
    public static int Armor,Hp,ArmorPercent,HPPercent,DamagePercent,PenetrationPercent
            ,MoveUPPercent,MoveDownPercent, AccelerationPercent;
    public static ClassUnit classUnit;
    public static int medic_help;

    public static void Pars() {
        FileHandle[] files = Gdx.files.internal("ContentGlobal/Item").list();
        if (files.length == 0) {
            AddBuilding();
            files = Gdx.files.internal("ContentGlobal/Item").list();
        }
        for (FileHandle file : files) {
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
        String ID = file.name().replace(".json", "");
        String TxT = file.readString();
        // Чтение JSON-файла и создание объекта Person
        ObjectMapper objectMapper = new ObjectMapper();

        buffItem obj = objectMapper.readValue(TxT, buffItem.class);
        Armor = obj.Armor;
        Hp = obj.Hp;
        Image = obj.Image;
        ArmorPercent = obj.ArmorPercent;
        HPPercent = obj.HPPercent;
        DamagePercent = obj.DamagePercent;
        MoveUPPercent= obj.MoveUPPercent;
        MoveDownPercent= obj.MoveDownPercent;
        AccelerationPercent= obj.AccelerationPercent;

        ArrayList<TegItem> list = new ArrayList();
        list.add(TegItem.upgrade);
        new Item(HPPercent,Hp,Armor,ArmorPercent,DamagePercent,PenetrationPercent
                ,MoveUPPercent,MoveDownPercent,AccelerationPercent,ID,
                new ArrayList<>(list),Image);
    }
    public static void AddBuilding(){
        new File("ContentGlobal").mkdirs();
        new File("ContentGlobal/Item").mkdirs();
        File armorB1 = new File("ContentGlobal/Item/armorB1.json");
        String data = "{\n" +
                "  \"Armor\": 20,\n" +
                "  \"Hp\": 500,\n" +
                "  \"ArmorPercent\": 10,\n" +
                "  \"HPPercent\": 0,\n" +
                "  \"DamagePercent\": 0,\n" +
                "  \"Image\": \"armorB1\",\n" +
                "  \"MoveUPPercent\": 0,\n" +
                "  \"MoveDownPercent\": 0,\n" +
                "  \"AccelerationPercent\": 0\n" +
                "\n" +
                "}";
        Create(armorB1,data);


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

