package com.mygdx.game.unit.moduleUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.MapFunction.MapBaseAdd;
import com.mygdx.game.Parsing.Parser;
import com.mygdx.game.Sound.SoundRegister;
import com.mygdx.game.unit.Fire.Fire;

import java.io.*;

import static com.mygdx.game.build.Build.BuildingScan.AddBuilding;
import static com.mygdx.game.unit.moduleUnit.RegisterModuleCannon.CannonListID;
import static com.mygdx.game.unit.moduleUnit.RegisterModuleCorpus.CorpusListID;

public class CannonParser {
    public static int WidthTower,HeightTower,ConstTowerX,ConstTowerY,Difference,SizeBullet,ReloadMax,TimeBullet
            ,TimeBulletRand,TowerX,TowerY,AmountFragment;
    public static Fire Fire;
    public static Sound Sound;
    public static String Image;
    public static FunctionalList List;
    public static boolean ConfControl;
    public static float SpeedRotationTower, Damage, Penetration, DamageFragment, PenetrationFragment,TemperatureDamage
    ,SpeedBullet;



    public static void ParsCannon(){
        FileHandle[] files = Gdx.files.internal("ContentGlobal/Module/Cannon").list();
        if(files.length== 0){
            AddBuilding();
            files = Gdx.files.internal("ContentGlobal/Module/Cannon").list();
        }
        for (FileHandle file: files) {
            System.out.println(file.name().replace(".Cannon",""));
            //System.out.println(file.path());
            Scan(file.path());
//            for (int i = file.name().length()-4;i<file.name().length();i++){
//                Name = file.name().replace(".bld");
//            }
//            StructBuffer = new boolean[][]{{true,true},
//                    {true,true}};
            CannonListID.add(new Object[]{
                    new Cannon(file.name().replace(".Cannon", "")
                            , WidthTower, HeightTower, ConstTowerX, ConstTowerY, SpeedRotationTower,
                            Damage, Penetration
                            , DamageFragment, PenetrationFragment, TemperatureDamage, SizeBullet, ReloadMax,
                            SpeedBullet, TimeBullet, TimeBulletRand, TowerX, TowerY, Fire.clone(), Image, List
                            , Sound, AmountFragment)
                    , file.name().replace(".Cannon", "")});
            List = new FunctionalList();

            //List.Clear();
            //System.out.println(BuildRegister.BuildingID.size());

        }


    }
    public static void Scan(String Path) {
        List = new FunctionalList();
        Object[]obj;
        String TxT = "";
        char c;
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            result.append(br.readLine());

        } catch (IOException e) {
            e.printStackTrace();
            MapBaseAdd.AddMap();
            try {
                BufferedReader br = new BufferedReader(new FileReader(Path));
                result.append(br.readLine());
            } catch (IOException ignored) {
            }
        }
        FileHandle file = Gdx.files.internal(Path);
        TxT = file.readString();
        String TotalTxT = "";


        for (int i = 0; i < TxT.length(); i++) {
            c = TxT.charAt(i);

            if(c != '\n' &  c != ' ' & c != '=' & c != '{'& c != '}'){
                TotalTxT = TotalTxT + c;
            }
            //System.out.println(TotalTxT);
            if(c == '=') {
                switch (TotalTxT.trim()) {
                    case "WidthTower":
                        obj = Parser.IntegerPars(TxT, i);
                        WidthTower = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "HeightTower":
                        obj = Parser.IntegerPars(TxT, i);
                        HeightTower = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "ConstTowerX":
                        obj = Parser.IntegerPars(TxT, i);
                        ConstTowerX = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;

                    case "ConstTowerY":
                        obj = Parser.IntegerPars(TxT, i);
                        ConstTowerY = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "SpeedRotationTower":
                        obj = Parser.FloatPars(TxT, i);
                        SpeedRotationTower = (float) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;

                    case "Damage":
                        obj = Parser.FloatPars(TxT, i);
                        Damage = (float) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "Penetration":
                        obj = Parser.FloatPars(TxT, i);
                        Penetration = (float) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "DamageFragment":
                        obj = Parser.FloatPars(TxT, i);
                        DamageFragment = (float) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;

                    case "PenetrationFragment":
                        obj = Parser.FloatPars(TxT, i);
                        PenetrationFragment = (float) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "TemperatureDamage":
                        obj = Parser.FloatPars(TxT, i);
                        TemperatureDamage = (float) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;


                    case "SizeBullet":
                        obj = Parser.IntegerPars(TxT, i);
                        SizeBullet = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "ReloadMax":
                        obj = Parser.IntegerPars(TxT, i);
                        ReloadMax = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "SpeedBullet":
                        obj = Parser.FloatPars(TxT, i);
                        SpeedBullet = (float) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;

                    case "TimeBullet":
                        obj = Parser.IntegerPars(TxT, i);
                        TimeBullet = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "TimeBulletRand":
                        obj = Parser.FloatPars(TxT, i);
                        TimeBulletRand = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;


                    case "TowerX":
                        obj = Parser.IntegerPars(TxT, i);
                        TowerX = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "TowerY":
                        obj = Parser.IntegerPars(TxT, i);
                        TowerY = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "Fire":
                        obj = Parser.FireParsing(TxT, i);
                        Fire Fires = (Fire) obj[0];
                        Fire = Fires.clone();
                        //Fire = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;

                    case "Sound":
                        obj = Parser.IntegerPars(TxT, i);
                        Sound = (Sound) SoundRegister.IDSound.get((int) obj[0])[0];
                        //Sound = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "AmountFragment":
                        obj = Parser.IntegerPars(TxT, i);
                        AmountFragment = (int) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;


                    case "Image":
                        obj = Parser.TextPars(TxT, i);
                        Image = (String) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;
                    case "func.Add":
                        obj = Parser.ComponentParsing(TxT, i);
                        FunctionalComponent comp = (FunctionalComponent) obj[0];
                        List.Add(comp);
                        //ConfControl = (boolean) obj[0];
                        i = (int) obj[1];
                        TotalTxT = "";
                        break;

                }
            }
        }


    }
    public static void AddBuilding(){
        new File("ContentGlobal").mkdirs();
        new File("ContentGlobal/Module").mkdirs();
        new File("ContentGlobal/Module/Cannon").mkdirs();
        File Ack2A = new File("ContentGlobal/Module/Cannon/Ack2A.Cannon");
        File Ack2AL = new File("ContentGlobal/Module/Cannon/Ack2AL.Cannon");
        File Flk4C = new File("ContentGlobal/Module/Cannon/Flk4C.Cannon");
        File Flk4CL = new File("ContentGlobal/Module/Cannon/Flk4CL.Cannon");
        File Kwk12A = new File("ContentGlobal//Module/Cannon/Kwk12A.Cannon");
        File Kwk12AL = new File("ContentGlobal/Module/Cannon/Kwk12AL.Cannon");
        File Kwk12M = new File("ContentGlobal/Module/Cannon/Kwk12M.Cannon");
        File Kwk12ML = new File("ContentGlobal/Module/Cannon/Kwk12ML.Cannon");
        String data = "WidthTower = 55;\n" +
                "HeightTower = 35;\n" +
                "ConstTowerX = 34;\n" +
                "ConstTowerY = 17;\n" +
                "SpeedRotationTower = 1;\n" +
                "Damage = 22;\n" +
                "Penetration = 20;\n" +
                "DamageFragment = 0;\n" +
                "PenetrationFragment = 0;\n" +
                "TemperatureDamage = 0;\n" +
                "SizeBullet = 2;\n" +
                "ReloadMax = 2;\n" +
                "SpeedBullet = 4;\n" +
                "TimeBullet = 85;\n" +
                "TowerX = 15;\n" +
                "TowerY = 20;\n" +
                "Fire = Acid;\n" +
                "Image = tower_enemy;\n" +
                "\n" +
                "func.Add = ComponentTowerXY;\n" +
                "func.Add = ComponentTowerControl;\n" +
                "func.Add = ComponentFireControl;\n" +
                "\n" +
                "Sound = 2;\n" +
                "AmountFragment = 0;";
        Create(Ack2A,data);
        data = "WidthTower = 20;\n" +
                "HeightTower = 15;\n" +
                "ConstTowerX = 12;\n" +
                "ConstTowerY = 7;\n" +
                "SpeedRotationTower = 1;\n" +
                "Damage = 15;\n" +
                "Penetration = 20;\n" +
                "DamageFragment = 0;\n" +
                "PenetrationFragment = 0;\n" +
                "TemperatureDamage = 0;\n" +
                "SizeBullet = 2;\n" +
                "ReloadMax = 2;\n" +
                "SpeedBullet = 4;\n" +
                "TimeBullet = 85;\n" +
                "TowerX = 15;\n" +
                "TowerY = 20;\n" +
                "Fire = Acid;\n" +
                "Image = tower_enemy_auxiliary_1;\n" +
                "\n" +
                "func.Add = ComponentTowerXY;\n" +
                "func.Add = ComponentTowerControl;\n" +
                "func.Add = ComponentFireControl;\n" +
                "\n" +
                "Sound = 2;\n" +
                "AmountFragment = 0;";
        Create(Ack2AL,data);
        data = "WidthTower = 55;\n" +
                "HeightTower = 35;\n" +
                "ConstTowerX = 34;\n" +
                "ConstTowerY = 17;\n" +
                "SpeedRotationTower = 1;\n" +
                "Damage = 10;\n" +
                "Penetration = 2;\n" +
                "DamageFragment = 0;\n" +
                "PenetrationFragment = 0;\n" +
                "TemperatureDamage = 1.5;\n" +
                "SizeBullet = 2;\n" +
                "ReloadMax = 2;\n" +
                "SpeedBullet = 4;\n" +
                "TimeBullet = 85;\n" +
                "TowerX = 15;\n" +
                "TowerY = 20;\n" +
                "Fire = Flame;\n" +
                "Image = tower_enemy;\n" +
                "\n" +
                "func.Add = ComponentTowerXY;\n" +
                "func.Add = ComponentTowerControl;\n" +
                "func.Add = ComponentFireControl;\n" +
                "\n" +
                "Sound = 5;\n" +
                "AmountFragment = 0;";
        Create(Flk4C,data);
        data = "WidthTower = 20;\n" +
                "HeightTower = 15;\n" +
                "ConstTowerX = 12;\n" +
                "ConstTowerY = 7;\n" +
                "SpeedRotationTower = 1;\n" +
                "Damage = 10;\n" +
                "Penetration = 2;\n" +
                "DamageFragment = 0;\n" +
                "PenetrationFragment = 0;\n" +
                "TemperatureDamage = 1.5;\n" +
                "SizeBullet = 2;\n" +
                "ReloadMax = 2;\n" +
                "SpeedBullet = 4;\n" +
                "TimeBullet = 85;\n" +
                "TowerX = 15;\n" +
                "TowerY = 20;\n" +
                "Fire = Flame;\n" +
                "Image = tower_enemy_auxiliary_1;\n" +
                "\n" +
                "func.Add = ComponentTowerXY;\n" +
                "func.Add = ComponentTowerControl;\n" +
                "func.Add = ComponentFireControl;\n" +
                "\n" +
                "Sound = 5;\n" +
                "AmountFragment = 0;";
        Create(Flk4CL,data);
        data = "WidthTower = 55;\n" +
                "HeightTower = 35;\n" +
                "ConstTowerX = 34;\n" +
                "ConstTowerY = 17;\n" +
                "SpeedRotationTower = 1;\n" +
                "Damage = 15;\n" +
                "Penetration = 12;\n" +
                "DamageFragment = 0;\n" +
                "PenetrationFragment = 0;\n" +
                "TemperatureDamage = 0;\n" +
                "SizeBullet = 2;\n" +
                "ReloadMax = 2;\n" +
                "SpeedBullet = 12;\n" +
                "TimeBullet = 270;\n" +
                "TowerX = 15;\n" +
                "TowerY = 20;\n" +
                "Fire = Bullet;\n" +
                "Image = tower_enemy;\n" +
                "\n" +
                "func.Add = ComponentTowerXY;\n" +
                "func.Add = ComponentTowerControl;\n" +
                "func.Add = ComponentFireControl;\n" +
                "\n" +
                "Sound = 4;\n" +
                "AmountFragment = 0;";
        Create(Kwk12A,data);
        data = "WidthTower = 20;\n" +
                "HeightTower = 15;\n" +
                "ConstTowerX = 12;\n" +
                "ConstTowerY = 7;\n" +
                "SpeedRotationTower = 1;\n" +
                "Damage = 15;\n" +
                "Penetration = 12;\n" +
                "DamageFragment = 0;\n" +
                "PenetrationFragment = 0;\n" +
                "TemperatureDamage = 0;\n" +
                "SizeBullet = 2;\n" +
                "ReloadMax = 2;\n" +
                "SpeedBullet = 12;\n" +
                "TimeBullet = 270;\n" +
                "TowerX = 15;\n" +
                "TowerY = 20;\n" +
                "Fire = Bullet;\n" +
                "Image = tower_enemy_auxiliary_1;\n" +
                "\n" +
                "func.Add ComponentTowerXY;\n" +
                "func.Add ComponentTowerControl;\n" +
                "func.Add ComponentFireControl;\n" +
                "\n" +
                "Sound = 4;\n" +
                "AmountFragment = 0;";
        Create(Kwk12AL,data);
        data = "WidthTower = 55;\n" +
                "HeightTower = 35;\n" +
                "ConstTowerX = 34;\n" +
                "ConstTowerY = 17;\n" +
                "SpeedRotationTower = 1;\n" +
                "Damage = 420;\n" +
                "Penetration = 24;\n" +
                "DamageFragment = 12;\n" +
                "PenetrationFragment = 16;\n" +
                "TemperatureDamage = 0;\n" +
                "SizeBullet = 2;\n" +
                "ReloadMax = 200;\n" +
                "SpeedBullet = 10;\n" +
                "TimeBullet = 450;\n" +
                "TowerX = 15;\n" +
                "TowerY = 20;\n" +
                "Fire = Mortar;\n" +
                "Image = tower_enemy;\n" +
                "\n" +
                "func.Add = ComponentTowerXY;\n" +
                "func.Add = ComponentTowerControl;\n" +
                "func.Add = ComponentFireControl;\n" +
                "\n" +
                "Sound = 4;\n" +
                "AmountFragment = 15;";
        Create(Kwk12M,data);
        data = "WidthTower = 20;\n" +
                "HeightTower = 15;\n" +
                "ConstTowerX = 12;\n" +
                "ConstTowerY = 7;\n" +
                "SpeedRotationTower = 1;\n" +
                "Damage = 320;\n" +
                "Penetration = 24;\n" +
                "DamageFragment = 12;\n" +
                "PenetrationFragment = 16;\n" +
                "TemperatureDamage = 0;\n" +
                "SizeBullet = 2;\n" +
                "ReloadMax = 200;\n" +
                "SpeedBullet = 10;\n" +
                "TimeBullet = 450;\n" +
                "TowerX = 15;\n" +
                "TowerY = 20;\n" +
                "Fire = Mortar;\n" +
                "Image = tower_enemy_auxiliary_1;\n" +
                "\n" +
                "func.Add = ComponentTowerXY;\n" +
                "func.Add = ComponentTowerControl;\n" +
                "func.Add = ComponentFireControl;\n" +
                "\n" +
                "Sound = 4;\n" +
                "AmountFragment = 12;";
        Create(Kwk12ML,data);

    }
    private static void Create(File file,String str){
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
