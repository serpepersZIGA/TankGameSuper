package com.mygdx.game.unit.CollisionUnit;

import com.mygdx.game.Sound.SoundPacket;
import com.mygdx.game.main.Main;
import com.mygdx.game.Sound.SoundPlay;
import com.mygdx.game.unit.Unit;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static com.mygdx.game.Sound.SoundRegister.SoundPack;
import static com.mygdx.game.method.Option.SoundConst;
import static com.mygdx.game.method.pow2.pow2;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

public class CollisionMethodGlobal {
    public static ArrayList<Unit>UnitsBuffer;
    public CollisionMethodGlobal(){
        UnitsBuffer = new ArrayList<>();
    }
    public void CollisionIterationGlobal(){
        for(Unit unit : Main.UnitList){
            for (Unit unit2 : UnitsBuffer){
                CollisionMethod(unit,unit2);
            }
            UnitsBuffer.add(unit);

        }
        UnitsBuffer.clear();

    }
    public void CollisionMethod(Unit unit1,Unit unit2){
        if(unit1.height == unit2.height) {
            switch (unit1.collision) {
                case rect: {
                    switch (unit2.collision) {
                        case rect: {
                            if (CollisionRectRect(unit1, unit2)) {
                                SoundPlay.sound(Main.ContentSound.hit, 1f - ((float) sqrt(pow2(unit1.x_rend) + pow2(unit1.y_rend)) / SoundConst));

                                SoundPacket soundPacket = new SoundPacket();
                                soundPacket.ix = (int) unit1.x;
                                soundPacket.iy = (int) unit1.y;
                                soundPacket.ID = 7;
                                SoundPack.add(soundPacket);

                                CollisionFunctional.physicCollision(unit1, unit2);
                                CollisionFunctional.MethodCollisionTransport(unit1, unit2);
                            }
                        }
                        break;
                        case circle: {
                            if (CollisionRectCircle(unit1, unit2)) {
                                if (unit1.team == unit2.team) {
                                    CollisionFunctional.MethodCollisionTransportSoldatAlly(unit1, unit2);
                                } else {
                                    unit2.hp = -1;
                                    CollisionFunctional.MethodCollisionTransportSoldatAlly(unit1, unit2);
                                }

                            }
                        }
                        break;

                    }
                }
                break;
                case circle: {
                    switch (unit2.collision) {
                        case rect: {
                            if (CollisionRectCircle(unit2, unit1)) {
                                if (unit1.team == unit2.team) {
                                    CollisionFunctional.MethodCollisionTransportSoldatAlly(unit2, unit1);
                                } else {
                                    CollisionFunctional.MethodCollisionTransportSoldatAlly(unit1, unit2);
                                    unit1.hp = -1;
                                }
                            }
                        }
                        break;
                        case circle: {
                            if (CollisionCircleCircle(unit1, unit2)) {
                                CollisionFunctional.MethodCollisionSoldatSoldat(unit1, unit2);
                            }

                        }
                        break;

                    }

                }
                break;
            }
        }
    }
    private boolean CollisionRectCircle(Unit unit1,Unit unit2){
        Rectangle2D rect1 = new Rectangle2D.Double(unit1.x,unit1.y,unit1.corpus_width,unit1.corpus_height);
        AffineTransform transform1 = new AffineTransform();
        transform1.rotate(Math.toRadians(-unit1.rotation_corpus), rect1.getCenterX(), rect1.getCenterY());
        Area area1 = new Area(rect1);
        area1.transform(transform1);

        Ellipse2D circle = new Ellipse2D.Double(unit2.x,unit2.y,unit2.corpus_width,unit2.corpus_width);

        return area1.intersects(circle.getBounds2D());


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
    private boolean CollisionCircleCircle(Unit unit1,Unit unit2){
        return sqrt(pow(unit1.x -unit2.x,2)+pow(unit1.y -unit2.y,2))<unit1.corpus_width+unit2.corpus_width;

    }
}
