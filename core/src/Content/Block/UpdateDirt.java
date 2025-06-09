package Content.Block;

import com.mygdx.game.block.UpdateBlock;
import com.mygdx.game.block.UpdateRegister;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.method.rand;

import static com.mygdx.game.main.Main.ContentImage;

public class UpdateDirt extends UpdateBlock {
    public void render(int x,int y){
        RenderMethod.transorm_img(x, y, Main.width_block_zoom, Main.height_block_zoom, ContentImage.dirt_2);
    }

    @Override
    public void renderTick(int x, int y,int ix,int iy) {
        super.renderTick(x,y,ix,iy);
        if (rand.rand(20) == 1) {
            Main.BlockList2D.get(iy).get(ix).render_block = UpdateRegister.GrassUpdate;
        }
    }
}
