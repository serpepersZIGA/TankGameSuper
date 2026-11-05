package com.mygdx.game.method;

import com.mygdx.game.build.Building;
import com.mygdx.game.unit.Unit;

import java.awt.geom.Area;
import java.util.ArrayList;

import static com.mygdx.game.main.Main.UnitList;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.StrictMath.atan2;
import static java.lang.StrictMath.sqrt;

public class Method {
    public static float PR = (float) (3.141559265/180f),RP = (float) (180f/3.141559265);
    private static float TowerX,TowerY,rotationX,gh,radius;
    public static float tower(float x, float y, float x_2, float y_2, float rotation_tower, float speed_tower) {
        gh = (float) (atan2(y - y_2, x - x_2) *RP);
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
        return (float) (x - (difference * sin(rotation * PR)));
    }
    public static float difference_rotation_cos(float x,float difference,float rotation){
        return (float) (x - (difference * cos(rotation * PR)));
    }
    public static float[]tower_xy(float x,float y,float difference,float rotation){
        return new float[]{difference_rotation_sin(x,difference,rotation),
        difference_rotation_cos(y,difference,rotation)};
    }
    public static float[]tower_xy_2(float x,float y,float difference,float difference_2,float rotation){
        TowerX = difference_rotation_sin(x,difference,rotation);
        TowerY = difference_rotation_cos(y,difference,rotation);
        rotationX = rotation-90;
        return new float[]{difference_rotation_sin(TowerX,difference_2,rotationX),
        difference_rotation_cos(TowerY,difference_2,rotationX)};
    }
    public static Unit detection_near_transport_i(Unit objBot) {
        Unit ind = null;
        radius = 0;
        for (Unit unit : UnitList) {
            if(unit.team !=objBot.team) {
                gh = (int) sqrt(pow2.pow2(objBot.x - unit.x) + pow2.pow2(objBot.y - unit.y));
                if (radius == 0 || radius > gh) {
                    ind = unit;
                    radius = gh;

                }
            }
        }
        return ind;
    }
    public static Object[] DetectionNearTransport(Unit objBot) {
        Unit ind = null;
        radius = 0;
        for (Unit unit : UnitList) {
            if(unit.team !=objBot.team) {
                gh = (float) sqrt(pow2.pow2(objBot.x - unit.x) + pow2.pow2(objBot.y - unit.y));
                if (radius == 0 || radius > gh) {
                    ind = unit;
                    radius = gh;

                }
            }
        }
        return new Object[]{ind,radius};
    }
    public static Object[] detection_near_transport(Unit objBot) {
        Unit ind = null;
        radius = 0;
        for (Unit unit : UnitList) {
            if(unit.team != objBot.team) {
                gh = (float) sqrt(pow2.pow2(objBot.x - unit.x) + pow2.pow2(objBot.y - unit.y));
                if (radius > gh || radius == 0) {
                    ind = unit;
                    radius = (int) gh;

                }
            }
        }
        return new Object[]{ind,radius};
    }
    public static Object[] detectionNearSupportTransport(Unit objBot) {
        Unit ind = null;
        radius = 0;

        for (Unit unit : UnitList) {
            if(unit.team == objBot.team & unit != objBot) {
                gh = (float) sqrt(pow2.pow2(objBot.x - unit.x) + pow2.pow2(objBot.y - unit.y));
                //float rad = (float) sqrt(pow2((x - Target.x)) + pow2(y - Target.y));
                if (radius > gh || radius == 0) {
                    ind = unit;
                    radius = gh;

                }
            }
        }
        return new Object[]{ind,radius};
    }
    public static boolean rect_collision(Area area,
                                  Area area2){
        area = (Area) area.clone();
        area2 = (Area) area2.clone();
        // Вычисление пересечения двух преобразованных прямоугольников
        area.intersect(area2);

        // Проверка наличия пересечения
        //Rectangle intersection = area1.getBounds();
        //System.out.println("Прямоугольники пересекаются. Результат: " + intersection);
        return !area.isEmpty();
    }



}
