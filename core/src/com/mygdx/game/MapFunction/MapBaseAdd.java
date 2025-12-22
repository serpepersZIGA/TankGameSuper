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

        File pepper1 = new File("Map/Structure/pepper1.str");
        File CentralSquare = new File("Map/Structure/CentralSquare.str");
        File MapCentral = new File("Map/maps/MapCentral.mapt");
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
        Create(MapFile,dataMap);
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

        Create(StrFile,dataStr);

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
        Create(Str2File,dataStr);
        dataStr =
                "MapObject:x14:y5:o pepper:;\n" +
                        "MapObject:x22:y10:o pepper:;\n" +
                        "MapObject:x14:y10:o pepper:;";
        Create(pepper1,dataStr);


        dataStr =
                "BuildAdd:B BigBuildingWood1:x8:y1:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x20:y1:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x32:y1:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x44:y1:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x0:y9:r1:;\n" +
                        "BuildAdd:B BigBuildingWood1:x0:y21:r1:;\n" +
                        "BuildAdd:B BigBuildingWood1:x0:y33:r1:;\n" +
                        "BuildAdd:B BigBuildingWood1:x0:y45:r1:;\n" +
                        "Asphalt:x0:y7:X62:;\n" +
                        "Asphalt:x0:y8:X62:;\n" +
                        "Asphalt:x6:y7:Y50:;\n" +
                        "Asphalt:x7:y7:Y50:;\n" +
                        "BuildAdd:B BigBuildingWood1:x56:y9:r1:;\n" +
                        "BuildAdd:B BigBuildingWood1:x56:y21:r1:;\n" +
                        "BuildAdd:B BigBuildingWood1:x56:y33:r1:;\n" +
                        "BuildAdd:B BigBuildingWood1:x56:y45:r1:;\n" +
                        "Asphalt:x54:y7:Y50:;\n" +
                        "Asphalt:x55:y7:Y50:;\n" +
                        "Asphalt:x0:y56:X62:;\n" +
                        "Asphalt:x0:y55:X62:;\n" +
                        "BuildAdd:B BigBuildingWood1:x8:y57:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x20:y57:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x32:y57:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x44:y57:r0:;\n" +
                        "Asphalt:x31:y7:Y50:;\n" +
                        "Asphalt:x30:y7:Y50:;\n" +
                        "BuildAdd:B BigBuildingWood1:x8:y49:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x20:y49:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x32:y49:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x44:y49:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x8:y9:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x20:y9:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x32:y9:r0:;\n" +
                        "BuildAdd:B BigBuildingWood1:x44:y9:r0:;";
        Create(CentralSquare,dataStr);



        dataStr =
                "^MapCentral;\n" +
                        "/x 100:y 100:;\n" +
                        "\n" +
                        "(str):CentralSquare:x 23:y23:;";
        Create(MapCentral,dataStr);

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
