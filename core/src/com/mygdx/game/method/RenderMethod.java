package com.mygdx.game.method;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.main.Main;


public class RenderMethod {
    public static void transorm_img(int x, int y, float width, float height, float rotation, Sprite sprite){
        //image = new Texture("badlogic.jpg");
        sprite.setPosition(x,y);
        sprite.setSize( width,height);
        sprite.setRotation( rotation);
        sprite.draw(Main.Batch);
    }
    public static void transorm_img(int x,int y,float width,float height,Sprite sprite){
        sprite.setPosition(x,y);
        sprite.setSize( width,height);
        sprite.draw(Main.Batch);


    }
    public static void transorm_img(int x,int y,float width,float height,float rotation,Sprite sprite,float x_const,float y_const){
        //image = new Texture("badlogic.jpg");
        sprite.setPosition((float)x, (float) y);
        sprite.setOrigin(x_const,y_const);
        sprite.setSize( width,height);
        sprite.setRotation(rotation);
        sprite.draw(Main.Batch);
    }

    //public static void
}
