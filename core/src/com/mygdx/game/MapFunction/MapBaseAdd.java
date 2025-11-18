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
                "/x 120:y 120:;\n" +
                "BuildAdd:B BigBuildingWood1:x7:y10:r1:;\n" +
                "(str):street1:x 23:y23:;\n" +
                "(str):street1:x23:y40:;\n" +
                "(str):street1:x69:y23:;\n" +
                "(str):street1:x69:y40:;\n" +
                "(str):streetPoselok:x70:y60:;\n" +
                "Asphalt:x70:y53:Y7:;\n" +
                "(str):pepper1:x12:y12:;";
        try {
            PrintWriter out = new PrintWriter("Map/maps/MapBase.mapt");
            out.println(dataMap);
            out.close();
        } catch (IOException ignored) {
        }
        String dataStr =
                "BuildAdd:B BigBuildingWood1:x0:y1:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x12:y1:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x24:y1:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x36:y1:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x0:y10:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x12:y10:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x24:y10:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x36:y10:r0:;\n" +
                        "Asphalt:x0:y8:X46:;\n" +
                        "Asphalt:x0:y9:X46:;\n" +
                        "Asphalt:x0:y7:X46:;\n" +
                        "Asphalt:x0:y16:X46:;\n" +
                        "Asphalt:x0:y0:X46:;\n" +
                        "Asphalt:x10:y1:Y15:;\n" +
                        "Asphalt:x11:y1:Y15:;\n" +
                        "Asphalt:x22:y1:Y15:;\n" +
                        "Asphalt:x23:y1:Y15:;\n" +
                        "Asphalt:x34:y1:Y15:;\n" +
                        "Asphalt:x35:y1:Y15:;\n";

        try {
            PrintWriter out = new PrintWriter("Map/Structure/street1.str");
            out.println(dataStr);
            out.close();
        } catch (IOException ignored) {
        }

        dataStr =
                "BuildAdd:B Building2:x1:y1:;\n" +
                        "BuildAdd:B Building2:x8:y1:;\n" +
                        "BuildAdd:B Building2:x15:y1:;\n" +
                        "BuildAdd:B Building2:x22:y1:;\n" +
                        "Asphalt:x0:y0:X29:;\n" +
                        "Asphalt:x0:y7:X29:;\n" +
                        "Asphalt:x7:y0:Y7:;\n" +
                        "Asphalt:x14:y0:Y7:;\n" +
                        "Asphalt:x21:y0:Y7:;\n" +
                        "Asphalt:x0:y0:Y7:;\n" +
                        "Asphalt:x28:y0:Y7:;";
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
                "MapObject:x14:y5:o pepper:;\n" +
                        "MapObject:x22:y10:o pepper:;\n" +
                        "MapObject:x14:y10:o pepper:;";
        try {
            PrintWriter out = new PrintWriter("Map/Structure/pepper1.str");
            out.println(dataStr);
            out.close();
        } catch (IOException ignored) {
        }

    }
}
