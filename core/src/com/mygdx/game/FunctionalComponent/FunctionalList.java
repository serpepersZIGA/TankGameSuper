package com.mygdx.game.FunctionalComponent;

import com.mygdx.game.bull.Bullet;
import com.mygdx.game.unit.Unit;

import java.util.ArrayList;

public class FunctionalList implements Cloneable{
    public ArrayList<FunctionalComponent> functional= new ArrayList<>();
    public void Add(FunctionalComponent component){
        functional.add(component);
    }
    public void Remove(FunctionalComponent component){
        functional.remove(component);
    }
    public void Clear(){
        functional.clear();
    }
    public void FunctionalIterationAnHost(Unit unit){
        for(FunctionalComponent func : functional){
            func.FunctionalIterationAnHost(unit);
        }
    }
    public void FunctionalIterationClientAnHost(Unit unit){
        for(FunctionalComponent func : functional){
            func.FunctionalIterationClientAnHost(unit);
        }
    }
    public void FunctionalIterationAnClient(Unit unit){
        for(FunctionalComponent func : functional){
            func.FunctionalIterationAnClient(unit);
        }
    }
    public void FunctionalIterationOtherAnClient(Unit unit){
        for(FunctionalComponent func : functional){
            func.FunctionalIterationOtherAnClient(unit);
        }
    }




    public void FunctionalIterationAnHost(Bullet bullet){
        for(FunctionalComponent func : functional){
            func.FunctionalIterationAnHost(bullet);
        }
    }
    public void FunctionalIterationClientAnHost(Bullet bullet){
        for(FunctionalComponent func : functional){
            func.FunctionalIterationClientAnHost(bullet);
        }
    }
    public void FunctionalIterationAnClient(Bullet bullet){
        for(FunctionalComponent func : functional){
            func.FunctionalIterationAnClient(bullet);
        }
    }
    public void FunctionalIterationOtherAnClient(Bullet bullet){
        for(FunctionalComponent func : functional){
            func.FunctionalIterationOtherAnClient(bullet);
        }
    }

    @Override
    public FunctionalList clone() {
        try {
            FunctionalList clone = (FunctionalList) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
