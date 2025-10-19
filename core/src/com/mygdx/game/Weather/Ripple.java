package com.mygdx.game.Weather;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.rand;

import java.util.ArrayList;

import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.Keyboard.*;

public class Ripple {
    public static ArrayList<Ripple>RippleList = new ArrayList<>();
    public int x,y;
    public static int width = 70,height = 70;
    public static Texture texture = new Texture("buffer2.png");
    public static int widthRender = 70,heightRender = 70;
    public int timer;
    public Ripple(int x,int y){
        this.x= (int) (x+RC.x2);
        this.y= (int) (y+RC.y2);
        timer = 40;
    }
    public void render(){
        timer-=1;
        int[] xy = RC.render_objZoom(x,y);
        Main.Batch.draw(texture,xy[0],xy[1],widthRender,heightRender);
        if(timer<0){RippleList.remove(this);}
    }
    public static void RippleSpawn(){

        RippleList.add(new Ripple((int) rand.rand(ZoomSpawnRippleWidth), (int) rand.rand(ZoomSpawnRippleHeight)));


    }
}
