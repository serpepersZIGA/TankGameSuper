package com.mygdx.game.Parsing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class buffObjMap {
    public String Image, Collision;
    public int X,Y,width,height,HP;
    public boolean LightingConf,SpawnUnit;
    public boolean PlayerSpawn;
    public int Lighting;
    // how far above/right of this object's own anchor point the light sits -
    // a lamp post's bulb is near the top, not at its base, so the light
    // shouldn't come from the whole object at once
    public int LightOffsetY;
    public int LightOffsetX;
    // name of a Sound field on DataSound (e.g. "break_wooden", "hit") to play
    // when this object gets crushed by CollisionBreak - empty for silent
    public String CrushSound;
    // true = a solid obstacle a tank can't drive through (uses the same
    // wall-collision system as buildings), instead of the usual
    // slow-down-and-pass-through decor collision
    public boolean Solid;
    // true = a metal object - picks the metallic clang variant of the
    // solid-wall impact sound instead of the default wooden thud
    public boolean Metallic;
}
