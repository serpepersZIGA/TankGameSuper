package com.mygdx.game.object_map.component_collision_system;

import com.mygdx.game.unit.Unit;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public abstract class ComponentCollisionSystem {
    public int x,y,width,height;
    public Area area;
    public ComponentCollisionSystem(){

    }
    public ComponentCollisionSystem(int x,int y,int width,int height){

    }
    public void collision(Unit tr, int ix, int iy){
    }
}
