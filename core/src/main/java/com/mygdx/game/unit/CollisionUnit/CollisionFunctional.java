package com.mygdx.game.unit.CollisionUnit;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.Sound.SoundPlay;
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
    public static float v = 4;
    // one hit sound for the whole scene at a time, no matter how many objects
    // are in contact this frame - without this, driving through a pile of
    // debris (or a multi-block building - see Unit.build_corpus) fires one
    // "hit"/"break" sample per thing touched, all stacked in the same instant
    private static int hitSoundCooldown = 0;
    // a separate, shorter cooldown for bullet impacts specifically - they're
    // a different event stream from physical collisions (a machine gun can
    // land several hits a second under normal fire, that shouldn't fight a
    // tank-vs-wall bump for the same budget) but still needs its own limit,
    // since a burst landing all at once was drowning out everything else
    private static int bulletHitCooldown = 0;
    public static void playCollisionSound(Unit source, int worldX, int worldY, int soundId, Sound sound){
        if (hitSoundCooldown > 0) return;
        SoundPlay.soundPlay(source.x_rend, source.y_rend, worldX, worldY, soundId, sound);
        hitSoundCooldown = 10;
    }
    // same cooldown as playCollisionSound, for callers (Unit.playImpact) that
    // play a procedural voice directly instead of going through soundPlay
    public static boolean canPlayCollisionSound(){
        if (hitSoundCooldown > 0) return false;
        hitSoundCooldown = 10;
        return true;
    }
    public static boolean canPlayBulletHit(){
        if (bulletHitCooldown > 0) return false;
        bulletHitCooldown = 6;
        return true;
    }
    public static void tickHitSoundCooldown(){
        if (hitSoundCooldown > 0) hitSoundCooldown--;
        if (bulletHitCooldown > 0) bulletHitCooldown--;
    }
    // SAT (separating axis theorem) over the two bodies' real rotated
    // rectangles - exact, no blind spots. Returns how far body1 needs to
    // move to no longer overlap body2 along the true minimum separating
    // vector, or null if they don't overlap.
    public static float[] satPush(float x1, float y1, float w1, float h1, float rotation1,
                                   float x2, float y2, float w2, float h2, float rotation2){
        float cx1 = x1+w1/2f, cy1 = y1+h1/2f;
        float cx2 = x2+w2/2f, cy2 = y2+h2/2f;
        float r1 = (float) Math.toRadians(rotation1);
        float r2 = (float) Math.toRadians(rotation2);
        float cos1 = (float) Math.cos(r1), sin1 = (float) Math.sin(r1);
        float cos2 = (float) Math.cos(r2), sin2 = (float) Math.sin(r2);
        float[][] axes = {{cos1,sin1},{-sin1,cos1},{cos2,sin2},{-sin2,cos2}};
        float dx = cx2-cx1, dy = cy2-cy1;
        float bestOverlap = Float.MAX_VALUE, bestX = 0, bestY = 0;
        for (float[] axis : axes){
            float ax = axis[0], ay = axis[1];
            float extent1 = abs(w1/2f*cos1*ax+w1/2f*sin1*ay) + abs(-h1/2f*sin1*ax+h1/2f*cos1*ay);
            float extent2 = abs(w2/2f*cos2*ax+w2/2f*sin2*ay) + abs(-h2/2f*sin2*ax+h2/2f*cos2*ay);
            float centerDist = dx*ax+dy*ay;
            float overlap = extent1+extent2-abs(centerDist);
            if (overlap <= 0f) return null;
            if (overlap < bestOverlap){
                bestOverlap = overlap;
                float sign = centerDist>0 ? -1f : 1f;
                bestX = ax*sign;
                bestY = ay*sign;
            }
        }
        return new float[]{bestX*bestOverlap, bestY*bestOverlap};
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
    // Ram physics, kept deliberately simple after an earlier attempt at a
    // fuller impulse+spin model didn't land after several tuning passes: SAT
    // overlap (exact, no blind spots - see satPush) resolved by closing only
    // a small slice of whatever overlap currently exists each frame, split
    // between the two bodies by mass, never a full teleport. Only the
    // velocity component actually driving each body into the other gets
    // cancelled - not reversed/reflected - which is exactly the same
    // treatment build_corpus gives a wall. No spin, no extra momentum
    // hand-off: this is just "stop jittering when two tanks touch," not an
    // attempt at a realistic ram feel.
    private static final float RAM_POSITION_CORRECTION = 0.2f;
    public static void MethodCollisionTransport(Unit unit, Unit unit2){
        float[] push = satPush(unit.x, unit.y, unit.corpus_width, unit.corpus_height, unit.rotation_corpus,
                unit2.x, unit2.y, unit2.corpus_width, unit2.corpus_height, unit2.rotation_corpus);
        if (push == null) return;

        float massA = unit.corpus_width*unit.corpus_height;
        float massB = unit2.corpus_width*unit2.corpus_height;
        float total = massA+massB;
        float shareA = massB/total, shareB = massA/total;

        unit.x += push[0]*shareA*RAM_POSITION_CORRECTION;
        unit.y += push[1]*shareA*RAM_POSITION_CORRECTION;
        unit2.x -= push[0]*shareB*RAM_POSITION_CORRECTION;
        unit2.y -= push[1]*shareB*RAM_POSITION_CORRECTION;

        float len = (float) sqrt(pow2(push[0])+pow2(push[1]));
        if (len < 0.0001f) return;
        float nx = push[0]/len, ny = push[1]/len;
        float intoA = unit.SpeedInertionX*nx + unit.SpeedInertionY*ny;
        if (intoA < 0f) {
            unit.SpeedInertionX -= intoA*nx;
            unit.SpeedInertionY -= intoA*ny;
        }
        float intoB = unit2.SpeedInertionX*(-nx) + unit2.SpeedInertionY*(-ny);
        if (intoB < 0f) {
            unit2.SpeedInertionX -= intoB*(-nx);
            unit2.SpeedInertionY -= intoB*(-ny);
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
