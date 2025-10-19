package com.mygdx.game.unit.Fire;

import com.mygdx.game.unit.Unit;

public abstract class Fire implements Cloneable{
    public static float rotationTower;
    public void FireIteration(Unit unit){

    }

    @Override
    public Fire clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Fire) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
