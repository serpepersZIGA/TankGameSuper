package com.mygdx.game.method;

import com.mygdx.game.build.Building;
import com.mygdx.game.unit.Unit;

import java.util.ArrayList;

import static com.mygdx.game.main.Main.UnitList;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.StrictMath.atan2;
import static java.lang.StrictMath.sqrt;

public class Method {
    public static float tower(float x, float y, float x_2, float y_2, float rotation_tower, float speed_tower) {
        int gh = (int) (atan2(y - y_2, x - x_2) / 3.1415926535 *180);
        if(gh>50 && rotation_tower<-50){
            gh= -180;
        }
        if(gh<-50 && rotation_tower>50){
            gh= 180;
        }
        if (rotation_tower > 179){rotation_tower = -179;}
        else if (rotation_tower < -179){rotation_tower = 179;}
        if (rotation_tower < gh) {
            rotation_tower += speed_tower;
        } else if (rotation_tower > gh) {
            rotation_tower -= speed_tower;
        }
        return rotation_tower;
    }
    public static float difference_rotation_sin(float x,float difference,float rotation){
        return (float) (x - (difference * sin(rotation * 3.1415926535 / 180.0)));
    }
    public static float difference_rotation_cos(float x,float difference,float rotation){
        return (float) (x - (difference * cos(rotation * 3.1415926535f / 180.0f)));
    }
    public static float[]tower_xy(float x,float y,float fire_x,float fire_y,float difference,float rotation){
        float tower_x = difference_rotation_sin(x+fire_x,difference,rotation);
        float tower_y = difference_rotation_cos(y+fire_y,difference,rotation);
        return new float[]{tower_x,tower_y};
    }
    public static float[]tower_xy_2(float x,float y,float fire_x,float fire_y,float difference,float difference_2,float rotation){
        float tower_x = difference_rotation_sin(x+fire_x,difference,rotation);
        float tower_y = difference_rotation_cos(y+fire_y,difference,rotation);
        float rotationX = rotation-90;
        tower_x = difference_rotation_sin(tower_x,difference_2,rotationX);
        tower_y = difference_rotation_cos(tower_y,difference_2,rotationX);
        return new float[]{tower_x,tower_y};
    }
    public static Unit detection_near_transport_i(Unit objBot) {
        Unit ind = null;
        int radius = 0;
        for (Unit unit : UnitList) {
            if(unit.team !=objBot.team) {
                int g = (int) sqrt(pow2.pow2(objBot.x - unit.x) + pow2.pow2(objBot.y - unit.y));
                if (radius == 0 || radius > g) {
                    ind = unit;
                    radius = g;

                }
            }
        }
        return ind;
    }
    public static Object[] DetectionNearTransport(Unit objBot) {
        Unit ind = null;
        float g;
        float radius = 0;
        for (Unit unit : UnitList) {
            if(unit.team !=objBot.team) {
                g = (float) sqrt(pow2.pow2(objBot.x - unit.x) + pow2.pow2(objBot.y - unit.y));
                if (radius == 0 || radius > g) {
                    ind = unit;
                    radius = g;

                }
            }
        }
        return new Object[]{ind,radius};
    }
    public static Object[] detection_near_transport(Unit objBot) {
        Unit ind = null;
        int radius = 0;
        float g;
        for (Unit unit : UnitList) {
            if(unit.team != objBot.team) {
                g = (float) sqrt(pow2.pow2(objBot.x - unit.x) + pow2.pow2(objBot.y - unit.y));
                if (radius > g || radius == 0) {
                    ind = unit;
                    radius = (int) g;

                }
            }
        }
        return new Object[]{ind,radius};
    }
    public static Object[] detectionNearSupportTransport(Unit objBot) {
        Unit ind = null;
        float radius = 0;
        float g;
        for (Unit unit : UnitList) {
            if(unit.team == objBot.team & unit != objBot) {
                g = (float) sqrt(pow2.pow2(objBot.x - unit.x) + pow2.pow2(objBot.y - unit.y));
                //float rad = (float) sqrt(pow2((x - Target.x)) + pow2(y - Target.y));
                if (radius > g || radius == 0) {
                    ind = unit;
                    radius = g;

                }
            }
        }
        return new Object[]{ind,radius};
    }



}
