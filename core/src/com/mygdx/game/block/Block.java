package com.mygdx.game.block;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.main.Main;
import com.mygdx.game.object_map.MapObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import static com.mygdx.game.block.UpdateRegister.VoidUpdate;
import static com.mygdx.game.main.Main.AirList;
import static com.mygdx.game.method.CycleTimeDay.*;
import static com.mygdx.game.method.pow2.pow2;
import static java.lang.StrictMath.sqrt;
import static java.sql.Types.NULL;

public abstract class Block {
    public int x,y;
    public int x_rend,y_rend,x_center,y_center;
    public float radius;

    public UpdateBlock render_block;
    public MapObject objMap;
    public boolean passability,AiClose;
    private float r;
    private float g;
    private float b;
    public float rad;
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
    protected final void block_xy(){
        this.x_center = this.x +Main.width_block/2;
        this.y_center = this.y +Main.height_block/2;
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
    public float lightingReal,lightingColor;
    protected final void UpdateAir(){
        if(radius != 0) {
            rad = radius/lighting_zoom;
            lightingReal = (lightRealGlobal)*rad;
            if(lightTotal<lightingReal){
                lightingReal = lightTotal;
            }
            else if(lightingReal<0.2){
                lightingReal = 0.2F;
            }
            lightingColor = (lightColorGlobal)*rad;
            Main.Render.rect(x, y, Main.width_block_air, Main.height_block_air,
                    new Color(r / lightingColor, g / lightingColor, b / lightingColor, lightingReal));

//            Main.Render.setColor(0, 0, 0, lightingReal);
//            Main.Render.rect(x, y, Main.width_block_air, Main.height_block_air);

            r = 0;g =0;b=0;
        } else{
            Main.Render.rect(x,y,Main.width_block_air,Main.height_block_air,
                    new Color(r / lightingColor, g / lightingColor, b / lightingColor, lightingReal));
        }
    }
    public static void LightingAir(int xZOOM, int yZOOM, float[] RGB){
        int x_min = (int) ((xZOOM-lighting_zoom)/Main.width_block_air);
        int x_max = (int) ((xZOOM+lighting_zoom)/Main.width_block_air);
        int y_min = (int) ((yZOOM-lighting_zoom)/Main.height_block_air);
        int y_max = (int) ((yZOOM+lighting_zoom)/Main.height_block_air);
        if(x_min>Main.xMaxAir){
            return;
        }
        if(x_max<0){
            return;
        }
        if(y_min>Main.yMaxAir){
            return;
        }
        if(y_max<0){
            return;
        }


        if(x_min<0){
            x_min = 0;
        }
        if (x_max>Main.xMaxAir) {
            x_max = Main.xMaxAir;
        }


        if(y_min<0){
            y_min = 0;
        }
        if (y_max>Main.yMaxAir) {
            y_max = Main.yMaxAir;
        }
        for (int i = y_min; i < y_max; i++) {
                for (int i2 = x_min; i2 < x_max; i2++) {
                    int gh = (int) sqrt(pow2(xZOOM - AirList.get(i).get(i2).x) + pow2(yZOOM - AirList.get(i).get(i2).y));
                    if (gh < lighting_zoom) {
                        AirList.get(i).get(i2).r += RGB[0] * (Main.radius_air_max_zoom / gh);
                        AirList.get(i).get(i2).g += RGB[1] * (Main.radius_air_max_zoom / gh);
                        AirList.get(i).get(i2).b += RGB[2] * (Main.radius_air_max_zoom / gh);
                        if (AirList.get(i).get(i2).radius == NULL || AirList.get(i).get(i2).radius > gh) {
                            AirList.get(i).get(i2).radius = gh;

                        }
                    }

                }
            }
    }
    public static void LightingAirObject(int xZOOM, int yZOOM, float[] RGB,float distance){
        int x_min = (int) ((xZOOM-distance)/Main.width_block_air);
        int x_max = (int) ((xZOOM+distance)/Main.width_block_air);
        int y_min = (int) ((yZOOM-distance)/Main.height_block_air);
        int y_max = (int) ((yZOOM+distance)/Main.height_block_air);
        if(x_min>Main.xMaxAir){
            return;
        }
        if(x_max<0){
            return;
        }
        if(y_min>Main.yMaxAir){
            return;
        }
        if(y_max<0){
            return;
        }


        if(x_min<0){
            x_min = 0;
        }
        if (x_max>Main.xMaxAir) {
            x_max = Main.xMaxAir;
        }


        if(y_min<0){
            y_min = 0;
        }
        if (y_max>Main.yMaxAir) {
            y_max = Main.yMaxAir;
        }
        for (int i = y_min; i < y_max; i++) {
            for (int i2 = x_min; i2 < x_max; i2++) {
                int gh = (int) sqrt(pow2(xZOOM - AirList.get(i).get(i2).x) + pow2(yZOOM - AirList.get(i).get(i2).y));
                if (gh < distance) {
                    AirList.get(i).get(i2).r += RGB[0] * ( distance / gh);
                    AirList.get(i).get(i2).g += RGB[1] * ( distance / gh);
                    AirList.get(i).get(i2).b += RGB[2] * ( distance / gh);
                    if (AirList.get(i).get(i2).radius == NULL || AirList.get(i).get(i2).radius > gh) {
                        AirList.get(i).get(i2).radius = gh;

                    }
                }

            }
        }
    }




}
