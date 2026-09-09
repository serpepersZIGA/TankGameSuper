package com.mygdx.game.method;
import static com.mygdx.game.method.Method.PR;
import static java.lang.Math.*;

public class move {
    public static float move_sin(float speed,float rotation){
        return (float) (speed * sin(rotation *PR));}
    public static float move_cos(float speed,float rotation){
        return (float) (speed * cos(rotation *PR));
    }
    public static float move_sin2(float speed,float rotation){
        return (float) (speed * sin(rotation));}
    public static float move_cos2(float speed,float rotation){
        return (float) (speed * cos(rotation));
    }
}
