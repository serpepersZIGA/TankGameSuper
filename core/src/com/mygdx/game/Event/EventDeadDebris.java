package com.mygdx.game.Event;

import com.mygdx.game.unit.Unit;

public class EventDeadDebris extends EventGame{
    @Override
    public void EventIteration(Unit unit) {
        unit.DebrisDelete();
    }
}
