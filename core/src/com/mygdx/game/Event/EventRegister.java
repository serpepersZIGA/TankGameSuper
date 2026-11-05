package com.mygdx.game.Event;

public class EventRegister {
    public EventGame eventDeadSoldat,eventDeadTransport,eventDeadDebris,eventUseItemClient;
    public EventRegister(){
        eventDeadSoldat = new EventDeadSoldat();
        eventDeadTransport = new EventDeadTransport();
        eventDeadDebris = new EventDeadDebris();
    }
}
