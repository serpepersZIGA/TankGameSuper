package Content.Build;
import com.mygdx.game.build.BuildType;
import com.mygdx.game.build.Building;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.RenderMethod;

public class BigBuildingWood1 extends Building {
    public BigBuildingWood1(int x, int y){
        name = BuildType.BigBuildingWood1;
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
    public void all_action(int i) {
        super.all_action(i);
        super.flame_build(Main.FlameStaticList);

    }
    public void update(){
        RenderBuilding.render(this.x,this.y,this.width_render,this.height_render);
    }
}
