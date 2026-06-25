package com.mygdx.game.Parsing;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mygdx.game.Inventory.Inventory;
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
            ,MoveUPPercent,MoveDownPercent, AccelerationPercent,Price;
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
        int ArmorFront = obj.ArmorFront;
        int ArmorCenter = obj.ArmorCenter;
        int ArmorBack = obj.ArmorBack;
        int Hp = obj.Hp;
        String Image = obj.Image;
        int ArmorFrontPercent = obj.ArmorFrontPercent;
        int ArmorCenterPercent = obj.ArmorCenterPercent;
        int ArmorBackPercent = obj.ArmorBackPercent;
        int HPPercent = obj.HPPercent;
        int DamagePercent = obj.DamagePercent;
        int MoveUPPercent= obj.MoveUPPercent;
        int MoveDownPercent= obj.MoveDownPercent;
        int AccelerationPercent= obj.AccelerationPercent;
        int Price = obj.Price;

        int PenetrationPercent = obj.PenetrationPercent;

        ArrayList<TegItem> list = new ArrayList<>();
        list.add(TegItem.upgrade);
        if(Price!= 0){
            Inventory.AssortmentAdd(new Item(HPPercent,Hp,ArmorFront,ArmorCenter,ArmorBack
                    ,ArmorFrontPercent,ArmorCenterPercent,ArmorBackPercent,DamagePercent,PenetrationPercent
                    ,MoveUPPercent,MoveDownPercent,AccelerationPercent,Price,ID,
                    new ArrayList<>(list),Image));
        }
        else{
            new Item(HPPercent,Hp,ArmorFront,ArmorCenter,ArmorBack
                    ,ArmorFrontPercent,ArmorCenterPercent,ArmorBackPercent,DamagePercent,PenetrationPercent
                    ,MoveUPPercent,MoveDownPercent,AccelerationPercent,Price,ID,
                    new ArrayList<>(list),Image);
        }
    }
    public static void AddBuilding(){
        new File("ContentGlobal").mkdirs();
        new File("ContentGlobal/Item").mkdirs();
        File armorB1 = new File("ContentGlobal/Item/armorB1.json");
        String data = "{\n" +
                "  \"ArmorFront\": 20,\n" +
                "  \"ArmorCenter\": 10,\n" +
                "  \"ArmorBack\": 5,\n" +
                "  \"Hp\": 500,\n" +
                "  \"ArmorFrontPercent\": 10,\n" +
                "  \"ArmorCenterPercent\": 10,\n" +
                "  \"ArmorBackPercent\": 10,\n" +
                "  \"HPPercent\": 0,\n" +
                "  \"DamagePercent\": 0,\n" +
                "  \"Image\": \"armorB1\",\n" +
                "  \"MoveUPPercent\": 200,\n" +
                "  \"MoveDownPercent\": 0,\n" +
                "  \"AccelerationPercent\": 0,\n" +
                "  \"Price\": 2\n" +
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

