package com.mygdx.game.unit.CollisionUnit;

import com.mygdx.game.method.Method;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.method.pow2.pow2;
import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.sqrt;

public abstract class CollisionFunctional{
    public static void physicCollision(Unit unit,Unit unit2){
        float x = unit2.x+unit2.corpus_width_2;
        float y = unit2.y+unit2.corpus_height_2;
        float[]xy;
        float v = 4;
        float x_2 = unit.x+ unit.corpus_width_2;
        float y_2 = unit.y+ unit.corpus_height_2;
        xy = Method.tower_xy(x,y,0,0,-unit2.corpus_height_2,-unit2.rotation_corpus);
        float x_1_2 = xy[0];
        float y_1_2 = xy[1];
        xy = Method.tower_xy(x_2,y_2,0,0,-unit.corpus_height_2,-unit.rotation_corpus);
        float x_2_2 = xy[0];
        float y_2_2 = xy[1];
        if(sqrt(pow2(x_1_2 - x_2_2) + pow2(y_1_2 - y_2_2))<(unit.corpus_width_2+unit2.corpus_width_2)*1.4){
            xy = Method.tower_xy_2(x_2,y_2,0,0,-unit.corpus_height_3, unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_2_1 = xy[0];
            float y_2_2_1 = xy[1];
            xy = Method.tower_xy_2(x_2,y_2,0,0,-unit.corpus_height_3,-unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_2_2 = xy[0];
            float y_2_2_2 = xy[1];
            xy = Method.tower_xy_2(x,y,0,0,-unit2.corpus_height_3,unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_2_1 = xy[0];
            float y_1_2_1 = xy[1];
            xy = Method.tower_xy_2(x,y,0,0,-unit2.corpus_height_3,-unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_2_2 = xy[0];
            float y_1_2_2 = xy[1];
            if(sqrt(pow2(x_2_2_1 - x_1_2) + pow2(y_2_2_1 - y_1_2))<(unit.corpus_width_2+unit2.corpus_width_2)/1.5) {
                unit.rotation_corpus += (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_2_2_2 - x_1_2) + pow2(y_2_2_2 - y_1_2))<(unit.corpus_width_2+unit2.corpus_width_2)/1.5) {
                unit.rotation_corpus -= (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_1_2_1 - x_2_2) + pow2(y_1_2_1 - y_2_2))<(unit.corpus_width_2+unit2.corpus_width_2)/1.5) {
                unit2.rotation_corpus += (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_1_2_2 - x_2_2) + pow2(y_1_2_2 - y_2_2))<(unit.corpus_width_2+unit2.corpus_width_2)/1.5) {
                unit2.rotation_corpus -= (abs(unit.speed) + 1) * v;
            }
            return;
        }
        xy = Method.tower_xy(x,y,0,0,unit2.corpus_height_2,-unit2.rotation_corpus);
        float x_1_1 = xy[0];
        float y_1_1 = xy[1];
        xy = Method.tower_xy(x_2,y_2,0,0, unit.corpus_height_2,-unit.rotation_corpus);
        float x_2_1 = xy[0];
        float y_2_1 = xy[1];
        if(sqrt(pow2(x_1_1 - x_2_1) + pow2(y_1_1 - y_2_1))<(unit.corpus_width_2+unit2.corpus_width_2)*1.2){
            xy = Method.tower_xy_2(x_2,y_2,0f,0f, unit.corpus_height_3, unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_1_1 = xy[0];
            float y_2_1_1 = xy[1];
            xy = Method.tower_xy_2(x_2,y_2,0,0, unit.corpus_height_3,-unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_1_2 = xy[0];
            float y_2_1_2 = xy[1];
            xy = Method.tower_xy_2(x,y,0,0,unit2.corpus_height_3,unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_1_1 = xy[0];
            float y_1_1_1 = xy[1];
            xy = Method.tower_xy_2(x,y,0,0,unit2.corpus_height_3,-unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_1_2 = xy[0];
            float y_1_1_2 = xy[1];
            if(sqrt(pow2(x_2_1_1 - x_1_1) + pow2(y_2_1_1 - y_1_1))<(unit.corpus_width_2+unit2.corpus_width_2)/1.2) {
                unit.rotation_corpus -= (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_2_1_2 - x_1_1) + pow2(y_2_1_2 - y_1_1))<(unit.corpus_width_2+unit2.corpus_width_2)/1.2) {
                unit.rotation_corpus += (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_1_1_1 - x_2_1) + pow2(y_1_1_1 - y_2_1))<(unit.corpus_width_2+unit2.corpus_width_2)/1.2) {
                unit2.rotation_corpus -= (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_1_1_2 - x_2_1) + pow2(y_1_1_2 - y_2_1))<(unit.corpus_width_2+unit2.corpus_width_2)/1.2) {
                unit2.rotation_corpus += (abs(unit.speed) + 1) * v;
            }
            return;
        }
        if(sqrt(pow2(x_1_1 - x_2_2) + pow2(y_1_1 - y_2_2))<(unit.corpus_width_2+unit2.corpus_width_2)*1.2){
            xy = Method.tower_xy_2(x_2,y_2,0,0,-unit.corpus_height_3, unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_2_1 = xy[0];
            float y_2_2_1 = xy[1];
            xy = Method.tower_xy_2(x_2,y_2,0,0,-unit.corpus_height_3,-unit.corpus_width_3,-unit.rotation_corpus);
            float x_2_2_2 = xy[0];
            float y_2_2_2 = xy[1];
            xy = Method.tower_xy_2(x,y,0,0,unit2.corpus_height_3,unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_1_1 = xy[0];
            float y_1_1_1 = xy[1];
            xy = Method.tower_xy_2(x,y,0,0,unit2.corpus_height_3,-unit2.corpus_width_3,-unit2.rotation_corpus);
            float x_1_1_2 = xy[0];
            float y_1_1_2 = xy[1];
            if(sqrt(pow2(x_2_2_1 - x_1_1) + pow2(y_2_2_1 - y_1_1))<(unit.corpus_width_2+unit2.corpus_width_2)/1.2) {
                unit.rotation_corpus -= (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_2_2_2 - x_1_1) + pow2(y_2_2_2 - y_1_1))<(unit.corpus_width_2+unit2.corpus_width_2)/1.2) {
                unit.rotation_corpus += (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_1_1_1 - x_2_2) + pow2(y_1_1_1 - y_2_2))<(unit.corpus_width_2+unit2.corpus_width_2)/1.2) {
                unit2.rotation_corpus -= (abs(unit.speed) + 1) * v;
            }
            if(sqrt(pow2(x_1_1_2 - x_2_2) + pow2(y_1_1_2 - y_2_2))<(unit.corpus_width_2+unit2.corpus_width_2)/1.2) {
                unit2.rotation_corpus += (abs(unit.speed) + 1) * v;
            }
        }
    }
    public static void MethodCollisionTransport(Unit unit, Unit unit2){
        if(unit2.x< unit.x) {
            unit2.x -= 2;
            unit.x += 2;
            unit2.SpeedInert += unit.speed*0.5;
            unit.SpeedInert += unit2.speed*0.5;
            unit2.speed *= -0.8;
            unit.speed *= -0.8;
            unit2.RotationInert = unit.rotation_corpus;
            unit.RotationInert = unit2.rotation_corpus;
        }
        else if(unit2.x>= unit.x) {
            unit2.x += 2;
            unit.x -= 2;
            unit2.SpeedInert += unit.speed*0.5;
            unit.SpeedInert += unit2.speed*0.5;
            unit2.speed *= -0.5;
            unit.speed *= -0.5;
            unit2.RotationInert = unit.rotation_corpus;
            unit.RotationInert = unit2.rotation_corpus;
        }
        if(unit2.y< unit.y) {
            unit2.y -= 2;
            unit.y += 2;
        }
        else if(unit2.y>= unit.y) {
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
