package Content.Block;

import com.mygdx.game.block.UpdateBlock;

public class UpdateVoid extends UpdateBlock {
    public UpdateVoid(){
        SpeedMod = 0.3f;
    }
    @Override final
    public void render(int x,int y){}
}
