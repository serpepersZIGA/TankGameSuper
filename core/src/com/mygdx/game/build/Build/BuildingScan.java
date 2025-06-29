package com.mygdx.game.build.Build;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.MapFunction.MapBaseAdd;
import com.mygdx.game.build.BuildRegister;
import com.mygdx.game.build.Building;
import com.mygdx.game.main.Main;
import com.mygdx.game.menu.button.ButtonTank.ListTankPlayerAdd;
import com.mygdx.game.menu.button.ButtonTank.TankChoice;

import java.io.*;
import java.util.Objects;

import static com.mygdx.game.main.Main.LightSystem;
import static com.mygdx.game.unit.SpawnPlayer.PlayerSpawnListData.SpawnList;

public class BuildingScan {
    public static boolean[][]StructBuffer;
    public static String Path;
    public static boolean FlameStatus;
    public static void ScanGlobal(){
        FileHandle[] files = Gdx.files.internal("ContentGlobal/Building").list();
        if(files.length== 0){
            AddBuilding();
            files = Gdx.files.internal("ContentGlobal/Building").list();
        }
        for (FileHandle file: files) {
            //System.out.println(file.path());
            Scan(file.path());
//            for (int i = file.name().length()-4;i<file.name().length();i++){
//                Name = file.name().replace(".bld");
//            }
//            StructBuffer = new boolean[][]{{true,true},
//                    {true,true}};
            BuildRegister.BuildingID.add(new Object[]{
                    new Building(0,0,file.name().replace(".bld",""),StructBuffer,Path,FlameStatus)
                    ,file.name().replace(".bld","")});
            //System.out.println(BuildRegister.BuildingID.size());

        }


    }
    public static void Scan(String Path) {
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
            switch (TotalTxT.trim()) {
                case "Struct":
                    i = Struct(TxT, i);
                    TotalTxT = "";
                    break;
                case "Asset":
                    //System.out.println(658349305);
                    i = Asset(TxT, i);
                    TotalTxT = "";
                    break;
                case "FlameStatus":
                    i = FlameStatus(TxT, i);
                    TotalTxT = "";
                    break;
            }
        }


    }
    public static int Struct(String TxT,int u){
        String TotalTxT = "";
        int[]xy = StructScan(TxT,u);
        StructBuffer = new boolean[xy[1]][xy[0]];
        int X = 0;
        int Y = 0;
        //int XMax = xy[0];
        //int YMax = xy[1];

        for (int i = u;i < TxT.length(); i++) {
            char c = TxT.charAt(i);
            if (c == ',') {
                X+= 1;
                TotalTxT = "";
            }
            else if (c == ':') {
                Y+= 1;
                X = 0;
                TotalTxT = "";
            }
            else if(c ==';'){
                return i;
            }
            else if(c !=' '& c !='\n' & c !='{'& c !='}'){
                TotalTxT = TotalTxT + c;
            }
            else{
                TotalTxT = "";
            }
            if(TotalTxT.equals("true")){
                StructBuffer[Y][X] = true;
                TotalTxT = "";
            }
            else if(TotalTxT.equals("false")){
                StructBuffer[Y][X] = false;
                TotalTxT = "";
            }

        }
        throw new RuntimeException();
    }
    public static int[] StructScan(String TxT,int u){
        int X = 0;
        int Y = 0;
        boolean XY = false;
        //StructBuffer = new boolean[][]{};

        for (int i = u;i < TxT.length(); i++) {
            char c = TxT.charAt(i);
            if(c == ','& !XY){
                X+=1;
            }
            else if(c == ':'){
                Y+=1;
                XY = true;
            } else if (c == ';') {
                return new int[]{X+1,Y+1} ;
            }

        }
        throw new RuntimeException();
    }
    public static int Asset(String TxT,int u){
        String TotalTxT = "";
        //int XMax = xy[0];
        //int YMax = xy[1];

        for (int i = u+1;i < TxT.length(); i++) {
            char c = TxT.charAt(i);
            if(c != '\n' &  c != ' '& c !='{'& c !='}'& c != '='& c != ';'){
                TotalTxT = TotalTxT + c;
            }
            if(c ==';'){
                //System.out.println(TotalTxT.trim());
                Path = TotalTxT.trim();
                return i;
            }

        }
        throw new RuntimeException();
        //return -1;
    }
    public static int FlameStatus(String TxT,int u){
        String TotalTxT = "";
        //int XMax = xy[0];
        //int YMax = xy[1];

        for (int i = u;i < TxT.length(); i++) {
            char c = TxT.charAt(i);
            if (c != '\n' &  c != ' '& c !='{'& c !='}'& c !='='){
                TotalTxT = TotalTxT + c;
            }
            if(c ==';'){
                if(TotalTxT.trim().equals("true")){
                    FlameStatus = true;
                }
                else if(TotalTxT.trim().equals("false")){
                    FlameStatus = false;
                }
                return i;
            }

        }
        throw new RuntimeException();
    }


    public static void AddBuilding(){
        new File("ContentGlobal").mkdirs();
        new File("ContentGlobal/Building").mkdirs();
        new File("ContentGlobal/Block").mkdirs();
        File MapFile = new File("ContentGlobal/Building/BigBuildingWood1.bld");
        File StrFile = new File("ContentGlobal/Building/Building2.bld");

        try {
            StrFile.createNewFile();
        } catch (IOException ignored) {
        }
        try {
            MapFile.createNewFile();
        } catch (IOException ignored) {
        }
        String dataMap = "Asset = image/build/big_build_wood_1.png;\n" +
                "Struct = {true,true,true,true,false,false,true,true,true,true}:\n" +
                "                         {true,true,true,true,false,false,true,true,true,true}:\n" +
                "                         {true,true,true,true,false,false,true,true,true,true}:\n" +
                "                         {true,true,true,true,false,false,true,true,true,true}:\n" +
                "                         {true,true,true,true,false,false,true,true,true,true}:\n" +
                "                         {true,true,true,true,false,false,true,true,true,true};\n" +
                "FlameStatus = true;";
        try {
            PrintWriter out = new PrintWriter("Content/Building/BigBuildingWood1.bld");
            out.println(dataMap);
            out.close();
        } catch (IOException ignored) {
        }
        String dataStr =
                "ConstructBuilding = {true,true,true,true,true,true}:\n" +
                        "                {true,true,true,true,true,true}:\n" +
                        "                {true,true,true,true,true,true}:\n" +
                        "                {false,false,false,false,false,false}:\n" +
                        "                {false,false,false,false,false,false}:\n" +
                        "                {true,true,true,true,true,true};\n" +
                        "Asset = image/build/Build2.png;\n" +
                        "FlameStatus = true; ";
        try {
            PrintWriter out = new PrintWriter("Content/Building/Building2.bld");
            out.println(dataStr);
            out.close();
        } catch (IOException ignored) {
        }

    }
}
