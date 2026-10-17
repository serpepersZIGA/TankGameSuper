package com.mygdx.game.object_map.component_collision_system;

import com.mygdx.game.main.Main;
import com.mygdx.game.Network.PacketMapObject;
import com.mygdx.game.method.Method;
import com.mygdx.game.unit.Unit;

import java.awt.*;
import java.awt.geom.Area;

import static com.mygdx.game.main.Main.LightSystem;

public class CollisionBreak extends ComponentCollisionSystem{
    public CollisionBreak(int x,int y,int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        Rectangle rect1 = new Rectangle(x,y,width,height); // Прямоугольник 1
        this.area = new Area(rect1);
    }
    @Override final
    public void collision(Unit tr, int ix, int iy) {
        if(Method.rect_collision(this.area,tr.area)){
            tr.speed *= 0.2f;
            tr.SpeedInertionY *= 0.2f;
            tr.SpeedInertionX *= 0.2f;
            int n = Main.PacketServer.mapObject.size();
            Main.PacketServer.mapObject.add(new PacketMapObject());
            Main.PacketServer.mapObject.get(n).ix = ix;
            Main.PacketServer.mapObject.get(n).iy = iy;
            LightSystem.lights.remove(Main.BlockList2D.get(iy).get(ix).objMap.light);
            Main.BlockList2D.get(iy).get(ix).objMap = Main.VoidObj;
            //Main.BlockList2D.get(iy).get(ix).objMap.assets = "";
            //Main.BlockList2D.get(iy).get(ix).objMap.lighting

        }

    }
}
