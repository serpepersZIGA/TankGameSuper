package com.mygdx.game.Weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.method.rand;

import java.util.ArrayList;
import java.util.List;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.Batch;
import static com.mygdx.game.main.Main.TimeGlobalBullet;
import static com.mygdx.game.method.RenderMethod.transorm_img;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

public class Rain {
    public static List<Rain> RainList = new ArrayList<Rain>();
    public float x,y,width,height;
    public static float speed;
    public float speedX = 3.5f,speedY = -15;
    public Rain(){
        x = rand.rand(Gdx.graphics.getWidth());
        y = Gdx.graphics.getHeight()+rand.rand(Gdx.graphics.getHeight());
        //speedX = 3.5f;
        width = 6;
        height = 18;
    }
    public void RainIteration(){
        this.y+=speedY*TimeGlobalBullet;
        this.x+=speedX*TimeGlobalBullet;
        update();
        if(y <0){
            speed = (-10f + rand.rand(-7f));
            speedY = (float) (speed*cos(25));
            speedX = (float) (speed*sin(25));
            x = -100+rand.rand(Gdx.graphics.getWidth()+100);
            y = Gdx.graphics.getHeight();
        }
    }
    public void update(){
        transorm_img((int) x, (int) y,width,height,25,TextureAtl.createSprite("Buffer"));
    }
}
