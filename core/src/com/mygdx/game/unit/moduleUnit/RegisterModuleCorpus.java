package com.mygdx.game.unit.moduleUnit;

import com.mygdx.game.FunctionalComponent.FunctionalList;

import static com.mygdx.game.main.Main.ContentImage;
import static com.mygdx.game.main.Main.RegisterFunctionalComponent;

public class RegisterModuleCorpus {
    public static Corpus CorpusT1,CorpusT2,CorpusRemountTrackR1,CorpusSoldatTrackS1;
    public static void Create(){
        FunctionalList func = new FunctionalList();
        func.Add(RegisterFunctionalComponent.TowerIteration);
        func.Add(RegisterFunctionalComponent.BuildCollision);
        func.Add(RegisterFunctionalComponent.TowerXY);
        //this.tower_x_const = (int) (corpus_width/2)-(width_tower/2)-6;
        //this.tower_y_const = (int) (corpus_height/2)-(height_tower/2);
        CorpusT1 = new Corpus(1200,35,50,130,18,ContentImage.corpus_enemy,func);
        CorpusT2 = new Corpus(2400,75,50,130,18,ContentImage.corpus_enemy,func);

        func = new FunctionalList();
        func.Add(RegisterFunctionalComponent.TowerIteration);
        func.Add(RegisterFunctionalComponent.BuildCollision);
        func.Add(RegisterFunctionalComponent.TowerXY);
        CorpusRemountTrackR1 = new Corpus(850,25,50,130,18
                ,ContentImage.corpus_track_remount_enemy,func);
        func.Add(RegisterFunctionalComponent.SoldatSpawn);
        CorpusSoldatTrackS1 = new Corpus(520,15,50,130,18
                ,ContentImage.corpus_track_soldat_enemy,func);

    }

}
