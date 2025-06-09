package com.mygdx.game.object_map.component_collision_system;

import com.mygdx.game.main.Main;
import com.mygdx.game.object_map.PacketMapObject;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.main.Main.LightSystem;

public class CollisionBreak extends ComponentCollisionSystem{
    public int x,y,width,height;
    public CollisionBreak(int x,int y,int width,int height){
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
            int n = Main.PacketServer.mapObject.size();
            Main.PacketServer.mapObject.add(new PacketMapObject());
            Main.PacketServer.mapObject.get(n).ix = ix;
            Main.PacketServer.mapObject.get(n).iy = iy;
            LightSystem.lights.removeValue(Main.BlockList2D.get(iy).get(ix).objMap.light,true);
            Main.BlockList2D.get(iy).get(ix).objMap = Main.VoidObj;

        }

    }
}
