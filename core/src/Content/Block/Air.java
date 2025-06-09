package Content.Block;

import com.mygdx.game.block.Block;

import static java.sql.Types.NULL;

public class Air extends Block {
    public Air(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void all_action(){
        super.UpdateAir();
        radius = NULL;
    }

}
