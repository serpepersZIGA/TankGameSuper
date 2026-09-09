package com.mygdx.game.unit.moduleUnit;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.method.Animator;
import com.mygdx.game.unit.Fire.Fire;
import com.mygdx.game.unit.Unit;

import java.util.Objects;

import static com.mygdx.game.FunctionalComponent.FunctionalUnit.FunctionalComponentUnitRegister.TowerXY;
import static com.mygdx.game.method.Method.RP;
import static com.mygdx.game.method.pow2.pow2;
import static com.mygdx.game.unit.moduleUnit.RegisterModuleCannon.CannonListID;
import static com.mygdx.game.unit.moduleUnit.RegisterModuleTrack.TrackID;
import static java.lang.StrictMath.atan2;
import static java.lang.StrictMath.sqrt;

public class Track implements Cloneable{
    public int WidthTrack, WidthTrack2;
    public int HeightTrack, HeightTrack2;
    public int ConstTowerX;
    public int ConstTowerY;
    public String ID;
    public FunctionalList functional = new FunctionalList();
    public int differenceY, differenceX,CenterX,CenterY,AmountFragment;
    public Sound sound;
    public String image;
    public Animator animator;
    public byte Side;
    public float differenceTotal,RotationDifference;

    public Track(String id,int WidthTrack, int HeightTrack
                  , String image,String imageInvert){
        this.ID = id;
        this.WidthTrack = WidthTrack;
        this.HeightTrack = HeightTrack;
        WidthTrack2 = (int) (WidthTrack *0.5f);
        HeightTrack2 = (int) (HeightTrack *0.5f);
        animator = new Animator(image,imageInvert,4,2);


        this.image =  image;


        //IDList.add(ID);


    }

    public Track TrackAdd(Unit unit, int differenceX, int differenceY){
        Track TrackAdd;
        try {
            TrackAdd = (Track) this.clone();
            TrackAdd.differenceX = differenceX;
            TrackAdd.differenceY = differenceY;

            TrackAdd.CenterX = unit.CorpusUnit.corpus_width_2-TrackAdd.WidthTrack2;
            TrackAdd.CenterY = unit.CorpusUnit.corpus_height_2-TrackAdd.HeightTrack2;
            
            //CannonAdd.ConstTowerX = unit.const_tower_x;
            //CannonAdd.ConstTowerY = unit.const_tower_y;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return TrackAdd;
    }
    public static Track TrackAdd(String string,Unit unit,int differenceX,int differenceY,int Side){
        Track track = null;
        try {
            track = TrackID.get(string);
            track = (Track) track.clone();
            track.differenceX = differenceX;
            track.differenceY = differenceY;

            track.differenceTotal = (float) sqrt(pow2(differenceX)+pow2(differenceY));
            //float[]xy = Method.tower_xy_2(0,0,)
            track.RotationDifference = (float) (atan2(differenceY,differenceX)*RP);
            track.Side = (byte)Side;

            track.CenterX = unit.CorpusUnit.corpus_width_2-track.WidthTrack2;
            track.CenterY = unit.CorpusUnit.corpus_height_2-track.HeightTrack2;
            //CannonAdd.ConstTowerX = unit.const_tower_x;
            //CannonAdd.ConstTowerY = unit.const_tower_y;
            return (Track) track.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

    public void TrackLoad(Unit unit){
        unit.functional = new FunctionalList();
        unit.tower_img = image;

        unit.width_tower = WidthTrack;
        unit.height_tower = HeightTrack;
        unit.tower_width_2 = WidthTrack2;
        unit.tower_height_2 = HeightTrack2;
        unit.tower_x_const =CenterX;
        unit.tower_y_const = CenterY;
        unit.difference_2 = differenceX;
        unit.difference = differenceY;

        unit.differenceTotal = differenceTotal;
        unit.RotationDifference = RotationDifference;




        unit.const_tower_x = ConstTowerX;
        unit.const_tower_y = ConstTowerY;
        unit.functional.functional.add(TowerXY);
        unit.animator = animator.clone();


        //unit.RotateNotTower = RotateBase;


    }
}
