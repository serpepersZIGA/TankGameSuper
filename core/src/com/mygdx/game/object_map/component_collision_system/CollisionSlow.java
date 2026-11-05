package com.mygdx.game.object_map.component_collision_system;

import com.mygdx.game.method.Method;
import com.mygdx.game.unit.Unit;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class CollisionSlow extends ComponentCollisionSystem{
    public CollisionSlow(int x,int y,int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        Rectangle rect1 = new Rectangle(x,y,width,height);
        this.area = new Area(rect1);
    }
    @Override final
    public void collision(Unit tr, int ix, int iy) {
        if(Method.rect_collision(this.area,tr.area)){
            //tr.speed *= 0.2f;
            //this.area.reset();
            tr.SpeedInertionY *= 0.5f;
            tr.SpeedInertionX *= 0.5f;

        }

    }
}
