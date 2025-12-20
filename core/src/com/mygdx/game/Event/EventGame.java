package com.mygdx.game.Event;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.main.ClientMain;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.main.Main.PacketClient;

public abstract class EventGame {
    public EventGame(){

    }
    public static void EventGameClient(String str,int i,int x,int y){
        EventUseClient event = new EventUseClient();
        event.str = str;
        event.ID = i;
        event.x = x;
        event.y = y;
        ClientMain.Client.sendTCP(event);
    }
    public void EventIteration(){

    }
    public void EventIteration(Unit unit){

    }
    public void EventIteration(Bullet bullet){

    }
}
