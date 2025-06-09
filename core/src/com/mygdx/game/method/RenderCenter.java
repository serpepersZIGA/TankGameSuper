package com.mygdx.game.method;

import com.mygdx.game.Shader.LightingMainSystem;
import com.mygdx.game.main.Main;

import java.util.ArrayList;

import static com.mygdx.game.main.Main.LightSystem;
import static java.lang.StrictMath.abs;


public class RenderCenter {
    public float x,y,x2,y2;
    public ArrayList<Integer>IndBuilding;
    public int ixCam,iyCam,ixCamBuff,iyCamBuff;
    public float width_2 = Main.screenWidth/2f,height_2 = Main.screenHeight/2f,width_2_zoom = Main.screenWidth/2f
            ,height_2_zoom = Main.screenHeight/2f;
    public float WidthRender = Main.screenWidth, HeightRender = Main.screenHeight;
    public int render_x_max,render_x_min,render_y_max,render_y_min;
    public int block_i_x_max,block_i_y_max,render_x,render_y,cam_x_width,cam_y_height;
    public float WidthRenderZoom,HeightRenderZoom,WidthRenderZoom2,HeightRenderZoom2;
    public RenderCenter(float x, float y){
        this.x = x;
        this.y = y;
        IndBuilding = new ArrayList<>();
    }
    public void const_xy_block(){
        block_i_x_max = Main.BlockList2D.get(0).size()-2;
        block_i_y_max = Main.BlockList2D.size()-2;
    }
    public void method(){
        this.x2 = this.x -this.width_2_zoom;
        this.y2 = this.y -this.height_2_zoom;
        ixCamBuff = (int) (x2/Main.width_block);
        iyCamBuff = (int) (y2/Main.height_block);
    }
    public float[] WindowSynchronization(float x_obj, float y_obj){
        x_obj -= this.x2;
        y_obj -= this.y2;
        return new float[]{x_obj,y_obj};
    }
    public int[] WindowSynchronization(int x_obj, int y_obj){
        x_obj -= this.x2;
        y_obj -= this.y2;
        return new int[]{x_obj,y_obj};
    }
    public int[] render_objZoom(int x_obj,int y_obj){
        x_obj -= this.x2;
        y_obj -= this.y2;
        return new int[]{(int) (x_obj*Main.Zoom), (int) (y_obj*Main.Zoom)};
    }
    public float[] render_objZoom(float x_obj,float y_obj){
        x_obj -= this.x2;
        y_obj -= this.y2;
        return new float[]{x_obj*Main.Zoom,y_obj*Main.Zoom};
    }
    public void render_block(){
        Main.TickBlock +=1;
        if(ixCam!= ixCamBuff||iyCam!= iyCamBuff) {
            CameraMapConf();
            ixCam = ixCamBuff;
            iyCam = iyCamBuff;
        }
        if (Main.TickBlock < Main.TickBlockMax) {
            for (int iy = render_y_min; iy < render_y_max; iy++) {
                for (int ix = render_x_min; ix < render_x_max; ix++) {
                    Main.BlockList2D.get(iy).get(ix).update();
                }
            }
        } else {
            for (int iy = render_y_min; iy < render_y_max; iy++) {
                for (int ix = render_x_min; ix < render_x_max; ix++) {
                    Main.BlockList2D.get(iy).get(ix).updateTick(ix,iy);
//                    if (Main.BlockList2D.get(iy).get(ix).render_block == UpdateRegister.DirtUpdate) {
//                        if (rand.rand(20) == 1) {
//                            Main.BlockList2D.get(iy).get(ix).render_block = UpdateRegister.GrassUpdate;
//                        }
//                    }
                }
            }
            Main.TickBlock = 0;

        }

    }
    public void CameraMapConf(){
        render_x_max = (int)((x2+ WidthRenderZoom)/Main.width_block+1);
        render_x_min = (int)(((x2)/Main.width_block-1));

        render_x = (int)(x2/Main.width_block);
        render_y = (int)(y2/Main.height_block);
        if(render_x_min <0){render_x_min =0;}
        else if(render_x_max >block_i_x_max){render_x_max = block_i_x_max;}
        render_y_max = (int)((y2+ HeightRenderZoom)/Main.height_block+1);
        render_y_min = (int)((y2)/Main.height_block-1);
        if(render_y_min <0){render_y_min = 0;}
        else if(render_y_max >block_i_y_max){render_y_max = block_i_y_max;}
        BuildingConst();
        LightSystem.lightsRender.clear();
        for(LightingMainSystem.Light light : LightSystem.lights){
            if(light.XRender+LightSystem.limitLightingRender >0 &
                    light.YRender+LightSystem.limitLightingRender >0&
                    light.XRender-LightSystem.limitLightingRender < Main.screenWidth &
                    light.YRender-LightSystem.limitLightingRender <Main.screenHeight
            ){
                LightSystem.lightsRender.add(light);

            }
        }

    }
    public void BuildingConst(){
        IndBuilding.clear();
        for (int i = 0; i< Main.BuildingList.size(); i++){
            if((((render_x_max>Main.BuildingList.get(i).RightTopPointX &render_x_min<Main.BuildingList.get(i).RightTopPointX)||
            (render_x_max>Main.BuildingList.get(i).xMatrix &render_x_min<Main.BuildingList.get(i).xMatrix))&
            ((render_y_max>Main.BuildingList.get(i).RightTopPointY &render_y_min<Main.BuildingList.get(i).RightTopPointY)||
            (render_y_max>Main.BuildingList.get(i).yMatrix &render_y_min<Main.BuildingList.get(i).yMatrix)))&
            (render_x_min<Main.BuildingList.get(i).RightTopPointX &render_y_min<Main.BuildingList.get(i).RightTopPointY)&
            (render_x_max>Main.BuildingList.get(i).xMatrix &render_y_max>Main.BuildingList.get(i).yMatrix)
            ) {
                IndBuilding.add(i);
            }

        }
    }
    public void BuildingIteration(){
        for (Integer integer : IndBuilding) {
            Main.BuildingList.get(integer).all_action(integer);
            Main.BuildingList.get(integer).xy_light_render.clear();
        }

    }
}


