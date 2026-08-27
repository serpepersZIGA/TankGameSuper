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
    // {coldFactor, aridFactor} at the local player's current position, both 0
    // (plain temperate) if there's no local player or no procedural terrain
    // here - continuous, not a discrete pick, so crossing a biome boundary
    // cross-fades what's falling instead of snapping between rain and snow
    private static float[] currentClimate(){
        if (Main.RC == null || Main.RC.MainUnit == null) return new float[]{0f, 0f};
        return com.mygdx.game.MapFunction.ProceduralTerrainPainter.climateAt(Main.RC.MainUnit.x, Main.RC.MainUnit.y);
    }
    private static float rainAlpha(float[] climate){
        return clamp01(1f-climate[0]-climate[1]);
    }
    private static float snowAlpha(float[] climate){
        return clamp01(climate[0]);
    }
    private static float clamp01(float v){
        return Math.max(0f, Math.min(1f, v));
    }
    public static void  RippleIteration(SpriteBatch batch){
        switch (WeatherGlobal){
            case 0:{

            }
            break;
            case 1:{
                // splashes only make sense once it's raining meaningfully,
                // not during the cross-fade sliver near a cold/arid boundary
                if (rainAlpha(currentClimate()) > 0.5f) {
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
    // below this, don't even bother running/drawing that particle system -
    // it would be practically invisible anyway
    private static final float MIN_VISIBLE_ALPHA = 0.03f;

    public static void  WeatherIteration(SpriteBatch batch){
        switch (WeatherGlobal){
            case 0:{

            }
            break;
            case 1:{
                // biome under the local player decides the MIX of what
                // falls: snow weight = how cold, rain weight = however much
                // is left once cold+arid are accounted for. Both can be
                // partially active at once near a boundary, so crossing one
                // fades between them instead of snapping.
                float[] climate = currentClimate();
                float rain = rainAlpha(climate);
                float snow = snowAlpha(climate);
                if (snow > MIN_VISIBLE_ALPHA) WeatherSnow(batch, snow);
                if (rain > MIN_VISIBLE_ALPHA) WeatherRain(batch, rain);
            }
            break;
        }
    }
    public static void WeatherRain(SpriteBatch batch, float alpha) {
        batch.begin();
        batch.setShader(shader);
        shader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shader.setUniformf("u_alphaScale", alpha);
        for (int i = 0; i < RainList.size(); i++) {
            Rain rain = RainList.get(i);
            rain.RainIteration();
        }
        batch.end();
    }
    public static void WeatherSnow(SpriteBatch batch, float alpha) {
        batch.begin();
        batch.setShader(shaderSnow);
        shaderSnow.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shaderSnow.setUniformf("u_alphaScale", alpha);
        for (int i = 0; i < SnowList.size(); i++) {
            SnowList.get(i).SnowIteration();
        }
        batch.end();
    }
    public static void end(){
        shader.end();
    }



}
