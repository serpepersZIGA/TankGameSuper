package com.mygdx.game.block;

import com.mygdx.game.main.Main;
import com.mygdx.game.object_map.MapObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.HashMap;

import static com.mygdx.game.block.UpdateRegister.VoidUpdate;
import static com.mygdx.game.method.pow2.pow2;

public abstract class Block {
    public int x,y;
    public int x_center,y_center;
    public UpdateBlock render_block;
    public MapObject objMap;
    public static HashMap<Integer,UpdateBlock>BlockID = new HashMap<>();
    public boolean passability,AiClose;
    // set by ProceduralTerrainPainter instead of a PNG render_block - one
    // color PER CORNER (sampled from continuous noise at that exact world
    // point) instead of one flat fill, so the quad gets drawn with the GPU's
    // own per-vertex color interpolation across it. A flat single-color
    // quad always shows a hard edge against its neighbor no matter how
    // close the two colors are; since adjacent cells sample the exact same
    // world coordinate at their shared corner, they get the identical color
    // there and the whole grid becomes one continuous gradient with no seam,
    // not an approximation of one.
    // terrainSpeedMultiplier/terrainFrictionMultiplier are read directly by
    // Unit.build_corpus() for the cell under the tank's own center, not
    // through the objMap.Collision mechanism (that's for discrete placed
    // objects, not a value that varies every single cell).
    public boolean hasTerrainPaint;
    public float terrainColorBL, terrainColorTL, terrainColorTR, terrainColorBR;
    public float terrainSpeedMultiplier = 1f;
    public float terrainFrictionMultiplier = 1f;
    // classification only (not used for rendering/physics) - lets weather
    // cross-fade between snow/rain/nothing based on how cold/arid the
    // player's current position is, continuously rather than as a hard
    // switch at some boundary line (see WeatherMainSystem). Both default to
    // 0 (plain temperate) for any map that was never procedurally painted.
    public float coldFactor, aridFactor;
    public int iBuilding;
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
                Main.BlockList2D.get(i).get(i2).render_block = BlockID.get(1);
                Main.BlockList2D.get(i).get(i2).iBuilding = -1;

            }
        }
    }
    protected final void block_xy(){
        this.x_center = (int) (this.x +Main.width_block *0.5);
        this.y_center = (int) (this.y +Main.width_block *0.5);
    }
    @SuppressWarnings("unused")
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
    private static com.badlogic.gdx.graphics.Texture whitePixel;
    private static com.badlogic.gdx.graphics.Texture whitePixel(){
        if (whitePixel == null) {
            com.badlogic.gdx.graphics.Pixmap pm = new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
            pm.setColor(com.badlogic.gdx.graphics.Color.WHITE);
            pm.fill();
            whitePixel = new com.badlogic.gdx.graphics.Texture(pm);
            pm.dispose();
        }
        return whitePixel;
    }
    // a small tileable speckle pattern (short light/dark marks - meant to
    // read as grass blades/twigs/pebbles at a glance, not real art) drawn
    // faintly on top of the smooth gradient so a biome's surface isn't
    // perfectly flat - generated once from a fixed pattern seed, not part
    // of the per-map noise, since this is just texture, not terrain data
    private static com.badlogic.gdx.graphics.Texture speckleTexture;
    private static com.badlogic.gdx.graphics.Texture speckleTexture(){
        if (speckleTexture == null) {
            int size = 64;
            com.badlogic.gdx.graphics.Pixmap pm = new com.badlogic.gdx.graphics.Pixmap(size, size, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
            java.util.Random rnd = new java.util.Random(1337);
            int fleckCount = size*size/10;
            for (int i = 0; i < fleckCount; i++){
                int px = rnd.nextInt(size);
                int py = rnd.nextInt(size);
                boolean light = rnd.nextBoolean();
                float a = 0.10f + rnd.nextFloat()*0.10f;
                pm.setColor(light ? 1f : 0f, light ? 1f : 0f, light ? 1f : 0f, a);
                int len = 1+rnd.nextInt(2);
                if (rnd.nextBoolean()) pm.drawLine(px, py, Math.min(px+len,size-1), py);
                else pm.drawLine(px, py, px, Math.min(py+len,size-1));
            }
            speckleTexture = new com.badlogic.gdx.graphics.Texture(pm);
            speckleTexture.setWrap(com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat, com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat);
            pm.dispose();
        }
        return speckleTexture;
    }
    private static final float[] terrainVertices = new float[20];

    public void update(){
        xy = Main.RC.render_objZoom(this.x,this.y);
        if (hasTerrainPaint) {
            float w = Main.width_block_zoom, h = Main.height_block_zoom;
            // bottom-left, top-left, top-right, bottom-right - each with its
            // own corner color, so the GPU interpolates the fill smoothly
            // across the quad instead of one flat tint
            terrainVertices[0]=xy[0];   terrainVertices[1]=xy[1];   terrainVertices[2]=terrainColorBL; terrainVertices[3]=0f; terrainVertices[4]=0f;
            terrainVertices[5]=xy[0];   terrainVertices[6]=xy[1]+h; terrainVertices[7]=terrainColorTL; terrainVertices[8]=0f; terrainVertices[9]=1f;
            terrainVertices[10]=xy[0]+w;terrainVertices[11]=xy[1]+h;terrainVertices[12]=terrainColorTR;terrainVertices[13]=1f;terrainVertices[14]=1f;
            terrainVertices[15]=xy[0]+w;terrainVertices[16]=xy[1];  terrainVertices[17]=terrainColorBR;terrainVertices[18]=1f;terrainVertices[19]=0f;
            Main.Batch.draw(whitePixel(), terrainVertices, 0, 20);

            // faint tiled speckle on top, offset per-cell so it doesn't read
            // as an obviously repeating grid
            com.badlogic.gdx.graphics.g2d.TextureRegion speckle = new com.badlogic.gdx.graphics.g2d.TextureRegion(speckleTexture());
            float tile = 3f;
            float uOff = (this.x%997)/997f, vOff = (this.y%997)/997f;
            speckle.setU(uOff); speckle.setV(vOff);
            speckle.setU2(uOff+tile); speckle.setV2(vOff+tile);
            Main.Batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
            Main.Batch.draw(speckle, xy[0], xy[1], w, h);
        } else {
            render_block.render(xy[0],xy[1]);
        }
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
