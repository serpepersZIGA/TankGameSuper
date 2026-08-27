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
import static com.mygdx.game.Weather.Snow.SnowList;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.CycleTimeDay.CycleDay;

public class WeatherMainSystem {
    public static ShaderProgram shader,shaderRipple,shaderSnow;
    public static int WeatherGlobal;
    public static float time;



    public static void WeatherMainSystemAdd(){

        WeatherGlobal = rand.rand(2);
        ShaderProgram.pedantic = false;

        String vertSrc = Gdx.files.internal("ShaderList/Rain/Rain.vert").readString();
        String fragSrc = Gdx.files.internal("ShaderList/Rain/Rain.frag").readString();

        String vertSrcSnow = Gdx.files.internal("ShaderList/Snow/Snow.vert").readString();
        String fragSrcSnow = Gdx.files.internal("ShaderList/Snow/Snow.frag").readString();

        String vertSrcRipple = Gdx.files.internal("ShaderList/Ripple/Ripple.vert").readString();
        String fragSrcRipple = Gdx.files.internal("ShaderList/Ripple/Ripple.frag").readString();

        shaderRipple = new ShaderProgram(vertSrcRipple, fragSrcRipple);
        if (!shaderRipple.isCompiled()) {
            throw new GdxRuntimeException("Rain shader compile error: " + shaderRipple.getLog());
        }
        for(int i = 0; i < 100; i++) {
            RainList.add(new Rain());
            SnowList.add(new Snow());
        }
        ShaderProgram.pedantic = false;

        shader = new ShaderProgram(vertSrc, fragSrc);
        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Rain shader compile error: " + shader.getLog());
        }
        shaderSnow = new ShaderProgram(vertSrcSnow, fragSrcSnow);
        if (!shaderSnow.isCompiled()) {
            throw new GdxRuntimeException("Snow shader compile error: " + shaderSnow.getLog());
        }
    }
    public static void  WeatherCycle(){
        WeatherGlobal = rand.rand(2);
    }
    /** What climate the local player is currently standing in - TEMPERATE (and so "rain") if there's no local player or no procedural terrain here. */
    private static com.mygdx.game.block.Block.Climate currentClimate(){
        if (Main.RC == null || Main.RC.MainUnit == null) return com.mygdx.game.block.Block.Climate.TEMPERATE;
        return com.mygdx.game.MapFunction.ProceduralTerrainPainter.climateAt(Main.RC.MainUnit.x, Main.RC.MainUnit.y);
    }
    public static void  RippleIteration(SpriteBatch batch){
        switch (WeatherGlobal){
            case 0:{

            }
            break;
            case 1:{
                // splashes only make sense for actual rain, not snow/desert
                if (currentClimate() == com.mygdx.game.block.Block.Climate.TEMPERATE) {
                    WeatherRipple(batch);
                }
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
                // biome under the local player decides what falls: snow in
                // the cold biome, rain in the temperate one, nothing in the
                // arid one - a desert storm can wait for another day
                switch (currentClimate()){
                    case COLD: WeatherSnow(batch); break;
                    case TEMPERATE: WeatherRain(batch); break;
                    case ARID: default: break;
                }
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
        }
        batch.end();
    }
    public static void WeatherSnow(SpriteBatch batch) {
        batch.begin();
        batch.setShader(shaderSnow);
        shaderSnow.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (int i = 0; i < SnowList.size(); i++) {
            SnowList.get(i).SnowIteration();
        }
        batch.end();
    }
    public static void end(){
        shader.end();
    }



}
