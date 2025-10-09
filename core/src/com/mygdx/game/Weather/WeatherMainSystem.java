package com.mygdx.game.Weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.Shader.LightingMainSystem;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.rand;

import java.util.ArrayList;

import static com.mygdx.game.Weather.Rain.RainList;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.CycleTimeDay.CycleDay;

public class WeatherMainSystem {
    public static ShaderProgram shader;
    public static int WeatherGlobal;
    public static int time;
    public static OrthographicCamera camera;
    public static FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth()
            , Gdx.graphics.getHeight(), false);
    public static TextureRegion fboTexture = new TextureRegion(fbo.getColorBufferTexture());// Have to flip on Y axis
    public static Mesh mesh;
    public static Texture texture;
// Have to flip on Y axis



    public WeatherMainSystem(){
        fboTexture.flip(false, true);

        WeatherGlobal = rand.rand(2);
        ShaderProgram.pedantic = false;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        String vertSrc = Gdx.files.internal("ShaderList/Rain/Rain.vert").readString();
        String fragSrc = Gdx.files.internal("ShaderList/Rain/Rain.frag").readString();
        for(int i = 0; i < 100; i++) {
            RainList.add(new Rain());
        }

        shader = new ShaderProgram(vertSrc, fragSrc);
        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Rain shader compile error: " + shader.getLog());
        }
        mesh = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));
        mesh.setVertices(new float[] {
                -0.5f, -0.5f, 0, 1, 1, 1, 1, 0, 1,
                0.5f, -0.5f, 0, 1, 1, 1, 1, 1, 1,
                0.5f, 0.5f, 0, 1, 1, 1, 1, 1, 0,
                -0.5f, 0.5f, 0, 1, 1, 1, 1, 0, 0
        });
        mesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});
        texture = new Texture(Gdx.files.internal("buffer2.png"));
    }
    public static void  WeatherCycle(){
        WeatherGlobal = rand.rand(2);
    }
    public static void  WeatherIteration(SpriteBatch batch){
        //WeatherGlobal = 0;
        switch (WeatherGlobal){
            case 0:{
            }
            break;
            case 1:{
                // Рендерим эффект дождя
                //WeatherRain(batch);

            }
            break;
        }
    }
    public static void WeatherRain(SpriteBatch batch) {
        batch.begin();
        batch.setShader(shader);
        //batch.begin();
        //shader.bind();
        time += Gdx.graphics.getDeltaTime();
        shader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());;
        for (int i = 0; i < RainList.size(); i++) {
            Rain rain = RainList.get(i);
            rain.RainIteration();
            shader.setUniformMatrix("u_projTrans",camera.combined);
            shader.setUniformf("u_rain["+i+"].position", new Vector2(rain.x,rain.y));
            shader.setUniformf("u_rain["+i+"].width", rain.width);
            shader.setUniformf("u_rain["+i+"].height", rain.height);
        }

        shader.setUniformf("u_time", time);
        shader.setUniformf("u_resolution",
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shader.setUniformf("u_rainIntensity", 20);

        batch.draw(new Texture("buffer2.png"),0,0,screenWidth,screenHeight);
        batch.end();

    }
    public static void end(){
        shader.end();
    }



}
