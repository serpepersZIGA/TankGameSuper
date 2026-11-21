package com.mygdx.game.unit.Fire;

import com.mygdx.game.unit.Unit;

public abstract class Fire implements Cloneable{
    public static float rotationTower;
    public void FireIteration(Unit unit){

    }
    public void FireUpdate(Unit unit){
        for(Unit tower : unit.TowerUnitList){
            tower.fire.FireUpdate(tower);
            //tower.indicator_reload();
        }
        for(Unit tower : unit.TrackUnitList){
            //tower.indicator_reload();
            tower.fire.FireUpdate(tower);
        }

    }

    @Override final
    public Fire clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Fire) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
