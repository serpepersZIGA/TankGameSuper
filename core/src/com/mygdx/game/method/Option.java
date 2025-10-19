package com.mygdx.game.method;

import com.mygdx.game.main.Main;
import com.mygdx.game.menu.button.Button;

public class Option {
    public float reload_r_indicator,reload_g_indicator,reload_b_indicator;
    public float reload_2_r_indicator,reload_2_g_indicator,reload_2_b_indicator;
    public float hp_r_indicator,hp_g_indicator,hp_b_indicator;
    public float hp_2_r_indicator,hp_2_g_indicator,hp_2_b_indicator;
    public float hp_crite_r_indicator,hp_crite_g_indicator,hp_crite_b_indicator;
    public float size_x_indicator,size_y_indicator;
    public float size_x_indicator_zoom,size_y_indicator_zoom;
    public float const_hp_x,const_hp_y,const_reload_x,const_reload_y;
    public float const_hp_x_zoom,const_hp_y_zoom,const_reload_x_zoom,const_reload_y_zoom;
    public static float SoundConst;
    public Option(){
        SoundConst = 10000;
        Button.RGBButton1 = new float[]{0f,(float) 1 /255*180,0};
        Button.RGBButton2 = new float[]{(float) 1 /255*180,(float) 1 /255*180,0};
        Button.RGBButton3 = new float[]{(float) 1 /255*180,0,0};
        reload_r_indicator = (float) 1 /255*180;
        reload_g_indicator = (float) 1 /255*180;
        reload_b_indicator = 0;
        reload_2_r_indicator = (float) 1 /255*180;
        reload_2_g_indicator = 0;
        reload_2_b_indicator = 0;
        hp_r_indicator = 0;
        hp_g_indicator = (float) 1 /255*180;
        hp_b_indicator = 0;
        hp_2_r_indicator = (float) 1 /255*180;
        hp_2_g_indicator = 0;
        hp_2_b_indicator = 0;
        hp_crite_r_indicator = (float)1/255*182;
        hp_crite_g_indicator = (float)1/255*114;
        hp_crite_b_indicator = (float)1/255*16;
        size_y_indicator = 8;
        size_y_indicator_zoom = (int) (8*Main.Zoom);
        size_x_indicator = 50;
        size_x_indicator_zoom = (int) (50*Main.Zoom);
        const_hp_x = 20;const_hp_y = 25;
        const_reload_x = 20;const_reload_y = 17;



    }
}
