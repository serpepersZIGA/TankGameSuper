package Content.Block;

import com.mygdx.game.block.UpdateBlock;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.method.rand;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.ContentImage;

public class UpdateGrass extends UpdateBlock {
    @Override final
    public void render(int x,int y){
        RenderMethod.transorm_img(x, y, Main.width_block_zoom, Main.height_block_zoom,TextureAtl.createSprite("grass"));
    }
}
