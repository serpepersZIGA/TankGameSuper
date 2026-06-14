package com.mygdx.game.method;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
    public Animator(String image,String imageInvert){
        animation = create(4,2,0.1f,image);
        animationInvert = create(4,2,0.1f,imageInvert);
        stateTime = 0f;
    }
    private Animation<TextureRegion> create(int frameCols,int frameRows,float frameDuration,String image) {
        sheet = new Texture(Gdx.files.internal(image)); // 4 cols x 2 rows

        TextureRegion[][] tmp = TextureRegion.split(sheet,
                sheet.getWidth() / frameCols,
                sheet.getHeight()/frameRows);

        TextureRegion[] frames = new TextureRegion[frameRows*frameCols];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                System.out.println(index+" "+j+" "+i);
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
