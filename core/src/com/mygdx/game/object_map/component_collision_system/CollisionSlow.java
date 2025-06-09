package com.mygdx.game.object_map.component_collision_system;

import com.mygdx.game.unit.Unit;

public class CollisionSlow extends ComponentCollisionSystem{
    public int x,y,width,height;
    public CollisionSlow(int x,int y,int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    @Override
    public void collision(Unit tr, int ix, int iy) {
        if(rect_collision(x,y,width,height,0,
                (int)tr.x,(int)tr.y,(int)tr.corpus_width,(int)tr.corpus_height,tr.rotation_corpus)){
            tr.speed /= 5;

        }

    }
}
