package com.mygdx.game.Shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.main.Main;
import com.mygdx.game.particle.Particle;

import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.CycleTimeDay.lightGlobal;
import static com.mygdx.game.method.CycleTimeDay.lightTotal;

public class LiquidShader {
    public static ShaderProgram shaderAcid,shaderBlood;
    public static float time;

//    public static FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, screenWidth, screenHeight, false);



    public static void LiquidShaderAdd() {
        String vertSrc = Gdx.files.internal("ShaderList/Liquid/Liquid.vert").readString();
        String fragSrc = Gdx.files.internal("ShaderList/Liquid/Liquid.frag").readString();

        //vertSrc = Gdx.files.internal("ShaderList/Fire/Fire.vert").readString();
        //fragSrc = Gdx.files.internal("ShaderList/Fire/Fire.frag").readString();



        shaderAcid = new ShaderProgram(vertSrc, fragSrc);
        if (!shaderAcid.isCompiled()) {
            throw new GdxRuntimeException("Flame shader compile error: " + shaderAcid.getLog());
        }


        // Инициализация матриц

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

//        frameBuffer.begin();
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        batch.setShader(shader1);
//        batch.begin();
//        batch.draw(texture, 0, 0);
//        batch.end();
//        frameBuffer.end();
//
//// Второй проход
//        batch.setShader(shader2);
//        batch.begin();
//        batch.draw(frameBuffer.getColorBufferTexture(), 0, 0);
//        batch.end();
//        batch.setShader(null);

//        shaderAcid.setUniformf("u_time", time);
//        shaderAcid.setUniformf("u_resolution",screenWidth, screenHeight);
//
//        //for (i= 0; i< LiquidList.size(); i++){
//            //Particle particle = Main.LiquidList.get(i);
//            //shaderAcid.setUniformf("seed",228.0f);
//        //}
//        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Установка шейдера

        // Установка матриц


        // Установка параметров воды
        shaderAcid.setUniformf("u_time", time);
        shaderAcid.setUniformf("u_resolution",screenWidth, screenHeight);
        shaderAcid.setUniformf("lightTotal",lightTotal);


        // Рисование меша

        //Batch.setShader(LightSystem.shader);
        if(time > 1000.0){
            time = 0;
        }
        for (i= 0; i< Main.LiquidList.size(); i++){
            Main.LiquidList.get(i).all_action();}
        Batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
    public static void BloodShaderIteration() {
        //Batch.flush();
        Batch.setShader(shaderBlood);
        Batch.enableBlending();
        Batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        //Batch.enableBlending();
        //Batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        time +=  TimeGlobal;

        shaderBlood.setUniformf("u_time", time);
        shaderBlood.setUniformf("u_resolution",screenWidth, screenHeight);
        shaderBlood.setUniformf("lightTotal",lightTotal);
//        for (i= 0; i< Main.LiquidList.size(); i++){
//            Particle particle = Main.LiquidList.get(i);
//            LiquidShader.shader.setUniformf("ColorList["+i+"]",particle.r,particle.g,particle.b);}
        if(time > 1000.0){
            time = 0;
        }
        for (i= 0; i< BloodList.size(); i++){
            Main.BloodList.get(i).all_action();}
        Batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    }
}
