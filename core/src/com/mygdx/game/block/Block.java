package com.mygdx.game.block;

import com.mygdx.game.main.Main;
import com.mygdx.game.object_map.MapObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import static com.mygdx.game.block.UpdateRegister.GrassUpdate;
import static com.mygdx.game.block.UpdateRegister.VoidUpdate;
import static com.mygdx.game.method.pow2.pow2;

public abstract class Block {
    public int x,y;
    public int x_rend,y_rend,x_center,y_center;
    public UpdateBlock render_block;
    public MapObject objMap;
    public boolean passability,AiClose;
    public int iBuilding;
    public static final float lighting = 400;
    public static float lighting_zoom = 400,lighting_zoom_2 = 200;
    public static void passability_detected() {
        for (int i = 0; i < Main.BuildingList.size(); i++) {
            for (int j = 0; j < Main.BuildingList.get(i).ConstructBuilding.length; j++) {
                for (int j2 = 0; j2 < Main.BuildingList.get(i).ConstructBuilding[j].length; j2++) {
                    Main.BlockList2D.get(j+Main.BuildingList.get(i).yMatrix).get(j2+Main.BuildingList.get(i).xMatrix).passability =
                            Main.BuildingList.get(i).ConstructBuilding[j][j2];
                    if(Main.BuildingList.get(i).ConstructBuilding[j][j2]) {
                        Main.BlockList2D.get(j+Main.BuildingList.get(i).yMatrix).get(j2+Main.BuildingList.get(i).xMatrix).render_block = VoidUpdate;
                        Main.BlockList2D.get(j+Main.BuildingList.get(i).yMatrix).get(j2+Main.BuildingList.get(i).xMatrix).iBuilding = i;

                    }
                }
            }
        }
    }
    public static void passability_detected2() {
        for (int i = 0; i < Main.BlockList2D.size(); i++) {
            for (int i2 = 0; i2 < Main.BlockList2D.get(i).size(); i2++) {
                Main.BlockList2D.get(i).get(i2).passability = false;
                Main.BlockList2D.get(i).get(i2).render_block = GrassUpdate;
                Main.BlockList2D.get(i).get(i2).iBuilding = -1;

            }
        }
    }
    protected final void block_xy(){
        this.x_center = (int) (this.x +Main.width_block *0.5);
        this.y_center = (int) (this.y +Main.width_block *0.5);
    }
    private boolean rect_collision(int x1,int y1,int width,int height,
                                  int x2,int y2,int width2,int height2,double rotation_2){

        Rectangle rect1 = new Rectangle(x1,y1,width,height); // Прямоугольник 1
        Rectangle rect2 = new Rectangle(x2,y2,width2,height2); // Прямоугольник 2

        // Создаем аффинное преобразование для поворота
        AffineTransform transform2 = new AffineTransform();
        transform2.rotate(Math.toRadians(rotation_2), rect2.getCenterX(), rect2.getCenterY());

        // Преобразование прямоугольников с учетом поворота
        Area area1 = new Area(rect1);
        Area area2 = new Area(rect2);
        area2.transform(transform2);

        // Вычисление пересечения двух преобразованных прямоугольников
        area1.intersect(area2);
        return !area1.isEmpty();
    }
    private static int[]xy;

    public void update(){
        xy = Main.RC.render_objZoom(this.x,this.y);
        render_block.render(xy[0],xy[1]);
        this.objMap.render();
    }
    public void updateTick(int ix,int iy){
        xy = Main.RC.render_objZoom(this.x,this.y);
        render_block.renderTick(xy[0],xy[1],ix,iy);
        this.objMap.render();
    }
    public void render(){
    }
    public void all_action(){

    }




}
