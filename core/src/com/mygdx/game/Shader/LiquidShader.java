package com.mygdx.game.Shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

import static com.mygdx.game.main.Main.*;

public class LiquidShader {
    public static ShaderProgram shaderAcid,shaderBlood;
    public static float time;
    public static void LiquidShaderAdd() {
        String vertSrc = Gdx.files.internal("ShaderList/Liquid/Liquid.vert").readString();
        String fragSrc = Gdx.files.internal("ShaderList/Liquid/Liquid.frag").readString();

        //vertSrc = Gdx.files.internal("ShaderList/Fire/Fire.vert").readString();
        //fragSrc = Gdx.files.internal("ShaderList/Fire/Fire.frag").readString();



        shaderAcid = new ShaderProgram(vertSrc, fragSrc);
        if (!shaderAcid.isCompiled()) {
            throw new GdxRuntimeException("Flame shader compile error: " + shaderAcid.getLog());
        }

        fragSrc = Gdx.files.internal("ShaderList/Liquid/Blood.frag").readString();

        //vertSrc = Gdx.files.internal("ShaderList/Fire/Fire.vert").readString();
        //fragSrc = Gdx.files.internal("ShaderList/Fire/Fire.frag").readString();



        shaderBlood = new ShaderProgram(vertSrc, fragSrc);
        if (!shaderBlood.isCompiled()) {
            throw new GdxRuntimeException("Flame shader compile error: " + shaderBlood.getLog());
        }
    }
    public static void AcidShaderIteration() {
        //Batch.flush();
        Batch.setShader(shaderAcid);
        Batch.enableBlending();
        Batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        time+=  TimeGlobal;

        shaderAcid.setUniformf("u_time", time);
        shaderAcid.setUniformf("u_resolution",screenWidth, screenHeight);
//        for (i= 0; i< Main.LiquidList.size(); i++){
//            Particle particle = Main.LiquidList.get(i);
//            LiquidShader.shader.setUniformf("ColorList["+i+"]",particle.r,particle.g,particle.b);}
        if(time > 1000.0){
            time = 0;
        }
    }
    public static void BloodShaderIteration() {
        //Batch.flush();
        Batch.setShader(shaderBlood);
        Batch.enableBlending();
        Batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        time +=  TimeGlobal;

        shaderBlood.setUniformf("u_time", time);
        shaderBlood.setUniformf("u_resolution",screenWidth, screenHeight);
//        for (i= 0; i< Main.LiquidList.size(); i++){
//            Particle particle = Main.LiquidList.get(i);
//            LiquidShader.shader.setUniformf("ColorList["+i+"]",particle.r,particle.g,particle.b);}
        if(time > 1000.0){
            time = 0;
        }

    }
}
