package com.mygdx.game.method;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Weather.WeatherMainSystem;
import com.mygdx.game.main.Main;

import static com.mygdx.game.main.Main.*;

public class RenderPrimitive implements Disposable {
    public final PolygonSpriteBatch polyBatch;
    private final Texture whitePixel,background;
    //private final ShaderProgram shader;

    public RenderPrimitive() {
        background = new Texture(Gdx.files.internal("buffer2.png"));
        whitePixel = createWhitePixel();
        indicesADD();


        // Загрузка шейдеров
//        shader = new ShaderProgram(
//                Gdx.files.internal("ShaderList/Primitive/pdwrimitive.vert"),
//                Gdx.files.internal("ShaderList/Primitive/primitive.frag")
//        );

        //batch = new SpriteBatch();
        polyBatch = new PolygonSpriteBatch(1000, LightSystem.shader);
    }
    private Texture createWhitePixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void begin() {
        polyBatch.begin();
        //polyBatchShader.begin();
    }

    public void end() {
        polyBatch.end();
        //polyBatchShader.end();
    }

    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
        float[] vertices = new float[6];
        // Вершина 1
        vertices[0] = x1; vertices[1] = y1;

        // Вершина 2
        vertices[2] = x2; vertices[3] = y2;


        vertices[4] = x3; vertices[5] = y3;


        PolygonRegion polyReg = new PolygonRegion(
                new TextureRegion(whitePixel),
                vertices,
                new short[] {0, 1, 2}
        );

        new PolygonSprite(polyReg).draw(polyBatch);
    }

    public void rect(float x, float y, float width, float height, Color color) {
        float[] vertices = new float[8];
        short[] indices = {0, 1, 2, 2, 3, 0};
        vertices[0] = x;         vertices[1] = y;
        vertices[2] = x + width; vertices[3] = y;
        vertices[4] = x + width; vertices[5] = y + height;
        vertices[6] = x;         vertices[7] = y + height;

        PolygonRegion polyReg = new PolygonRegion(
                new TextureRegion(whitePixel),
                vertices,
                indices
        );
        PolygonSprite sprite = new PolygonSprite(polyReg);
        sprite.setColor(color);
        sprite.draw(polyBatch);
    }
    public float angleStep = 0.1308958f;
    public int segments = 48;
    private short[] indices;
    private static final float[] vertices = new float[392];

    public void circle(float centerX, float centerY, float radius, Color color) {
        // Создаем массив для вершин круга (сегменты + центр)

        // Создаем массив для вершин (центр + точки окружности)

        // Центральная точка (индекс 0)
        int vertexPos = 0;
        vertices[vertexPos++] = centerX;
        vertices[vertexPos++] = centerY;

        // Угловой шаг


        // Заполняем точки окружности (индексы 1..segments)
        for (int i = 0; i < segments; i++) {
            float angle = i * angleStep;
            float cos = MathUtils.cos(angle);
            float sin = MathUtils.sin(angle);

            vertices[vertexPos++] = centerX + radius * cos; // X
            vertices[vertexPos++] = centerY + radius * sin; // Y

        }


        // Создаем и рисуем полигон
        PolygonRegion polyReg = new PolygonRegion(
                new TextureRegion(whitePixel),
                vertices,
                indices
        );
        PolygonSprite sprite = new PolygonSprite(polyReg);
        sprite.setColor(color);
        sprite.draw(polyBatch);
        //new PolygonSprite(polyReg).draw(polyBatch);
    }
    private void indicesADD(){
        int indexPos = 0;
        // Заполняем индексы (веер треугольников)
        indices = new short[144];
        //indices[indexPos++] = 0;
        for (int i = 0; i < segments; i++) {
            indices[indexPos++] = 0; // Центральная точка
            indices[indexPos++] = (short)(i + 1);
            indices[indexPos++] = (short)((i + 1) % segments + 1);
        }
    }

    @Override final
    public void dispose() {
        polyBatch.dispose();
        whitePixel.dispose();
        //shader.dispose();
    }
}
