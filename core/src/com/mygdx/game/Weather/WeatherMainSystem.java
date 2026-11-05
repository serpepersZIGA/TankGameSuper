package com.mygdx.game.Weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.Shader.LightingMainSystem;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.rand;

import java.util.ArrayList;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.Weather.Rain.RainList;
import static com.mygdx.game.Weather.Ripple.RippleList;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.CycleTimeDay.CycleDay;

public class WeatherMainSystem {
    public static ShaderProgram shader,shaderRipple;
    public static int WeatherGlobal;
    public static float time;



    public static void WeatherMainSystemAdd(){

        WeatherGlobal = rand.rand(2);
        ShaderProgram.pedantic = false;

        String vertSrc = Gdx.files.internal("ShaderList/Rain/Rain.vert").readString();
        String fragSrc = Gdx.files.internal("ShaderList/Rain/Rain.frag").readString();

        String vertSrcRipple = Gdx.files.internal("ShaderList/Ripple/Ripple.vert").readString();
        String fragSrcRipple = Gdx.files.internal("ShaderList/Ripple/Ripple.frag").readString();

        shaderRipple = new ShaderProgram(vertSrcRipple, fragSrcRipple);
        if (!shaderRipple.isCompiled()) {
            throw new GdxRuntimeException("Rain shader compile error: " + shaderRipple.getLog());
        }
        for(int i = 0; i < 100; i++) {
            RainList.add(new Rain());
        }
        ShaderProgram.pedantic = false;

        shader = new ShaderProgram(vertSrc, fragSrc);
        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Rain shader compile error: " + shader.getLog());
        }
    }
    public static void  WeatherCycle(){
        WeatherGlobal = rand.rand(2);
    }
    public static void  RippleIteration(SpriteBatch batch){
        switch (WeatherGlobal){
            case 0:{

            }
            break;
            case 1:{
                // Рендерим эффект дождя
                WeatherRipple(batch);

            }
            break;
        }
    }




    public static void WeatherRipple(SpriteBatch batch) {
        batch.end();
        batch.begin();
        batch.setShader(shaderRipple);
        time += TimeGlobal;
        shaderRipple.setUniformMatrix("u_projTrans",Batch.getProjectionMatrix());
        shaderRipple.setUniformf("u_resolution",screenWidth, screenHeight);
        shaderRipple.setUniformf("u_time", time);
        Ripple.RippleSpawn();
        for(int i = 0;Ripple.RippleList.size()>i;i++){
            RippleList.get(i).render();
        }
        batch.end();
        batch.begin();
        batch.setShader(LightSystem.shader);
        //batch.flush();



//        batch.draw(new Texture("buffer2.png"),0,0,screenWidth,screenHeight);
//        batch.end();

    }
    public static void  WeatherIteration(SpriteBatch batch){
        switch (WeatherGlobal){
            case 0:{

            }
            break;
            case 1:{
                // Рендерим эффект дождя
                WeatherRain(batch);

            }
            break;
        }
    }
    public static void WeatherRain(SpriteBatch batch) {
        batch.begin();
        batch.setShader(shader);
        shader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (int i = 0; i < RainList.size(); i++) {
            Rain rain = RainList.get(i);
            rain.RainIteration();
//            shader.setUniformMatrix("u_projTrans",Batch.getProjectionMatrix());
//            shader.setUniformf("u_rain["+i+"].position", new Vector2(rain.x,rain.y));
//            shader.setUniformf("u_rain["+i+"].width", rain.width);
//            shader.setUniformf("u_rain["+i+"].height", rain.height);
        }


        //batch.draw(TextureAtl.createSprite("Buffer"),0,0,screenWidth,screenHeight);
        batch.end();

    }
    public static void end(){
        shader.end();
    }



}
