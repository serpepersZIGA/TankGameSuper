package com.mygdx.game.unit.moduleUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.MapFunction.MapBaseAdd;
import com.mygdx.game.Parsing.Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static com.mygdx.game.unit.moduleUnit.RegisterModuleCorpus.CorpusListID;
import static com.mygdx.game.unit.moduleUnit.RegisterModuleEngine.EngineListID;

public class CorpusParser {
    public static int MaxHP,Armor,CorpusWidth,CorpusHeight,Difference;
    public static String Image;
    public static FunctionalList List;
    public static boolean ConfControl;

    public static void ParsCorpus(){
        FileHandle[] files = Gdx.files.internal("ContentGlobal/Module/Corpus").list();
        if(files.length== 0){
            //AddBuilding();
            files = Gdx.files.internal("ContentGlobal/Module/Corpus").list();
        }
        for (FileHandle file: files) {
            //System.out.println(file.path());
            Scan(file.path());
//            for (int i = file.name().length()-4;i<file.name().length();i++){
//                Name = file.name().replace(".bld");
//            }
//            StructBuffer = new boolean[][]{{true,true},
//                    {true,true}};

            CorpusListID.add(new Object[]{
                    new Corpus(file.name().replace(".Corpus","")
                            ,MaxHP,Armor,CorpusWidth,CorpusHeight,Difference,Image,List)
                    ,file.name().replace(".Corpus","")});
            List = new FunctionalList();
            //List.Clear();
            //System.out.println(BuildRegister.BuildingID.size());

        }
        System.out.println(CorpusListID.size());


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
            switch (TotalTxT.trim()) {
                case "MaxHP":
                    obj = Parser.IntegerPars(TxT,i);
                    MaxHP = (int) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;
                case "Armor":
                    obj = Parser.IntegerPars(TxT,i);
                    Armor = (int) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;
                case "CorpusWidth":
                    obj = Parser.IntegerPars(TxT,i);
                    CorpusWidth = (int) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;

                case "CorpusHeight":
                    obj = Parser.IntegerPars(TxT,i);
                    CorpusHeight = (int) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;
                case "Difference":
                    obj = Parser.IntegerPars(TxT,i);
                    Difference = (int) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;
                case "Image":
                    obj = Parser.TextPars(TxT,i);
                    Image = (String) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;
                case "func.Add":
                    obj = Parser.ComponentParsing(TxT,i);
                    FunctionalComponent comp =(FunctionalComponent)obj[0];
                    System.out.println(comp);
                    List.Add(comp);
                    //ConfControl = (boolean) obj[0];
                    i = (int) obj[1];
                    TotalTxT = "";
                    break;

            }
        }


    }
}
