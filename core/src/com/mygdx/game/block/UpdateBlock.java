package com.mygdx.game.block;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.method.rand;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.block.Block.BlockID;

public class UpdateBlock {
    public Sprite img;
    public int ind;
    public boolean GrassGrow;
    public String ID;
    public UpdateBlock(){

    }
    public UpdateBlock(String img,int ind,boolean GrassGrow,String ID){
        this.img = TextureAtl.createSprite(img);
        this.ind = ind;
        this.ID = ID;
        this.GrassGrow = GrassGrow;
    }
    public void render(int x,int y){
        RenderMethod.transorm_img(x, y, Main.width_block_zoom, Main.height_block_zoom,img);

    }
    public void renderTick(int x,int y,int ix,int iy){
        this.render(x,y);
        if(GrassGrow){
            if (rand.rand(20) == 1) {
                Main.BlockList2D.get(iy).get(ix).render_block = BlockID.get(1);
            }
        }
    }

}
