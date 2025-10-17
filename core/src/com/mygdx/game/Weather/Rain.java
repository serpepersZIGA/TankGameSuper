package com.mygdx.game.Weather;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.method.rand;

import java.util.ArrayList;
import java.util.List;

public class Rain {
    public static List<Rain> RainList = new ArrayList<Rain>();
    public float x,y,width,height;
    public float speedX = 1.2f,speedY = -15;
    public Rain(){
        x = rand.rand(Gdx.graphics.getWidth());
        y = Gdx.graphics.getHeight()+rand.rand(Gdx.graphics.getHeight());
        width = 6;
        height = 18;
    }
    public void RainIteration(){
        this.y+=speedY;
        this.x+=speedX;
        if(y <0){
            speedY = -10;
            speedY += rand.rand(-7);
            x = rand.rand(Gdx.graphics.getWidth());
            y = Gdx.graphics.getHeight();
        }
    }
}
