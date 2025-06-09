package com.mygdx.game.unit.moduleUnit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.unit.Unit;

public class Corpus extends moduleUnit implements Cloneable{
    public int max_hp,hp;
    public FunctionalList functional;
    public int armor;
    public int corpus_width,corpus_width_2;
    public int corpus_height,corpus_height_2;
    public float RotationCorpus;
    public Sprite image;
    public float CenterCorpusX;
    public float CenterCorpusY;
    public int Difference;
    public Corpus(int max_hp, int armor, int corpus_width, int corpus_height,int Difference, Sprite image,
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
    public void CorpusLoad(Unit unit){
        unit.max_hp = max_hp;
        unit.hp = hp;
        unit.armor = armor;
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
