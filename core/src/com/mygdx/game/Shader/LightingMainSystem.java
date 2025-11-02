package com.mygdx.game.Shader;//package com.mygdx.game.Shader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.main.Main;

import java.util.ArrayList;

import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.main.Main.screenHeight;
import static com.mygdx.game.main.Main.screenWidth;
import static com.mygdx.game.method.CycleTimeDay.lightTotal;


public class LightingMainSystem implements Disposable {
    public final ShaderProgram shader;
    public final ArrayList<Light> lights;
    public final ArrayList<Light> lightsRender;
    private Color ambientColor;
    private float minLightness;
    public int limitLightingRender = 650;
    public static FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth()
            , Gdx.graphics.getHeight(), false);
    public static TextureRegion fboLTexture = new TextureRegion(fbo.getColorBufferTexture());

    public static Mesh mesh;
    public static Texture texture;
    public static OrthographicCamera camera;

    public static class Light {
        public final Vector2 position = new Vector2();
        public final Color color = new Color();
        public float intensity = 1f;
        public float radius = 100f,radiusZoom;
        public boolean work = true;
        public float XRender,YRender;
        public float transparency;

        //public String vertexShader,fragmentShader;

        public Light set(float x, float y, Color color, float intensity, float radius,float transparency) {
            position.set(x, y);
            this.color.set(color);
            this.intensity = intensity;
            this.radius = radius;
            this.radiusZoom = radius*Main.Zoom;
            this.transparency = transparency;

            float[]xy = Main.RC.render_objZoom(this.position.x,this.position.y);
            this.XRender = (int)xy[0];
            this.YRender = (int)xy[1];

            if(XRender+LightSystem.limitLightingRender >0 &
                    YRender+LightSystem.limitLightingRender >0&
                    XRender-LightSystem.limitLightingRender < Main.screenWidth &
                    YRender-LightSystem.limitLightingRender <Main.screenHeight
            ){
                LightSystem.lightsRender.add(this);

            }

            return this;
        }
//        protected void center_render(){
//            float[]xy = Main.RC.render_objZoom(this.position.x,this.position.y);
//            this.XRender = (int)xy[0];
//            this.YRender = (int)xy[1];
//
//        }
    }

    public LightingMainSystem() {
        // Загрузка шейдеров
        String vertexShader = Gdx.files.internal("ShaderList/Lighting/Lighting.vert").readString();
        String fragmentShader = Gdx.files.internal("ShaderList/Lighting/Lighting.frag").readString();

        shader = new ShaderProgram(vertexShader, fragmentShader);

        if (!shader.isCompiled()) {
            throw new RuntimeException("Shader compilation error: " + shader.getLog());
        }
        lightsRender = new ArrayList<>();
        lights = new ArrayList<>();
        ambientColor = new Color(0.1f, 0.1f, 0.15f, 1f);
        minLightness = 0.3f;

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

    public void begin(SpriteBatch batch) {
        batch.begin();
        //batch.flush();
        batch.setShader(shader);

        // Устанавливаем общие параметры освещения
        LightSystem.lightsRender.clear();
        for(LightingMainSystem.Light light : LightSystem.lights){
            if(light.XRender+LightSystem.limitLightingRender >0 &
                    light.YRender+LightSystem.limitLightingRender >0&
                    light.XRender-LightSystem.limitLightingRender < Main.screenWidth &
                    light.YRender-LightSystem.limitLightingRender <Main.screenHeight &
                    light.work
            ){
                LightSystem.lightsRender.add(light);

            }
        }
        shader.setUniformf("u_ambientColor", ambientColor.r, ambientColor.g, ambientColor.b
                ,ambientColor.a);
        shader.setUniformf("u_minLightness", minLightness);
        shader.setUniformf("u_resolution", screenWidth, screenHeight);
        shader.setUniformi("u_activeLights", lightsRender.size());
        for (Light light : lights) {
            float[] xy = Main.RC.render_objZoom(light.position.x, light.position.y);
            light.XRender = xy[0];
            light.YRender = xy[1];
        }

        // Устанавливаем параметры каждого источника света
        for (int i = 0; i < lightsRender.size() && i < 160; i++) {
            Light light = lightsRender.get(i);
            float[]xy = Main.RC.render_objZoom(light.position.x,light.position.y);
            light.XRender = xy[0];
            light.YRender = xy[1];
            shader.setUniformf("u_lights[" + i + "].position", light.XRender, light.YRender);
            shader.setUniformf("u_lights[" + i + "].color", light.color.r, light.color.g, light.color.b,
                    light.color.a);
            shader.setUniformf("u_lights[" + i + "].intensity", light.intensity);
            shader.setUniformf("u_lights[" + i + "].radius", light.radiusZoom + light.radiusZoom *0.5f * lightTotal);
            shader.setUniformf("u_lights[" + i + "].transparency", light.transparency);

        }
        batch.draw(TextureAtl.createSprite("Buffer"),0,0,screenWidth,screenHeight);
        //batch.flush();
        batch.end();
        //batch.setShader(null);
    }

    public Light addLight() {
        Light light = new Light();
        lights.add(light);
        return light;
    }

    public void removeLight(Light light) {
        lights.remove(light);
    }

    public void setAmbientColor(Color color) {
        ambientColor.set(color);
    }

    public void setMinLightness(float value) {
        minLightness = value;
    }

    public void clearLights() {
        lights.clear();
    }

    @Override
    public void dispose() {
        shader.dispose();
    }
}