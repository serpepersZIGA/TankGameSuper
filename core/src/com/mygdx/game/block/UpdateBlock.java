package com.mygdx.game.block;

public abstract class UpdateBlock {
    public void render(int x,int y){
    }
    public void renderTick(int x,int y,int ix,int iy){
        this.render(x,y);
    }

}
