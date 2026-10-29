package com.mygdx.game.Parsing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.Inventory.Inventory;
import com.mygdx.game.Inventory.Item;
import com.mygdx.game.Inventory.ItemRegister;
import com.mygdx.game.Inventory.TegItem;
import com.mygdx.game.unit.Fire.FireRegister;
import com.mygdx.game.unit.moduleUnit.Gun;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static com.mygdx.game.Sound.SoundRegister.IDSound;

public class ParsGun {
    public static void Pars() {
        FileHandle[] files = Gdx.files.internal("ContentGlobal/Gun").list();
        if (files.length == 0) {
            AddBuilding();
            files = Gdx.files.internal("ContentGlobal/Gun").list();
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

        buffGun obj = objectMapper.readValue(TxT, buffGun.class);


        int Damage = obj.Damage;
        int Penetration = obj.Penetration;
        int DamageFragment = obj.DamageFragment;
        int PenetrationFragment = obj.PenetrationFragment;
        String Image = obj.Image;
        int TemperatureDamage = obj.TemperatureDamage;
        int ReloadMax = obj.ReloadMax;
        int TimeBullet = obj.TimeBullet;
        int AmountFragment = obj.AmountFragment;
        int SpeedBullet = obj.SpeedBullet;
        int sound = obj.Sound;
        int Price = obj.Price;
        int QuantityShots = obj.QuantityShots;
        int ReloadBetweenShots = obj.ReloadBetweenShots;
        int ReloadShots = obj.ReloadShots;
        float ReloadUP = obj.ReloadUP;
        float OneShot = obj.OneShot;
        String TypeShot = obj.TypeShot;
        ArrayList<String>TegList = obj.TegList;
        ArrayList<TegItem>teg = new ArrayList<>();
        for(int i = 0;i<TegList.size();i++){
            String str = TegList.get(i);
            teg.add(Parser.TegUnitParsing(str));
        }

        FunctionalComponent func = Parser.ComponentParsing(TypeShot);
        Object[]e = IDSound.get(sound);

        String Fire = obj.Fire;
//        new Item(new Gun(Damage,Penetration,DamageFragment,PenetrationFragment,TemperatureDamage,ReloadMax,TimeBullet
//                ,Parser.FireParsing(Fire),(Sound) e[0],AmountFragment,SpeedBullet,
//                QuantityShots,ReloadBetweenShots,
//                ReloadShots,ReloadUP,OneShot,func,
//                ID),ID,teg,Image,Price);
        if (Price != 0) {
            Inventory.AssortmentAdd(new Item(new Gun(Damage,Penetration,DamageFragment,PenetrationFragment,TemperatureDamage,ReloadMax,TimeBullet
                    ,Parser.FireParsing(Fire),(Sound) e[0],AmountFragment,SpeedBullet,
                    QuantityShots,ReloadBetweenShots,
                    ReloadShots,ReloadUP,OneShot,func,
                    ID),ID,teg,Image,Price));
        } else {
            new Item(new Gun(Damage,Penetration,DamageFragment,PenetrationFragment,TemperatureDamage,ReloadMax,TimeBullet
                    ,Parser.FireParsing(Fire),(Sound) e[0],AmountFragment,SpeedBullet,
                    QuantityShots,ReloadBetweenShots,
                    ReloadShots,ReloadUP,OneShot,func,
                    ID),ID,teg,Image,Price);
        }



    }
    public static void AddBuilding(){
        new File("ContentGlobal").mkdirs();
        new File("ContentGlobal/Gun").mkdirs();
        File AK74 = new File("ContentGlobal/Gun/AK74.json");
        File flamethrower = new File("ContentGlobal/Gun/flamethrower.json");
        String data = "{\n" +
                "  \"TypeShot\": \"ComponentFireDrumControl\",\n" +
                "  \"Fire\": \"Bullet\",\n" +
                "  \"Damage\": 20,\n" +
                "  \"Penetration\": 3,\n" +
                "  \"DamageFragment\": 0,\n" +
                "  \"PenetrationFragment\": 0,\n" +
                "  \"TemperatureDamage\": 0,\n" +
                "  \"ReloadMax\": 5,\n" +
                "  \"TimeBullet\": 200,\n" +
                "  \"AmountFragment\": 0,\n" +
                "  \"SpeedBullet\": 0,\n" +
                "  \"Sound\": 10,\n" +
                "  \"QuantityShots\": 30,\n" +
                "  \"ReloadBetweenShots\": 0,\n" +
                "  \"ReloadShots\": 0,\n" +
                "  \"ReloadUP\": 2,\n" +
                "  \"OneShot\": 5\n" +
                "\"Price\": 2\n" +
                "}";
        Create(AK74,data);
        data = "{\n" +
                "  \"TypeShot\": \"ComponentFireTankCumulativeControl\",\n" +
                "  \"Fire\": \"Flame\",\n" +
                "  \"Damage\": 20,\n" +
                "  \"Penetration\": 3,\n" +
                "  \"DamageFragment\": 0,\n" +
                "  \"PenetrationFragment\": 0,\n" +
                "  \"TemperatureDamage\": 0,\n" +
                "  \"ReloadMax\": 5,\n" +
                "  \"TimeBullet\": 200,\n" +
                "  \"AmountFragment\": 0,\n" +
                "  \"SpeedBullet\": 0,\n" +
                "  \"Sound\": 5,\n" +
                "  \"QuantityShots\": 30,\n" +
                "  \"ReloadBetweenShots\": 0,\n" +
                "  \"ReloadShots\": 0,\n" +
                "  \"ReloadUP\": 2,\n" +
                "  \"OneShot\": 5\n" +
                "\"Price\": 2\n" +
                "}";
        Create(flamethrower,data);


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
