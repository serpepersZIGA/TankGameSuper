package com.mygdx.game.MapFunction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Parsing.Parser;
import com.mygdx.game.block.UpdateRegister;
import com.mygdx.game.build.Building;
import com.mygdx.game.main.Main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

import static com.mygdx.game.build.BuildRegister.BuildingID;

import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.object_map.MapObject.ObjectMapIDList;

import static java.nio.file.Files.readAllLines;

public class MapScan {
    public static void MapInput(String Map) {
        LightSystem.lightsRender.clear();
        LightSystem.lights.clear();
        BuildingList.clear();
        BlockDelete();
        String TxT;
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(Map))) {
            result.append(br.readLine());

        } catch (IOException e) {
            e.printStackTrace();
            MapBaseAdd.AddMap();
            try {
                BufferedReader br = new BufferedReader(new FileReader(Map));
                result.append(br.readLine());
            } catch (IOException ignored) {
            }
        }
        FileHandle file = Gdx.files.internal(Map);
        TxT = file.readString();
        String TotalTxT = "";
        int[] xy;
        char c;
        int i;


        for (i = 0; i < TxT.length(); i++) {
            c = TxT.charAt(i);
            if (c == ',' || c == ' '|| c == '/') {
            }
            else if (c == ':') {
                switch (TotalTxT){
                    case "MapObject":

                        //i = xy[2]+1;
                        i =ObjSpawn(i,TxT,0,0);
                        break;
                    case "BuildAdd":

                        //i = xy[2]+1;
                        i = BuildSpawn(i,TxT,0,0);
                        break;
                    case "Asphalt":
                        //i = xy[2]+1;
                        i = AsphaltSpawn(i,TxT,0,0);
                        break;
                    case "(str)":
                        xy= XYObject(TxT,i);
                        Object[]obj = Parser.TextPars2(TxT,i);
                        //i = xy[2]+1;
                        SpawnStructures((String) obj[0],xy[0],xy[1]);
                        for(;i<TxT.length();i++){
                            c = TxT.charAt(i);
                            if(c == ';'){
                                break;
                            }
                        }
                        break;
                }
                TotalTxT = "";
            } else if (c== ';'||c== '\n') {
                TotalTxT = "";
            } else{
                TotalTxT = TotalTxT + c;
            }
        }


    }

    public static int[] XYObject(String TxT,int i){
        int x = 0,y = 0;
        char c;
        for (; i < TxT.length(); i++) {
            c = TxT.charAt(i);
            if (c == '\n' || c == ',' || c == ' '|| c == '/') {
            }
            switch(c){
                case 'x':{
                    Object[]obj = Parser.IntegerPars2(TxT,i);
                    x = (int)obj[0];
                    i =  (int)obj[1];

                }
                break;
                case 'y':{
                    Object[]obj = Parser.IntegerPars2(TxT,i);
                    y = (int)obj[0];
                    i =  (int)obj[1];
                }
                break;
                case';':{
                    return new int[]{x,y,i};
                }
            }


        }
        return new int[]{x,y,i};

    }

    public static String MapName(String Map) {
        String TxT;
        String name = "";
        boolean conf = false;
        try {
            TxT = String.valueOf(readAllLines(Paths.get(Map), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String TotalTxT = "";
        for (int i = 0; i < TxT.length(); i++) {
            char c = TxT.charAt(i);
            if (c == '\n') {
                TotalTxT = "";
            } else if (c == '^') {
                conf = true;
            } else if (c == ';' & conf) {
                name = TotalTxT;
                conf = false;

            } else if (conf) {
                TotalTxT = TotalTxT + c;
            }
        }
        return name;
    }

    public static void MapSize(String Map) {
        String TxT;
        try {
            TxT = String.valueOf(readAllLines(Paths.get(Map), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int x = 0,y = 0;
        for (int i = 0; i < TxT.length(); i++) {
            char c = TxT.charAt(i);
            if (c == '/') {
                for (; i < TxT.length(); i++) {
                    c = TxT.charAt(i);
                    switch (c){
                        case 'x':{
                            Object[]xi = Parser.IntegerPars2(TxT,i);
                            x = (int)xi[0];
                            i =  (int)xi[1];
                        }break;
                        case 'y':{
                            Object[]yi = Parser.IntegerPars2(TxT,i);
                            y = (int)yi[0];
                            i =  (int)yi[1];
                        }break;
                        case ';':{
                            field(x,y);
                            return;
                        }
                    }
                }
            }
        }
        return;
    }

    private static void BlockDelete() {
        for (int i = 0; i < BlockList2D.size(); i++) {
            for (int i2 = 0; i2 < BlockList2D.get(i).size(); i2++) {
                BlockList2D.get(i).get(i2).render_block = UpdateRegister.GrassUpdate;
                BlockList2D.get(i).get(i2).objMap = VoidObj;
            }
        }
    }
    public static void SpawnObjectBlock(String Obj,int x,int y){

        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("ContentGlobal/ObjectMap/"+Obj+".json"))) {
            result.append(br.readLine());

        } catch (IOException e) {
            e.printStackTrace();
            MapBaseAdd.AddMap();
            try {
                BufferedReader br = new BufferedReader(new FileReader("ContentGlobal/ObjectMap/"+Obj+".json"));
                result.append(br.readLine());
            } catch (IOException ignored) {
            }
        }
        //FileHandle file = Gdx.files.internal("ContentGlobal/ObjectMap/"+Obj+".json");
        // Чтение JSON-файла и создание объекта Person


        ObjectMapIDList.get(Obj).MapObjectAdd(x,y);


        //BlockList2D.get(y).get(x).objMap

    }

    private static void MapSpawnBlock(String Build, int x, int y, int z, int conf) {
        if (Objects.equals(Build, "Asphalt") & conf == 0) {
            AsphaltSpawn(x, y);
        } else if (Objects.equals(Build, "Asphalt") & conf == 1) {
            for (int i = 0; i < z; i++) {
                AsphaltSpawn(x + i, y);
            }
        } else if (Objects.equals(Build, "Asphalt") & conf == 2) {
            for (int i = 0; i < z; i++) {
                AsphaltSpawn(x, y + i);
            }
        }
    }

    public static void AsphaltSpawn(int x, int y) {
        BlockList2D.get(y).get(x).render_block = UpdateRegister.UpdateAsphalt1;
    }




    private static void SpawnStructures(String Struct, int xStr, int yStr) {
        String TxT;
        StringBuilder result = new StringBuilder();
        Struct = "Map/Structure/"+Struct+".str";
        try (BufferedReader br = new BufferedReader(new FileReader(Struct))) {
            result.append(br.readLine());

        } catch (IOException e) {
            e.printStackTrace();
            try {
                BufferedReader br = new BufferedReader(new FileReader(Struct));
                result.append(br.readLine());
            } catch (IOException ignored) {
            }
        }
        FileHandle file = Gdx.files.internal(Struct);
        TxT = file.readString();
        String TotalTxT = "";
        int i;
        char c;


        for (i = 0; i < TxT.length(); i++) {
            c = TxT.charAt(i);
            if (c == '\n' || c == ',' || c == ' '|| c == '/') {
            }
            else if (c == ':') {
                switch (TotalTxT){
                    case "MapObject":i =ObjSpawn(i,TxT,xStr,yStr)+1;
                        break;
                    case "BuildAdd":i = BuildSpawn(i,TxT,xStr,yStr)+1;
                        break;
                    case "Asphalt":i = AsphaltSpawn(i,TxT,xStr,yStr)+1;
                        break;
                    case "(str)":
                        int[]xy= XYObject(TxT,i);
                        Object[]obj = Parser.TextPars2(TxT,i);
                        String str = obj[0].toString();
                        SpawnStructures(str,xStr+xy[0],yStr+xy[1]);
                    for(;i<TxT.length();i++){
                        c = TxT.charAt(i);
                        if(c == ';'||c=='\n'){
                            break;
                        }
                    }
                    break;
                }
                TotalTxT = "";
            }
            else{
                TotalTxT = TotalTxT + c;
            }
        }

    }

    private static int ObjSpawn(int i,String TxT,int xStr, int yStr){
        char c;
        int x = 0;
        int y = 0;
        String Build = "";
        for (; i < TxT.length(); i++) {
            c = TxT.charAt(i);
            switch (c){
                case'x':{
                    Object[]obj = Parser.IntegerPars2(TxT,i);
                    x = (int)obj[0];
                    i =  (int)obj[1];
                }
                break;
                case'y':{
                    Object[]obj = Parser.IntegerPars2(TxT,i);
                    y = (int)obj[0];
                    i =  (int)obj[1];
                }
                break;
                case'o':{
                    Object[]obj = Parser.TextPars2(TxT,i);
                    Build = (String) obj[0];
                    i =  (int)obj[1];
                }
                break;
                case';':{
                    SpawnObjectBlock(Build,x+xStr,y+yStr);
                    return i;
                }
            }
            //else if (c == ':') {
            //}
        }
        return i;
    }
    private static int BuildSpawn(int i,String TxT,int xStr, int yStr){
        char c;
        int x = 0;
        int y = 0;
        String Build = "";
        int rotation = 0;
        for (; i < TxT.length(); i++) {
            c = TxT.charAt(i);
            switch (c){
                case'x':{
                    Object[]obj = Parser.IntegerPars2(TxT,i);
                    x = (int)obj[0];
                    i =  (int)obj[1];
                }
                break;
                case'y':{
                    Object[]obj = Parser.IntegerPars2(TxT,i);
                    y = (int)obj[0];
                    i = (int)obj[1];
                }
                break;
                case'r':{
                    Object[]obj = Parser.IntegerPars2(TxT,i);
                    rotation = (int)obj[0];
                    i = (int)obj[1];
                }
                break;
                case'B':{
                    Object[]obj = Parser.TextPars2(TxT,i);
                    Build = (String) obj[0];
                    Build = Build.trim();
                    i =  (int)obj[1];
                }
                break;
                case';':{
                    for(Object[] obj :BuildingID){
                        //System.out.println(Build+" "+obj[1]);
                        if(Objects.equals(obj[1],Build)){

                            Building build = (Building)obj[0];
                            BuildingList.add(build.BuildingCreate((x+xStr) * width_block,(y+yStr) * width_block,rotation));
                        }
                    }
                    return i;

                }
            }
            //else if (c == ':') {
            //}
        }
        return i;
    }
    private static int AsphaltSpawn(int i,String TxT,int xStr, int yStr){
        char c;
        int x = 0;
        int y = 0;
        byte xy = 0;
        int XY = 0;
        byte conf = 0;
        for (; i < TxT.length(); i++) {
            c = TxT.charAt(i);
            switch (c){
                case'x':{
                    Object[]obj = Parser.IntegerPars2(TxT,i);
                    x = (int)obj[0];
                    i =  (int)obj[1];
                }
                break;
                case'y':{
                    Object[]obj = Parser.IntegerPars2(TxT,i);
                    y = (int)obj[0];
                    i =  (int)obj[1];
                }
                break;
                case'X':{
                    xy = 1;
                    Object[]obj = Parser.IntegerPars2(TxT,i);
                    XY = (int)obj[0];
                    i =  (int)obj[1];
                }
                break;
                case'Y':{
                    xy = 2;
                    Object[]obj = Parser.IntegerPars2(TxT,i);
                    XY = (int)obj[0];
                    i =  (int)obj[1];
                }
                break;
                case';':{
                    MapSpawnBlock("Asphalt", xStr+ x, yStr+y, XY,xy);
                    return i;
                }
            }
            //else if (c == ':') {
            //}
        }
        return i;
    }
}
