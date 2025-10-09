package com.mygdx.game.method;
import static java.lang.Math.*;

public class move {
    public static double move_sin(double speed,double rotation){
        return speed * sin(rotation *3.1415926535 /180);}
    public static double move_cos(double speed,double rotation){
        return speed * cos(rotation *3.1415926535 /180);
    }
    public static double move_sin2(double speed,double rotation){
        return speed * sin(rotation);}
    public static double move_cos2(double speed,double rotation){
        return speed * cos(rotation);
    }


    public static float move_sin(float speed,float rotation){
        return (float) (speed * sin(rotation *3.1415926535 /180));}
    public static float move_cos(float speed,float rotation){
        return (float) (speed * cos(rotation *3.1415926535 /180));
    }
    public static float move_sin2(float speed,float rotation){
        return (float) (speed * sin(rotation));}
    public static float move_cos2(float speed,float rotation){
        return (float) (speed * cos(rotation));
    }
}
