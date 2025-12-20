package com.mygdx.game.Event;

import com.mygdx.game.bull.Bullet;
import com.mygdx.game.unit.Unit;

import java.util.ArrayList;

public class EventList {
    public EventList(){
        List = new ArrayList<>();
    }
    public ArrayList<EventGame> List;
    public void AddEvent(EventGame Event){
        List.add(Event);
    }
    public void AddEvent(EventList Event){
        List.addAll(Event.List);
    }
    public void RemoveEvent(int i){
        List.remove(i);
    }
    public void EventIteration(){
        for(EventGame event : List){
            event.EventIteration();
        }

    }
    public void EventIteration(Unit unit){
        for(EventGame event : List){
            event.EventIteration(unit);
        }
    }
    public void EventIteration(Bullet bullet){
        for(EventGame event : List){
            event.EventIteration(bullet);
        }
    }

}
