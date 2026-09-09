package com.mygdx.game.Weather;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.method.rand;

import java.util.ArrayList;
import java.util.List;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.TimeGlobalBullet;
import static com.mygdx.game.method.RenderMethod.transorm_img;

// Same falling-particle shape as Rain, but slower, drifting side to side
// instead of streaking, and tinted near-white instead of drawn as a blue
// streak - spawned instead of Rain when the local player is standing in a
// cold biome (see WeatherMainSystem).
public class Snow {
    public static List<Snow> SnowList = new ArrayList<>();
    public float x, y, width, height;
    private float swayPhase;
    private float swaySpeed;
    private float fallSpeed;

    public Snow(){
        x = rand.rand(Gdx.graphics.getWidth());
        y = Gdx.graphics.getHeight()+rand.rand(Gdx.graphics.getHeight());
        width = 5; height = 5;
        swayPhase = rand.rand(6.28f);
        swaySpeed = 0.6f+rand.rand(0.8f);
        fallSpeed = 2.5f+rand.rand(2.5f);
    }
    public void SnowIteration(){
        swayPhase += swaySpeed*TimeGlobalBullet*0.02f;
        this.y -= fallSpeed*TimeGlobalBullet;
        this.x += (float) Math.sin(swayPhase)*1.2f*TimeGlobalBullet;
        update();
        if (y < -height){
            y = Gdx.graphics.getHeight()+height;
            x = rand.rand(Gdx.graphics.getWidth());
        }
    }
    public void update(){
        transorm_img((int) x, (int) y, width, height, TextureAtl.createSprite("Buffer"));
    }
}
