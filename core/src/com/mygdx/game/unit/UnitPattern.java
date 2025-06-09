package com.mygdx.game.unit;

import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.unit.CollisionUnit.TypeCollision;
import com.mygdx.game.unit.moduleUnit.Cannon;
import com.mygdx.game.unit.moduleUnit.Corpus;
import com.mygdx.game.unit.moduleUnit.Engine;
import com.mygdx.game.unit.moduleUnit.Soldat;

import java.util.ArrayList;

import static com.mygdx.game.main.Main.EventData;
import static com.mygdx.game.main.Main.RegisterFunctionalComponent;

public class UnitPattern extends Unit {
    public UnitPattern(String str,Corpus corpus, Engine engine, ArrayList<Cannon> cannon, int[][]TowerXY,ClassUnit classUnit,int medic_help){
        super(corpus,engine,cannon,TowerXY,classUnit,medic_help);
        tower_obj = new ArrayList<>();
        behavior = 3;
        this.collision = TypeCollision.rect;
        corpus.CorpusLoad(this);
        engine.EngineLoad(this);
        ID = str;
        data();
        IDList.add(new Object[]{this,str});
    }
    public UnitPattern(Cannon cannon,Unit unit){
        super(cannon,unit);
        cannon.XTower = (int) unit.CorpusUnit.CenterCorpusX;
        cannon.YTower = (int) unit.CorpusUnit.CenterCorpusY;
        cannon.CannonLoad(this);
        data_tower();

    }
    public UnitPattern(Corpus corpus,String str,float x,float y,float rotation,float speed,float SpeedInert,float RotationInert){
        super(corpus,x,y,rotation,speed,SpeedInert,RotationInert);
        corpus.functional = new FunctionalList();
        corpus.functional.Add(RegisterFunctionalComponent.MoveDebris);
        corpus.functional.Add(RegisterFunctionalComponent.BuildCollision);
        this.tower_obj = new ArrayList<>();
        ID = str;
        corpus.CorpusLoad(this);
        data();

    }
    public UnitPattern(String str, Soldat soldat){
        super(soldat);
        EventClear = EventData.eventDeadSoldat;
        tower_obj = new ArrayList<>();
        ID = str;
        dataSoldat();
        IDList.add(new Object[]{this,str});
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
        RenderMethod.transorm_img(this.x_rend,this.y_rend,this.corpus_width_zoom,this.corpus_height_zoom,this.rotation_corpus,this.corpus_img,const_x_corpus,const_y_corpus);
        for(Unit Tower : tower_obj){
            Tower.x = x;
            Tower.y = y;
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
        RenderMethod.transorm_img(this.x_tower_rend,this.y_tower_rend,this.width_tower_zoom,this.height_tower_zoom,this.rotation_tower,this.tower_img,const_x_tower,const_y_tower
        );
    }
}
