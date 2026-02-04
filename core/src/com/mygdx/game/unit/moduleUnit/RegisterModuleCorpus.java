package com.mygdx.game.unit.moduleUnit;

import com.mygdx.game.FunctionalComponent.FunctionalList;

import java.util.ArrayList;

import static com.mygdx.game.FunctionalComponent.FunctionalUnit.FunctionalComponentUnitRegister.*;
import static com.mygdx.game.main.Main.ContentImage;
import static com.mygdx.game.main.Main.RegisterFunctionalComponent;

public class RegisterModuleCorpus {
    public static ArrayList<Object[]> CorpusListID = new ArrayList<>();  
    public static Corpus CorpusRemountTrackR1,CorpusSoldatTrackS1;
    public static void Create(){
        FunctionalList func = new FunctionalList();
        func.Add(TowerIteration);
        func.Add(BuildCollision);
        func.Add(TowerXY);

        func = new FunctionalList();
        func.Add(TowerIteration);
        func.Add(BuildCollision);
        func.Add(TowerXY);
        CorpusRemountTrackR1 = new Corpus(520,12,50,130,18
                ,"machine_enemy_1lvl",func);
        func.Add(SoldatSpawn);
        CorpusSoldatTrackS1 = new Corpus(520,12,50,130,18
                ,"machine_enemy_1lvl",func);

    }

}
