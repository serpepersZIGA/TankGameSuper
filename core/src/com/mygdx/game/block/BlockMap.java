package com.mygdx.game.block;

import com.mygdx.game.block.Block;
import com.mygdx.game.block.UpdateRegister;
import com.mygdx.game.main.Main;


public class BlockMap extends Block {
    public BlockMap(int x, int y){
        objMap = Main.VoidObj;
        this.x = x;
        this.y = y;
        block_xy();
        this.render_block = UpdateRegister.GrassUpdate;
    }
}
