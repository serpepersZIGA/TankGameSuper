package com.mygdx.game.object_map;

import com.mygdx.game.object_map.component_collision_system.CollisionVoid;

public class VoidObject extends MapObject {
    public VoidObject() {
        Collision = new CollisionVoid();
        lighting = false;
        light = null;
        assets = "";



    }
    @Override final
    public void render() {
    }

}
