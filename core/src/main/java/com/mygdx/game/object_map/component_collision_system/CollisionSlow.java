package com.mygdx.game.object_map.component_collision_system;

import com.mygdx.game.unit.Unit;

public class CollisionSlow extends ComponentCollisionSystem{
    public CollisionSlow(int x,int y,int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    @Override final
    public void collision(Unit tr, int ix, int iy) {
        if(rect_collision(x,y,width,height,0,
                (int)tr.x,(int)tr.y,(int)tr.corpus_width,(int)tr.corpus_height,tr.rotation_corpus)){
            // a per-frame *0.2 was an instant 80% speed loss on the very
            // first frame of contact, then the throttle immediately starts
            // dragging speed back up next frame - that fight is what read as
            // "jittering forward and back" while driving over ordinary
            // decorative terrain. A gentle per-frame drag still visibly
            // slows you down crossing the patch, just not in a single tick.
            tr.speed *= 0.95f;
            tr.SpeedInertionY *= 0.95f;
            tr.SpeedInertionX *= 0.95f;

        }

    }
}
