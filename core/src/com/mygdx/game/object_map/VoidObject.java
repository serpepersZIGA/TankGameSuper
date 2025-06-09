package com.mygdx.game.object_map;

import com.mygdx.game.object_map.component_collision_system.CollisionVoid;
import com.mygdx.game.object_map.component_collision_system.ComponentCollisionSystem;

public class VoidObject extends MapObject {
    public VoidObject() {
        Collision = new CollisionVoid();
    }
    public void render() {
    }

}
