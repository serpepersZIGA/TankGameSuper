package com.mygdx.game.build.Build;
import com.mygdx.game.build.BuildType;
import com.mygdx.game.build.Building;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.RenderMethod;

public class BigBuildingWood1 extends Building {
    public BigBuildingWood1(int x, int y){
        super(x,y,(byte) 1);
        name = "BigBuildingWood1";
        this.x = x;
        this.y = y;
        RenderBuilding = Main.BuildingRegister.Update_big_build_wood1;
        ConstructBuilding = new boolean[][]{
                {true,true,true,true,false,false,true,true,true,true},
                {true,true,true,true,false,false,true,true,true,true},
                {true,true,true,true,false,false,true,true,true,true},
                {true,true,true,true,false,false,true,true,true,true},
                {true,true,true,true,false,false,true,true,true,true},
                {true,true,true,true,false,false,true,true,true,true}
        };

        super.Data();


    }
    @Override final
    public void all_action() {
        super.all_action();
        super.flame_build();

    }
    @Override final
    public void update(){
        RenderBuilding.render(this.x,this.y,this.width_render,this.height_render);
    }
}
