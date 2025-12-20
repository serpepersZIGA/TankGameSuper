package com.mygdx.game.unit.moduleUnit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.unit.Unit;

import java.util.Objects;

import static com.mygdx.game.unit.moduleUnit.RegisterModuleCorpus.CorpusListID;
import static com.mygdx.game.unit.moduleUnit.RegisterModuleEngine.EngineListID;

public class Corpus extends moduleUnit implements Cloneable{
    public int max_hp;
    public FunctionalList functional;
    public int armor;
    public int corpus_width,corpus_width_2;
    public int corpus_height,corpus_height_2;
    public float RotationCorpus;
    public String image;
    public String ID;
    public float CenterCorpusX;
    public float CenterCorpusY;
    public int Difference;
    public Corpus(int max_hp, int armor, int corpus_width, int corpus_height,int Difference, String image,
    FunctionalList func){
        this.max_hp = max_hp;
        this.Difference = Difference;
        this.armor = armor;
        this.corpus_width = corpus_width;
        this.corpus_height = corpus_height;

        this.corpus_width_2 = corpus_width/2;
        this.corpus_height_2 = corpus_height/2;
        CenterCorpusX = corpus_width_2;
        CenterCorpusY = corpus_height_2;

        this.image = image;
        this.functional = func.clone();
    }
    public Corpus CorpusAdd(){
        try {
            return (Corpus) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    public static Corpus CorpusAdd(String string){
        try {
            for(Object[] corpus : CorpusListID) {
                if(Objects.equals(corpus[1], string)) {
                    Corpus corp = (Corpus)corpus[0];
                    return (Corpus) corp.clone();
                }
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(string);
        throw new RuntimeException();
    }
    public Corpus(String ID,int max_hp, int armor, int corpus_width, int corpus_height,
                  int Difference, String image,FunctionalList func){
        this.ID = ID;
        CorpusListID.add(new Object[]{this,ID});
        this.max_hp = max_hp;
        this.Difference = Difference;
        this.armor = armor;
        this.corpus_width = corpus_width;
        this.corpus_height = corpus_height;

        this.corpus_width_2 = corpus_width/2;
        this.corpus_height_2 = corpus_height/2;
        this.CenterCorpusX = corpus_width_2;
        this.CenterCorpusY = corpus_height_2;

        this.image = image;
        this.functional = func.clone();
    }
    public void CorpusLoad(Unit unit){
        unit.max_hp = max_hp;
        unit.HpBase = max_hp;
        unit.hp = max_hp;
        unit.armor = armor;
        unit.ArmorBase = armor;
        unit.corpus_width = corpus_width;
        unit.corpus_height = corpus_height;

        unit.corpus_width_2 = corpus_width_2;
        unit.corpus_height_2 = corpus_height_2;

        unit.tower_x_const = CenterCorpusX;
        unit.tower_y_const = CenterCorpusY;
        unit.corpus_img = image;
        unit.difference = Difference;
        for(int i = 0;i<functional.functional.size();i++){
            unit.functional.Add(functional.functional.get(i));
        }

    }

}
