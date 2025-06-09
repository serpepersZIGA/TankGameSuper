package com.mygdx.game.unit.ComponentClear;

import com.mygdx.game.unit.Unit;

public class ComponentClearTransport extends ComponentClear {
    public void Clear(Unit unit){
        unit.transportDelete();

    }
}
