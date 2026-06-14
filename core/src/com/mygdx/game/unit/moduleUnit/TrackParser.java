package com.mygdx.game.unit.moduleUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.MapFunction.MapBaseAdd;
import com.mygdx.game.Parsing.Parser;

import java.io.*;

import static com.mygdx.game.FunctionalComponent.FunctionalUnit.FunctionalComponentUnitRegister.TowerXY;
import static com.mygdx.game.unit.moduleUnit.RegisterModuleCorpus.CorpusListID;
import static com.mygdx.game.unit.moduleUnit.RegisterModuleEngine.EngineListID;
import static com.mygdx.game.unit.moduleUnit.RegisterModuleTrack.TrackID;

public class TrackParser {
    public static int MaxHP,Armor,TrackWidth,TrackHeight,Difference,CorrectX,CorrectY;
    public static String Images,ImageInvert;
    public static FunctionalList List;
    public static boolean ConfControl;

    public static void ParsTrack(){
        FileHandle[] files = Gdx.files.internal("ContentGlobal/Module/Track").list();
        if(files.length== 0){
            AddBuilding();
            files = Gdx.files.internal("ContentGlobal/Module/Track").list();
        }
        for (FileHandle file: files) {
            //System.out.println(file.path());
            Scan(file.path());
//            for (int i = file.name().length()-4;i<file.name().length();i++){
//                Name = file.name().replace(".bld");
//            }
//            StructBuffer = new boolean[][]{{true,true},
//                    {true,true}};

            TrackID.put(file.name().replace(".Track",""),
                    new Track(file.name().replace(".Track","")
                            ,TrackWidth,TrackHeight
                            ,Images,ImageInvert));
            List = new FunctionalList();
            //List.Clear();
            //System.out.println(BuildRegister.BuildingID.size());

        }
        //System.out.println(CorpusListID.size());


    }
    public static void Scan(String Path) {
        List = new FunctionalList();
        List.functional.add(TowerXY);
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
                case "TrackWidth":
                    obj = Parser.IntegerPars(TxT,i);
                    TrackWidth = (int) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;

                case "TrackHeight":
                    obj = Parser.IntegerPars(TxT,i);
                    TrackHeight = (int) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;

                case "Images":
                    obj = Parser.TextPars(TxT,i);
                    Images = (String) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;
                case "ImageInvert":
                    obj = Parser.TextPars(TxT,i);
                    ImageInvert = (String) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;

            }
        }


    }
    public static void AddBuilding(){
        new File("ContentGlobal").mkdirs();
        new File("ContentGlobal/Module").mkdirs();
        new File("ContentGlobal/Module/Track").mkdirs();
        File Track = new File("ContentGlobal/Module/Track/TrackPz2.Track");
        String data = "TrackWidth = 15;\n" +
                "TrackHeight = 130;\n" +
                "Images = image/animation/AnimationTrackUp.png;\n" +
                "ImageInvert = image/animation/animationTrack.png;";
        Create(Track,data);



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
