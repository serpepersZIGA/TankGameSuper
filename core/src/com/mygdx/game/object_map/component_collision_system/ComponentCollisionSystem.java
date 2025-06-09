package com.mygdx.game.object_map.component_collision_system;

import com.mygdx.game.unit.Unit;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public abstract class ComponentCollisionSystem {
    public ComponentCollisionSystem(){

    }
    public ComponentCollisionSystem(int x,int y,int width,int height){

    }
    public void collision(Unit tr, int ix, int iy){
    }
    public boolean rect_collision(int x1,int y1,int width,int height,float rotation,
                                   int x2,int y2,int width2,int height2,float rotation_2){

        Rectangle rect1 = new Rectangle(x1,y1,width,height); // Прямоугольник 1
        Rectangle rect2 = new Rectangle(x2,y2,width2,height2); // Прямоугольник 2

        // Создаем аффинное преобразование для поворота
        AffineTransform transform1 = new AffineTransform();
        transform1.rotate(Math.toRadians(rotation), rect1.getCenterX(), rect1.getCenterY());
        AffineTransform transform2 = new AffineTransform();
        transform2.rotate(Math.toRadians(rotation_2), rect2.getCenterX(), rect2.getCenterY());

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
}
