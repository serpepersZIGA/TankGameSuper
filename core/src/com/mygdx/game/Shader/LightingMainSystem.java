package com.mygdx.game.Shader;//package com.mygdx.game.Shader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.main.Main;

import static com.mygdx.game.main.Main.LightSystem;
import static com.mygdx.game.method.CycleTimeDay.lightTotal;


public class LightingMainSystem implements Disposable {
    public final ShaderProgram shader;
    public final Array<Light> lights;
    public final Array<Light> lightsRender;
    private Color ambientColor;
    private float minLightness;
    public int limitLightingRender = 550;

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
        lightsRender = new Array<>();
        lights = new Array<>();
        ambientColor = new Color(0.1f, 0.1f, 0.15f, 1f);
        minLightness = 0.3f;
    }

    public void begin(SpriteBatch batch) {
        batch.setShader(shader);

        //Render.setTransformMatrix(Batch.getTransformMatrix());

        //Render.setTransformMatrix(shader);
        //Render.setAutoShapeType(true);

        // Устанавливаем общие параметры освещения
        shader.setUniformf("u_ambientColor", ambientColor.r, ambientColor.g, ambientColor.b
                ,ambientColor.a);
        shader.setUniformf("u_minLightness", minLightness);
        shader.setUniformi("u_activeLights", lightsRender.size);
        for(int i = 0; i < lights.size; i++){
            Light light = lights.get(i);
            float[]xy = Main.RC.render_objZoom(light.position.x,light.position.y);
            light.XRender = xy[0];
            light.YRender = xy[1];
        }

        // Устанавливаем параметры каждого источника света
        for (int i = 0; i < lightsRender.size && i < 160; i++) {
            Light light = lightsRender.get(i);
            if(light.work) {
                float[]xy = Main.RC.render_objZoom(light.position.x,light.position.y);
                light.XRender = xy[0];
                light.YRender = xy[1];
                shader.setUniformf("u_lights[" + i + "].position", light.XRender, light.YRender);
                shader.setUniformf("u_lights[" + i + "].color", light.color.r, light.color.g, light.color.b,
                        light.color.a);
                shader.setUniformf("u_lights[" + i + "].intensity", light.intensity);
                shader.setUniformf("u_lights[" + i + "].radius", light.radiusZoom + light.radiusZoom / 2 * lightTotal);
                shader.setUniformf("u_lights[" + i + "].transparency", light.transparency);
            }
            else{
                shader.setUniformf("u_lights[" + i + "].position", light.XRender, light.YRender);
                shader.setUniformf("u_lights[" + i + "].color", light.color.r, light.color.g, light.color.b,
                        light.color.a);
                shader.setUniformf("u_lights[" + i + "].intensity", 0);
                shader.setUniformf("u_lights[" + i + "].radius", 0);
                shader.setUniformf("u_lights[" + i + "].transparency", 0);
            }
        }
    }
    public void end(SpriteBatch batch) {
        batch.setShader(null);
    }

    public Light addLight() {
        Light light = new Light();
        lights.add(light);
        return light;
    }

    public void removeLight(Light light) {
        lights.removeValue(light, true);
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