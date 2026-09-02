package com.mygdx.game.unit.CollisionUnit;

import com.mygdx.game.method.Method;
import com.mygdx.game.unit.Unit;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.main.Main.RC;
import static com.mygdx.game.method.Method.tower_xy_2;
import static com.mygdx.game.method.pow2.pow2;
import static java.lang.StrictMath.*;

public abstract class CollisionFunctional{
    public static float v = 0.2f;
    public static void PhysicCollision(Unit unit1,Unit unit2){
        // центр корпуса — вокруг него вращается танк
        float cx = unit1.x + unit1.corpus_width_2;
        float cy = unit1.y + unit1.corpus_height_2;

// полуразмеры квадранта (квадрант = четверть корпуса)
        float qw = unit1.corpus_width_2  * 0.5f;
        float qh = unit1.corpus_height_2 * 0.5f;

// в tower_xy_2: d2 -> локальное "вправо", d -> локальное "вниз"
        float rot = -unit1.rotation_corpus;
        float[]xy1 = Method.tower_xy_2(cx, cy,  qh, -qw, rot);
        float[]xy2 = Method.tower_xy_2(cx, cy,  qh,  qw, rot);
        float[]xy3 = Method.tower_xy_2(cx, cy, -qh, -qw, rot);
        float[]xy4 = Method.tower_xy_2(cx, cy, -qh,  qw, rot);

        float inertion;
        Rectangle rect1 = new Rectangle((int) unit2.x, (int) unit2.y,
                (int) unit2.corpus_width, (int) unit2.corpus_height); // Прямоугольник 1
        AffineTransform transform1 = new AffineTransform();
        transform1.rotate(Math.toRadians(unit2.rotation_corpus),rect1.getCenterX(),rect1.getCenterY());
        Area area1 = new Area(rect1);
        area1.transform(transform1);

        float w = qw * 2, h = qh * 2;
        float r1 = unit1.rotation_corpus;
        int torque = 0;
        boolean hit = false;

        if (unit1.rectCollisionCenter(xy1[0], xy1[1], w, h, r1, area1)) { torque += 1; hit = true; }
        if (unit1.rectCollisionCenter(xy2[0], xy2[1], w, h, r1, area1)) { torque -= 1; hit = true; }
        if (unit1.rectCollisionCenter(xy3[0], xy3[1], w, h, r1, area1)) { torque -= 1; hit = true; }
        if (unit1.rectCollisionCenter(xy4[0], xy4[1], w, h, r1, area1)) { torque += 1; hit = true; }

        if (hit) {
            if (torque != 0) {
                inertion = (abs(unit2.speed) + 0.2f) * v;
                TowerRotate(unit1, Math.signum(torque) * inertion);
            }
               // ровно один раз
        }


        unit1.green_len = ((float) unit1.hp / unit1.max_hp) * Option.size_x_indicator;
        return;


    }
    public static void PhysicCollisionBuild(Unit unit1,Area area1){
        // центр корпуса — вокруг него вращается танк
        float cx = unit1.x + unit1.corpus_width_2;
        float cy = unit1.y + unit1.corpus_height_2;

// полуразмеры квадранта (квадрант = четверть корпуса)
        float qw = unit1.corpus_width_2  * 0.5f;
        float qh = unit1.corpus_height_2 * 0.5f;

// в tower_xy_2: d2 -> локальное "вправо", d -> локальное "вниз"
        float rot = -unit1.rotation_corpus;
        float[]xy1 = Method.tower_xy_2(cx, cy,  qh, -qw, rot);
        float[]xy2 = Method.tower_xy_2(cx, cy,  qh,  qw, rot);
        float[] xy3 = Method.tower_xy_2(cx, cy, -qh, -qw, rot);
        float[] xy4 = Method.tower_xy_2(cx, cy, -qh,  qw, rot);

        float inertion;

        float w = qw * 2, h = qh * 2;
        float r1 = unit1.rotation_corpus;
        int torque = 0;
        boolean hit = false;

        if (unit1.rectCollisionCenter(xy1[0], xy1[1], w, h, r1, area1)) { torque += 1; hit = true; }
        if (unit1.rectCollisionCenter(xy2[0], xy2[1], w, h, r1, area1)) { torque -= 1; hit = true; }
        if (unit1.rectCollisionCenter(xy3[0], xy3[1], w, h, r1, area1)) { torque -= 1; hit = true; }
        if (unit1.rectCollisionCenter(xy4[0], xy4[1], w, h, r1, area1)) { torque += 1; hit = true; }

        if (hit) {
            if (torque != 0) {
                inertion = (abs(unit1.speed) + 0.2f) * v;
                TowerRotate(unit1, Math.signum(torque) * inertion);
            }
            // ровно один раз
        }


        unit1.green_len = ((float) unit1.hp / unit1.max_hp) * Option.size_x_indicator;
        return;


    }
    private boolean CollisionRectRect(Unit unit1,Unit unit2){
        Rectangle rect1 = new Rectangle((int) unit1.x, (int) unit1.y, (int) unit1.corpus_width, (int) unit1.corpus_height); // Прямоугольник 1
        Rectangle rect2 = new Rectangle((int) unit2.x, (int) unit2.y, (int) unit2.corpus_width, (int) unit2.corpus_height); // Прямоугольник 2

        // Создаем аффинное преобразование для поворота
        AffineTransform transform1 = new AffineTransform();
        transform1.rotate(Math.toRadians(unit1.rotation_corpus), rect1.getCenterX(), rect1.getCenterY());
        AffineTransform transform2 = new AffineTransform();
        transform2.rotate(Math.toRadians(unit2.rotation_corpus), rect2.getCenterX(), rect2.getCenterY());

        // Преобразование прямоугольников с учетом поворота
        Area area1 = new Area(rect1);
        area1.transform(transform1);
        Area area2 = new Area(rect2);
        area2.transform(transform2);

        // Вычисление пересечения двух преобразованных прямоугольников
        area1.intersect(area2);

        // Проверка наличия пересечения
        //Rectangle intersection = area1.getBounds();
        //System.out.println("Прямоугольники пересекаются. Результат: " + intersection);
        return !area1.isEmpty();

    }
    public static void physicCollision(Unit unit,Unit unit2){
        float x = unit2.x+unit2.corpus_width_2;
        float y = unit2.y+unit2.corpus_height_2;
        float[]xy;
        float inertion;
        float x_2 = unit.x+ unit.corpus_width_2;
        float y_2 = unit.y+ unit.corpus_height_2;
        xy = Method.tower_xy(x,y,-unit2.corpus_height_2,-unit2.rotation_corpus);
        float x_1_2 = xy[0];
        float y_1_2 = xy[1];
        xy = Method.tower_xy(x_2,y_2,-unit.corpus_height_2,-unit.rotation_corpus);
        float x_2_2 = xy[0];
        float y_2_2 = xy[1];
        if(sqrt(pow2(x_1_2 - x_2_2) + pow2(y_1_2 - y_2_2))<(unit.corpus_width_2+unit2.corpus_width_2)*1.4){
            xy = Method.tower_xy_2(x_2,y_2,-unit.corpus_height_3, unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_2_1 = xy[0];
            float y_2_2_1 = xy[1];
            xy = Method.tower_xy_2(x_2,y_2,-unit.corpus_height_3,-unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_2_2 = xy[0];
            float y_2_2_2 = xy[1];
            xy = Method.tower_xy_2(x,y,-unit2.corpus_height_3,unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_2_1 = xy[0];
            float y_1_2_1 = xy[1];
            xy = Method.tower_xy_2(x,y,-unit2.corpus_height_3,-unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_2_2 = xy[0];
            float y_1_2_2 = xy[1];
            if(sqrt(pow2(x_2_2_1 - x_1_2) + pow2(y_2_2_1 - y_1_2))<(unit.corpus_width_2+unit2.corpus_width_2)/1.5) {
                inertion = (abs(unit2.speed) + 1) * v;
                TowerRotate(unit,inertion);
                //TowerRotate(unit2,-inertion);
            }
            if(sqrt(pow2(x_2_2_2 - x_1_2) + pow2(y_2_2_2 - y_1_2))<(unit.corpus_width_2+unit2.corpus_width_2)/1.5) {
                inertion = (abs(unit2.speed) + 1) * v;
                TowerRotate(unit,-inertion);
                //TowerRotate(unit2,inertion);
            }

            if(sqrt(pow2(x_1_2_1 - x_2_2) + pow2(y_1_2_1 - y_2_2))<(unit.corpus_width_2+unit2.corpus_width_2)/1.5) {
                inertion = (abs(unit.speed) + 1) * v;
                TowerRotate(unit2,inertion);
                //TowerRotate(unit,-inertion);
            }
            if(sqrt(pow2(x_1_2_2 - x_2_2) + pow2(y_1_2_2 - y_2_2))<(unit.corpus_width_2+unit2.corpus_width_2)/1.5) {
                inertion = (abs(unit.speed) + 1) * v;
                TowerRotate(unit2,-inertion);
                //TowerRotate(unit,inertion);
            }
            return;
        }
        xy = Method.tower_xy(x,y,unit2.corpus_height_2,-unit2.rotation_corpus);
        float x_1_1 = xy[0];
        float y_1_1 = xy[1];
        xy = Method.tower_xy(x_2,y_2, unit.corpus_height_2,-unit.rotation_corpus);
        float x_2_1 = xy[0];
        float y_2_1 = xy[1];
        if(sqrt(pow2(x_1_1 - x_2_1) + pow2(y_1_1 - y_2_1))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8){
            xy = Method.tower_xy_2(x_2,y_2, unit.corpus_height_3, unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_1_1 = xy[0];
            float y_2_1_1 = xy[1];
            xy = Method.tower_xy_2(x_2,y_2, unit.corpus_height_3,-unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_1_2 = xy[0];
            float y_2_1_2 = xy[1];
            xy = Method.tower_xy_2(x,y,unit2.corpus_height_3,unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_1_1 = xy[0];
            float y_1_1_1 = xy[1];
            xy = Method.tower_xy_2(x,y,unit2.corpus_height_3,-unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_1_2 = xy[0];
            float y_1_1_2 = xy[1];
            if(sqrt(pow2(x_2_1_1 - x_1_1) + pow2(y_2_1_1 - y_1_1))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8) {
                inertion = (abs(unit2.speed) + 1) * v;
                TowerRotate(unit,-inertion);
                //TowerRotate(unit2,inertion);
            }
            if(sqrt(pow2(x_2_1_2 - x_1_1) + pow2(y_2_1_2 - y_1_1))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8) {
                inertion = (abs(unit2.speed) + 1) * v;
                TowerRotate(unit,inertion);
                //TowerRotate(unit2,-inertion);
            }
            if(sqrt(pow2(x_1_1_1 - x_2_1) + pow2(y_1_1_1 - y_2_1))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8) {
                inertion = (abs(unit.speed) + 1) * v;
                TowerRotate(unit2,-inertion);
                //TowerRotate(unit,inertion);
            }
            if(sqrt(pow2(x_1_1_2 - x_2_1) + pow2(y_1_1_2 - y_2_1))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8) {
                inertion = (abs(unit.speed) + 1) * v;
                TowerRotate(unit2,inertion);
                //TowerRotate(unit,-inertion);
            }
            return;
        }
        if(sqrt(pow2(x_1_1 - x_2_2) + pow2(y_1_1 - y_2_2))<(unit.corpus_width_2+unit2.corpus_width_2)*1.2){
            xy = Method.tower_xy_2(x_2,y_2,-unit.corpus_height_3, unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_2_1 = xy[0];
            float y_2_2_1 = xy[1];
            xy = Method.tower_xy_2(x_2,y_2,-unit.corpus_height_3,-unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_2_2 = xy[0];
            float y_2_2_2 = xy[1];
            xy = Method.tower_xy_2(x,y,unit2.corpus_height_3,unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_1_1 = xy[0];
            float y_1_1_1 = xy[1];
            xy = Method.tower_xy_2(x,y,unit2.corpus_height_3,-unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_1_2 = xy[0];
            float y_1_1_2 = xy[1];
            if(sqrt(pow2(x_2_2_1 - x_1_1) + pow2(y_2_2_1 - y_1_1))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8) {
                inertion = (abs(unit2.speed) + 1) * v;
                TowerRotate(unit,-inertion);
                //TowerRotate(unit2,inertion);
            }
            if(sqrt(pow2(x_2_2_2 - x_1_1) + pow2(y_2_2_2 - y_1_1))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8) {
                inertion = (abs(unit2.speed) + 1) * v;
                TowerRotate(unit,inertion);
                //TowerRotate(unit2,-inertion);
            }
            if(sqrt(pow2(x_1_1_1 - x_2_2) + pow2(y_1_1_1 - y_2_2))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8) {
                inertion = (abs(unit.speed) + 1) * v;
                TowerRotate(unit2,-inertion);
                //TowerRotate(unit,inertion);
            }
            if(sqrt(pow2(x_1_1_2 - x_2_2) + pow2(y_1_1_2 - y_2_2))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8) {
                inertion = (abs(unit.speed) + 1) * v;
                TowerRotate(unit2,inertion);
                //TowerRotate(unit,-inertion);
            }
        }
        if(sqrt(pow2(x_2_1 - x_1_2) + pow2(y_2_1 - y_1_2))<(unit.corpus_width_2+unit2.corpus_width_2)*1.4){
            xy = tower_xy_2(x_2,y_2, unit.corpus_height_3, unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_1_1 = xy[0];
            float y_2_1_1 = xy[1];
            xy = tower_xy_2(x_2,y_2, unit.corpus_height_3,-unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_1_2 = xy[0];
            float y_2_1_2 = xy[1];
            xy = tower_xy_2(x,y,-unit2.corpus_height_3,unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_2_1 = xy[0];
            float y_1_2_1 = xy[1];
            xy = tower_xy_2(x,y,-unit2.corpus_height_3,-unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_2_2 = xy[0];
            float y_1_2_2 = xy[1];
            if(sqrt(pow2(x_2_1_1 - x_1_2) + pow2(y_2_1_1 - y_1_2))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8) {
                inertion = (abs(unit2.speed) + 1) * v;
                TowerRotate(unit,-inertion);
                //TowerRotate(unit2,inertion);
            }
            if(sqrt(pow2(x_2_1_2 - x_1_2) + pow2(y_2_1_2 - y_1_2))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8) {
                inertion = (abs(unit2.speed) + 1) * v;
                TowerRotate(unit,inertion);
                //TowerRotate(unit2,-inertion);
            }
            if(sqrt(pow2(x_1_2_1 - x_2_1) + pow2(y_1_2_1 - y_2_1))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8) {
                inertion = (abs(unit.speed) + 1) * v;
                TowerRotate(unit2,inertion);
                //TowerRotate(unit,-inertion);
            }
            if(sqrt(pow2(x_1_2_2 - x_2_1) + pow2(y_1_2_2 - y_2_1))<(unit.corpus_width_2+unit2.corpus_width_2)*0.8) {
                inertion = (abs(unit.speed) + 1) * v;
                TowerRotate(unit2,-inertion);
                //TowerRotate(unit,inertion);
            }
            return;
        }
    }
    public static void TowerRotate(Unit unit,float inertion){
        unit.RotationInertion += inertion;
    }
    private static void TowerRotate(Unit unit){
        float inertion = 0;
        for(Unit tower: unit.TowerUnitList){
            tower.rotation_tower += inertion;
        }
        unit.rotation_corpus += inertion;
    }
    public static void MethodCollisionTransport(Unit unit, Unit unit2){
        unit2.speed *= 0.8f;
        unit.speed *= 0.8f;
        //float rotation = (float) atan2(unit.tower_x-unit2.tower_x,unit.tower_y-unit2.tower_y);
        float x = (unit.SpeedInertionX - unit2.SpeedInertionX)*0.4f;
        float y = (unit.SpeedInertionY - unit2.SpeedInertionY)*0.4f;
        unit2.SpeedInertionX += x;
        unit2.SpeedInertionY += y;
        unit.SpeedInertionX +=  -x;
        unit.SpeedInertionY +=  -y;

//        unit2.x += (float) (2*sin(rotation));
//        unit2.y += (float) (2*cos(rotation));
//        unit.x +=  (float) (2*sin(rotation));
//        unit.y +=  (float) (2*cos(rotation));


        if(unit2.x< unit.x) {
            unit2.x -= 2;
            unit.x += 2;
        }
        else if(unit2.x> unit.x) {
            unit2.x += 2;
            unit.x -= 2;
        }
        if(unit2.y< unit.y) {
            unit2.y -= 2;
            unit.y += 2;
        }
        else if(unit2.y> unit.y) {
            unit2.y += 2;
            unit.y -= 2;
        }
    }
    public static void MethodCollisionSoldatSoldat(Unit unit, Unit unit2){
        if(unit.x>unit2.x){
            unit2.x -= 2;
            unit.x += 2;
        }
        else if(unit.x<unit2.x){
            unit2.x += 2;
            unit.x -= 2;
        }
        if(unit.y>unit2.y){
            unit2.y -= 2;
            unit.y += 2;
        }
        else if(unit.y<unit2.y){
            unit2.y += 2;
            unit.y -= 2;
        }
    }
    public static void MethodCollisionTransportSoldatAlly(Unit unit, Unit unit2){
        if(unit.x>unit2.x){
            unit2.x -= 2;
        }
        else if(unit.x<unit2.x){
            unit2.x += 2;
        }
        if(unit.y>unit2.y){
            unit2.y -= 2;
        }
        else if(unit.y<unit2.y){
            unit2.y += 2;
        }
    }
}
