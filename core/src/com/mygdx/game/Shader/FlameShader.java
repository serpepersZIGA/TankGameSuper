package com.mygdx.game.Shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.Weather.Rain;
import com.mygdx.game.Weather.WeatherMainSystem;
import com.mygdx.game.main.Main;

import static com.mygdx.game.Weather.Rain.RainList;
import static com.mygdx.game.main.Main.*;

public class FlameShader {
    public static ShaderProgram shader;
    public static float time;
    public static void FlameShaderAdd() {
        String vertSrc = Gdx.files.internal("ShaderList/Fire/Fire.vert").readString();
        String fragSrc = Gdx.files.internal("ShaderList/Fire/Fire.frag").readString();



        shader = new ShaderProgram(vertSrc, fragSrc);
        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Flame shader compile error: " + shader.getLog());
        }
    }
    public static void FlameShaderIteration() {
        Batch.flush();
        Batch.setShader(shader);
        time+=  Gdx.graphics.getDeltaTime();

        shader.setUniformf("u_time", time);
        shader.setUniformf("u_resolution",screenWidth, screenHeight);

    }
}
