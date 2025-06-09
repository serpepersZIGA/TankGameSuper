package com.mygdx.game.method;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

import static com.mygdx.game.main.Main.LightSystem;

public class RenderPrimitive implements Disposable {
    public final PolygonSpriteBatch polyBatch;
    private final Texture whitePixel;
    //private final ShaderProgram shader;
    private static final float PI2 =  2 * MathUtils.PI;

    public RenderPrimitive() {
        whitePixel = createWhitePixel();


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
    }

    public void end() {
        polyBatch.end();
    }

    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
        float[] vertices = new float[8*3];
        // Вершина 1
        vertices[0] = x1; vertices[1] = y1;
        vertices[2] = color.r; vertices[3] = color.g;
        vertices[4] = color.b; vertices[5] = color.a;
        vertices[6] = 0; vertices[7] = 0;

        // Вершина 2
        vertices[8] = x2; vertices[9] = y2;
        vertices[10] = color.r; vertices[11] = color.g;
        vertices[12] = color.b; vertices[13] = color.a;
        vertices[14] = 1; vertices[15] = 1;

        vertices[16] = x3; vertices[17] = y3;
        vertices[18] = color.r; vertices[19] = color.g;
        vertices[20] = color.b; vertices[21] = color.a;
        vertices[22] = 2; vertices[23] = 2;

        PolygonRegion polyReg = new PolygonRegion(
                new TextureRegion(whitePixel),
                vertices,
                new short[] {0, 1, 2}
        );

        new PolygonSprite(polyReg).draw(polyBatch);
    }

    public void rect(float x, float y, float width, float height, Color color) {
        float[] vertices = new float[8 * 4];
        short[] indices = {0, 1, 2, 2, 3, 0};

        // Левый нижний угол
        vertices[0] = x;         vertices[1] = y;
//        vertices[2] = color.r;   vertices[3] = color.g;
//        vertices[4] = color.b;   vertices[5] = color.a;
//        vertices[6] = 0;         vertices[7] = 0;

        // Правый нижний
        vertices[2] = x + width; vertices[3] = y;
//        vertices[10] = color.r;   vertices[11] = color.g;
//        vertices[12] = color.b;   vertices[13] = color.a;
//        vertices[14] = 0;         vertices[15] = 0;
        // ... остальные параметры аналогично

        // Правый верхний
        vertices[4] = x + width; vertices[5] = y + height;
//        vertices[18] = color.r;   vertices[19] = color.g;
//        vertices[20] = color.b;   vertices[21] = color.a;
//        vertices[22] = 0;         vertices[23] = 0;

        // Левый верхний
        vertices[6] = x;         vertices[7] = y + height;
//        vertices[26] = color.r;   vertices[27] = color.g;
//        vertices[28] = color.b;   vertices[29] = color.a;
//        vertices[30] = 0;         vertices[31] = 0;

        PolygonRegion polyReg = new PolygonRegion(
                new TextureRegion(whitePixel),
                vertices,
                indices
        );
        PolygonSprite sprite = new PolygonSprite(polyReg);
        sprite.setColor(color);
        sprite.draw(polyBatch);
    }

    public void circle(float centerX, float centerY, float radius, int segments, Color color) {
        // Создаем массив для вершин круга (сегменты + центр)
        segments = 48;

        // Создаем массив для вершин (центр + точки окружности)
        float[] vertices = new float[8 * (segments + 1)];
        short[] indices = new short[segments * 3];

        // Центральная точка (индекс 0)
        int vertexPos = 0;
        vertices[vertexPos++] = centerX;
        vertices[vertexPos++] = centerY;

        // Угловой шаг
        float angleStep = PI2 / segments;

        // Заполняем точки окружности (индексы 1..segments)
        for (int i = 0; i < segments; i++) {
            float angle = i * angleStep;
            float cos = MathUtils.cos(angle);
            float sin = MathUtils.sin(angle);

            vertices[vertexPos++] = centerX + radius * cos; // X
            vertices[vertexPos++] = centerY + radius * sin; // Y

        }

        // Заполняем индексы (веер треугольников)
        int indexPos = 0;
        for (int i = 0; i < segments; i++) {
            indices[indexPos++] = 0; // Центральная точка
            indices[indexPos++] = (short)(i + 1);
            indices[indexPos++] = (short)((i + 1) % segments + 1);
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

    @Override
    public void dispose() {
        polyBatch.dispose();
        whitePixel.dispose();
        //shader.dispose();
    }
}
