package com.mygdx.game.Weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import static com.mygdx.game.main.Main.screenHeight;
import static com.mygdx.game.main.Main.screenWidth;

public class RainRippleShader{
    private ShaderProgram rainShader;
    private float time;
    private float rainIntensity = 0.8f;

    public RainRippleShader() {
        rainShader = createRainShader();
    }

    private ShaderProgram createRainShader() {
        String vertexShader = Gdx.files.internal("ShaderList/RainRipple/Ripple.vert").readString();
        String fragmentShader = Gdx.files.internal("ShaderList/RainRipple/Ripple.frag").readString();

        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) {
            Gdx.app.error("RainShader", shader.getLog());
        }
        return shader;
    }

    public void begin(SpriteBatch batch) {
        time += Gdx.graphics.getDeltaTime();
        batch.begin();
        batch.setShader(rainShader);
        rainShader.bind();
        rainShader.setUniformf("u_time", time);
        rainShader.setUniformf("u_resolution",
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        rainShader.setUniformf("u_rain_intensity", rainIntensity);

        // Передаем позицию мыши (если нужно)
//        rainShader.setUniformf("u_mousePos",
//                Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        batch.draw(new Texture("buffer2.png"),0,0,screenWidth,screenHeight);
        batch.end();

    }

    public void end(SpriteBatch batch) {
        batch.setShader(null);
    }

    public void setRainIntensity(float intensity) {
        this.rainIntensity = Math.max(0, Math.min(1, intensity));
    }

    public void dispose() {
        rainShader.dispose();
    }
}