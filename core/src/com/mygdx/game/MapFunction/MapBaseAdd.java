package com.mygdx.game.MapFunction;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class MapBaseAdd {
    public static void AddMap(){
        new File("Map").mkdirs();
        new File("Map/maps").mkdirs();
        new File("Map/Structure").mkdirs();
        new File("Map/ObjectMap").mkdirs();
        File MapFile = new File("Map/maps/MapBase.mapt");
        File Str2File = new File("Map/Structure/streetPoselok.str");
        File StrFile = new File("Map/Structure/street1.str");
        try {
            StrFile.createNewFile();
        } catch (IOException ignored) {
        }
        try {
            MapFile.createNewFile();
        } catch (IOException ignored) {
        }
        String dataMap = "^MapLite;\n" +
                "\n" +
                "str{street1}:23:23:;\n" +
                "str{street1}:23:40:;\n" +
                "str{street1}:69:23:;\n" +
                "str{street1}:69:40:;\n" +
                "str{streetPoselok}:70:60:;\n" +
                "Asphalt:70:53:>Y7;\n" +
                "str{pepper1}:12:12:;\n";
        try {
            PrintWriter out = new PrintWriter("Map/maps/MapBase.mapt");
            out.println(dataMap);
            out.close();
        } catch (IOException ignored) {
        }
        String dataStr =
                "BigBuildingWood1:0:1:;\n" +
                        "BigBuildingWood1:12:1:;\n" +
                        "BigBuildingWood1:24:1:;\n" +
                        "BigBuildingWood1:36:1:;\n" +
                        "BigBuildingWood1:0:10:;\n" +
                        "BigBuildingWood1:12:10:;\n" +
                        "BigBuildingWood1:24:10:;\n" +
                        "BigBuildingWood1:36:10:;\n" +
                        "Asphalt:0:8:>X46;\n" +
                        "Asphalt:0:9:>X46;\n" +
                        "Asphalt:0:7:>X46;\n" +
                        "Asphalt:0:16:>X46;\n" +
                        "Asphalt:0:0:>X46;\n" +
                        "Asphalt:10:1:>Y15;\n" +
                        "Asphalt:11:1:>Y15;\n" +
                        "Asphalt:22:1:>Y15;\n" +
                        "Asphalt:23:1:>Y15;\n" +
                        "Asphalt:34:1:>Y15;\n" +
                        "Asphalt:35:1:>Y15;";
        try {
            PrintWriter out = new PrintWriter("Map/Structure/street1.str");
            out.println(dataStr);
            out.close();
        } catch (IOException ignored) {
        }

        dataStr =
                "Build2:1:1:;\n" +
                        "Build2:8:1:;\n" +
                        "Build2:15:1:;\n" +
                        "Build2:22:1:;\n" +
                        "Asphalt:0:0:>X29;\n" +
                        "Asphalt:0:7:>X29;\n" +
                        "Asphalt:7:0:>Y7;\n" +
                        "Asphalt:14:0:>Y7;\n" +
                        "Asphalt:21:0:>Y7;\n" +
                        "Asphalt:0:0:>Y7;\n" +
                        "Asphalt:28:0:>Y7;";
        try {
            PrintWriter out = new PrintWriter("Map/Structure/streetPoselok.str");
            out.println(dataStr);
            out.close();
        } catch (IOException ignored) {
        }

        dataStr =
                "ObjectMap:0:0:(int)2:(int)2:pepper_object_map:(int)20:(int)20:(int)120:CollisionBreak:true:(int)600:;\n";
        try {
            PrintWriter out = new PrintWriter("Map/ObjectMap/pepper.objM");
            out.println(dataStr);
            out.close();
        } catch (IOException ignored) {
        }
        dataStr =
                "(obj)ObjectMap:14:5:pepper:;\n" +
                        "(obj)ObjectMap:22:10:pepper:;\n" +
                        "(obj)ObjectMap:14:10:pepper:;\n";
        try {
            PrintWriter out = new PrintWriter("Map/Structure/pepper1.str");
            out.println(dataStr);
            out.close();
        } catch (IOException ignored) {
        }

    }
}
