package com.mygdx.game.unit;

import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.FunctionalComponent.FunctionalUnit.FunctionalComponentUnitRegister;
import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.unit.CollisionUnit.TypeCollision;
import com.mygdx.game.unit.moduleUnit.*;

import java.util.ArrayList;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.EventData;

public class UnitPattern extends Unit {
    public UnitPattern(String str,Corpus corpus, Engine engine, ArrayList<Cannon> cannon, int[][]TowerXY,ClassUnit classUnit,int medic_help,int Height){
        super(corpus,engine,cannon,TowerXY,classUnit,medic_help,Height);
        TowerUnitList = new ArrayList<>();
        this.TrackUnitList = new ArrayList<>();
        behavior = 3;
        this.collision = TypeCollision.rect;
        corpus.CorpusLoad(this);
        engine.EngineLoad(this);
        ID = str;
        data();
        IDList.put(str,this);
    }
    public UnitPattern(String str, String corpus, String engine, ArrayList<String> cannon, int[][]TowerXY,
            ArrayList<String>track,int[][]TrackXY, ClassUnit classUnit, int HillHp, int Height){
        super(corpus,engine,cannon,TowerXY,track,TrackXY,classUnit,HillHp,Height);
        TowerUnitList = new ArrayList<>();
        this.TrackUnitList = new ArrayList<>();
        behavior = 3;
        this.classUnit = ClassUnit.Transport;
        this.collision = TypeCollision.rect;
        this.CorpusUnit.CorpusLoad(this);
        this.EngineUnit.EngineLoad(this);
        ID = str;
        data();
        IDList.put(str,this);
    }
    public UnitPattern(Cannon cannon,Unit unit){
        super(cannon);
        cannon.XTower = (int) unit.CorpusUnit.CenterCorpusX;
        cannon.YTower = (int) unit.CorpusUnit.CenterCorpusY;
        cannon.CannonLoad(this);
        data_tower();

    }
    public UnitPattern(Track track, Unit unit){
        super(track);
        track.TrackLoad(this);
        data_tower();

    }
    public UnitPattern(Corpus corpus,String str,float x,float y,float rotation,float speed,int Height){
        super(corpus,x,y,rotation,speed,Height);
        corpus.functional = new FunctionalList();
        corpus.functional.Add(FunctionalComponentUnitRegister.MoveDebris);
        corpus.functional.Add(FunctionalComponentUnitRegister.BuildCollision);
        this.TrackUnitList = new ArrayList<>();
        this.TowerUnitList = new ArrayList<>();
        ID = str;
        corpus.CorpusLoad(this);
        data();

    }
    public UnitPattern(String str, Soldat soldat){
        super(soldat);
        ConfSquad = true;
        EventClear = EventData.eventDeadSoldat;
        TowerUnitList = new ArrayList<>();
        ID = str;
        dataSoldat();
        IDList.put(str,this);
    }
    @Override
    public void all_action() {
        super.all_action();
    }
    @Override
    public void all_action_client() {
        super.all_action_client();
    }
    @Override
    public void all_action_client_1() {
        super.all_action_client_1();
    }
    @Override
    public void all_action_client_2() {
        super.all_action_client_2();
    }
    public void UpdateUnit(){
        center_render();
        for(Unit Tower : TrackUnitList){
            Tower.x = x;
            Tower.y = y;
            Tower.rotation_corpus = rotation_corpus;
            Tower.UpdateTrack(this.speed);
        }
        RenderMethod.transorm_img(this.x_rend,this.y_rend,this.corpus_width_zoom,this.corpus_height_zoom
                ,this.rotation_corpus,TextureAtl.createSprite(this.corpus_img),const_x_corpus,const_y_corpus);
        for(Unit Tower : TowerUnitList){
            Tower.x = x;
            Tower.y = y;
            Tower.rotation_corpus = rotation_corpus;
            Tower.UpdateTower();
        }
    }
    public void update(){
        //indicator_reload();
        indicator_hp_2();
    }
    public void updateTower(){
        indicator_reload();
    }
    public void UpdateTower(){
        //this.x = tower_x;
        //this.y = tower_y;
        TowerXY2();
        center_render();
        RenderMethod.transorm_img(this.x_tower_rend,this.y_tower_rend,this.width_tower_zoom,this.height_tower_zoom
                ,this.rotation_tower,TextureAtl.createSprite(this.tower_img),const_x_tower,const_y_tower
        );
    }
    public void UpdateTrack(float speed){
        //this.x = tower_x;
        //this.y = tower_y;
        TowerXY2();
        center_render();
        animator.render(x_tower_rend,y_tower_rend,this.width_tower_zoom,this.height_tower_zoom,
                this.const_x_tower,this.const_y_tower,this.rotation_corpus,speed);
    }
}
