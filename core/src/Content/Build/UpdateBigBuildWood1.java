package Content.Build;

import com.mygdx.game.build.Building;
import com.mygdx.game.build.UpdateBuilding;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.RenderMethod;

import static com.mygdx.game.main.Main.ContentImage;

public class UpdateBigBuildWood1 extends UpdateBuilding {
    private static int[]xy;
    public void render(int x,int y,int width,int height){
        xy = Building.center_render(x,y);
        RenderMethod.transorm_img((int) (xy[0]* Main.Zoom),(int) (xy[1]* Main.Zoom),width,height,ContentImage.big_build_wood_1);
    }
}
