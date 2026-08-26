package com.mygdx.game.MapFunction;

import com.badlogic.gdx.graphics.Color;

// One procedurally-paintable ground surface: a color and two independent
// movement effects. speedMultiplier is the same kind of gradual per-frame
// decay the old CollisionSlow used (1 = no effect, lower = a stronger drag
// the longer you sit on it - mud/snow/sand). frictionMultiplier is
// different: it scales how much grip Unit.move_xy_transport()'s steering
// and damping terms still have, so a low value (ice) doesn't just make you
// slower, it makes the tank keep coasting/sliding instead of responding
// right away - a proper skid, not a speed penalty.
public enum TerrainMaterial {
    GRASS(new Color(0.30f, 0.42f, 0.23f, 1f), 1f, 1f),
    SNOW(new Color(0.87f, 0.90f, 0.93f, 1f), 0.85f, 1f),
    ICE(new Color(0.78f, 0.88f, 0.90f, 1f), 1f, 0.15f),
    SAND(new Color(0.76f, 0.68f, 0.46f, 1f), 0.82f, 1f),
    GRAVEL(new Color(0.52f, 0.50f, 0.46f, 1f), 0.88f, 0.85f),
    SWAMP(new Color(0.28f, 0.27f, 0.17f, 1f), 0.55f, 1f),
    PUDDLE(new Color(0.22f, 0.27f, 0.26f, 1f), 0.75f, 0.5f),
    ASPHALT(new Color(0.24f, 0.24f, 0.25f, 1f), 1f, 1f);

    public final Color color;
    public final float speedMultiplier;
    public final float frictionMultiplier;

    TerrainMaterial(Color color, float speedMultiplier, float frictionMultiplier){
        this.color = color;
        this.speedMultiplier = speedMultiplier;
        this.frictionMultiplier = frictionMultiplier;
    }
}
