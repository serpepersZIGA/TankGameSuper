package com.mygdx.game.method;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.mygdx.game.main.Main;

import java.awt.geom.AffineTransform;

import static com.mygdx.game.main.Main.TimeGlobal;
import static java.lang.StrictMath.abs;

public class Animator implements Cloneable{

    private Texture sheet;
    private final Animation<TextureRegion> animation,animationInvert;
    private float stateTime;
    //4,2,0.1f;
    public Animator(String image,String imageInvert,int x,int y){
        animation = create(x,y,0.1f,image);
        animationInvert = create(x,y,0.1f,imageInvert);
        stateTime = 0f;
    }
    private Animation<TextureRegion> create(int frameCols,int frameRows,float frameDuration,String image) {
        FileHandle file = Gdx.files.internal(image);
        if (file.exists()) {
            sheet = new Texture(file); // 4 cols x 2 rows
        } else {
            Gdx.app.error("Animator", "Missing animation sheet '" + image + "', using placeholder instead.");
            sheet = placeholder(frameCols, frameRows);
        }

        TextureRegion[][] tmp = TextureRegion.split(sheet,
                sheet.getWidth() / frameCols,
                sheet.getHeight()/frameRows);

        TextureRegion[] frames = new TextureRegion[frameRows*frameCols];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index]
                        = tmp[i][j];
                index++;
            }
        }

        // seconds per frame
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        return animation;
    }

    private static final int PLACEHOLDER_FRAME_SIZE = 8;

    private static Texture placeholder(int frameCols, int frameRows) {
        Pixmap pixmap = new Pixmap(frameCols * PLACEHOLDER_FRAME_SIZE, frameRows * PLACEHOLDER_FRAME_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.MAGENTA);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void render(int x,int y,int width,int height,int width2,int height2,float rotation,float speed) {
        stateTime += TimeGlobal*abs(speed);
        TextureRegion currentFrame;
        if(speed>0) {
            currentFrame = animation.getKeyFrame(stateTime, true);
        }
        else{
            currentFrame = animationInvert.getKeyFrame(stateTime, true);
        }
        Sprite sprite = new Sprite(currentFrame);
        sprite.setPosition((float)x, (float) y);
        sprite.setOrigin(width2,height2);
        sprite.setRotation(rotation);
        sprite.setSize(width,height);

        sprite.draw(Main.Batch);
    }
    public float render(int x,int y,int width,int height,float rotation,float speed,float StateTime) {
        StateTime += TimeGlobal*abs(speed);
        TextureRegion currentFrame;
        if(speed>0) {
            currentFrame = animation.getKeyFrame(StateTime, true);
        }
        else{
            currentFrame = animationInvert.getKeyFrame(StateTime, true);
        }
        Sprite sprite = new Sprite(currentFrame);
        sprite.setPosition((float)x, (float) y);
        //sprite.setOrigin(width2,height2);
        sprite.setRotation(rotation);
        sprite.setSize(width,height);
        sprite.setColor(1,1,1,0.5f);

        sprite.draw(Main.Batch);
        return StateTime;
    }

    public void dispose() {
        sheet.dispose();
    }

    @Override final
    public Animator clone() {
        try {
            Animator clone = (Animator) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
