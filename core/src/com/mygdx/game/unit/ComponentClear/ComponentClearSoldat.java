package com.mygdx.game.unit.ComponentClear;

import Content.Particle.Blood;
import com.mygdx.game.main.Main;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.main.Main.UnitList;

public class ComponentClearSoldat extends ComponentClear{
    public void Clear(Unit unit){
        for(int i1 =0;i1<12;i1++){
            Main.LiquidList.add(new Blood(unit.x+i1, unit.y));
        }
        UnitList.remove(unit);
    }
}
