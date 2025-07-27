package com.mygdx.game.unit.moduleUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.MapFunction.MapBaseAdd;
import com.mygdx.game.Parsing.Parser;
import com.mygdx.game.build.BuildRegister;
import com.mygdx.game.build.Building;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static com.mygdx.game.unit.moduleUnit.RegisterModuleEngine.EngineListID;

public class EngineParser {
    public static float MaxSpeed,MinSpeed,Acceleration,SpeedRotation;
    public static boolean ConfControl;

    public static void ParsEngine(){
        FileHandle[] files = Gdx.files.internal("ContentGlobal/Module/Engine").list();
        if(files.length== 0){
            //AddBuilding();
            files = Gdx.files.internal("ContentGlobal/Module/Engine").list();
        }
        for (FileHandle file: files) {
            //System.out.println(file.path());
            Scan(file.path());
//            for (int i = file.name().length()-4;i<file.name().length();i++){
//                Name = file.name().replace(".bld");
//            }
//            StructBuffer = new boolean[][]{{true,true},
//                    {true,true}};

            EngineListID.add(new Object[]{
                    new Engine(file.name().replace(".Engine","")
                            ,MaxSpeed,MinSpeed,Acceleration,SpeedRotation,ConfControl)
                    ,file.name().replace(".Engine","")});
            //System.out.println(BuildRegister.BuildingID.size());

        }


    }
    public static void Scan(String Path) {
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
            switch (TotalTxT.trim()) {
                case "MaxSpeed":
                    obj = Parser.FloatPars(TxT,i);
                    MaxSpeed = (float) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;
                case "MinSpeed":
                    obj = Parser.FloatPars(TxT,i);
                    MinSpeed = (float) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;
                case "Acceleration":
                    obj = Parser.FloatPars(TxT,i);
                    Acceleration = (float) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;

                case "SpeedRotation":
                    obj = Parser.FloatPars(TxT,i);
                    SpeedRotation = (float) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;
                case "ConfControl":
                    obj = Parser.BooleanPars(TxT,i);
                    ConfControl = (boolean) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;

            }
        }


    }
}


