package Content.Build;
import com.mygdx.game.build.BuildType;
import com.mygdx.game.build.Building;
import com.mygdx.game.main.Main;

import com.mygdx.game.method.RenderMethod;

public class Home1 extends Building {
    public Home1(int x, int y){
        name = BuildType.Home1;
        this.x = x;
        this.y = y;
        RenderBuilding = Main.BuildingRegister.Update_big_build_wood1;
        this.build_image = Main.ContentImage.big_build_wood_1;
        ConstructBuilding = new boolean[][]{
                {true,true,true,true,true,false,true,true,true,true},
                {true,false,false,false,false,false,false,false,false,true},
                {true,false,false,false,false,false,false,false,false,true},
                {true,false,false,false,false,false,false,false,false,true},
                {true,false,false,false,false,false,false,false,false,true},
                {true,true,true,true,true,true,true,true,true,true}
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
