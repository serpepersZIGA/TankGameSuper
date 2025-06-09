package com.mygdx.game.method;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.main.Main;

import static com.mygdx.game.main.Main.LightSystem;
import static java.lang.StrictMath.abs;

public class CycleTimeDay {
    public static int timeDay,timeNight,totalTime,MaxTime,timeTransitionDay,timeTransitionNight;
    public static float lightDay,lightNight,lightTotal,lightPurpose,lightFlame, lightGlobal
            ,lightRealGlobal,lightColorGlobal;

    public static float lightVariabilityDayNight,lightVariabilityNightDay;
    public static int ConfigTime;

    public CycleTimeDay(int timeDay,int timeNight,int timeTransitionDay,int timeTransitionNight,float lightDay,float lightNight){
        CycleTimeDay.timeDay = timeDay* Main.FPS;
        CycleTimeDay.timeNight = timeNight* Main.FPS;
        CycleTimeDay.timeTransitionDay = timeTransitionDay* Main.FPS;
        CycleTimeDay.timeTransitionNight = timeTransitionNight* Main.FPS;
        CycleTimeDay.lightDay = lightDay;
        CycleTimeDay.lightFlame = CycleTimeDay.lightDay+0.1f;
        CycleTimeDay.lightNight = lightNight;
        CycleTimeDay.MaxTime = CycleTimeDay.timeDay;
        CycleTimeDay.ConfigTime = 0;
        CycleTimeDay.lightTotal = lightDay;
        data();

    }
    private void data(){
        CycleTimeDay.lightVariabilityDayNight = (lightDay-lightNight)/timeTransitionDay;
        CycleTimeDay.lightVariabilityNightDay = (lightNight-lightDay)/timeTransitionNight;
    }
    public void WorkTime() {
        lightGlobal = (lightTotal-lightFlame);
        lightRealGlobal = lightGlobal*1.5f;
        lightColorGlobal = lightGlobal*8f;
        LightSystem.setAmbientColor(new Color(0,0,0,lightTotal));
        //LightSystem.setMinLightness(lightTotal);

        totalTime += 1;
        if (MaxTime < totalTime) {
            ConfigTime += 1;
            if (ConfigTime > 3) {
                ConfigTime = 0;
            }
            switch (ConfigTime) {
                case 0: {
                    MaxTime = timeDay;
                    lightPurpose = lightDay;
                    lightTotal = lightDay;
                    break;
                }
                case 1: {
                    MaxTime = timeTransitionDay;
                    lightPurpose = timeNight;
                    break;
                }
                case 2: {
                    MaxTime = timeNight;
                    lightPurpose = lightNight;
                    lightTotal = lightNight;
                    break;
                }
                case 3: {
                    MaxTime = timeTransitionNight;
                    lightPurpose = lightDay;
                    break;
                }
            }
            totalTime = 0;
        }
        switch (ConfigTime) {
            case 1: {
                lightTotal += lightVariabilityNightDay;
                break;
            } case 3: {
                lightTotal += lightVariabilityDayNight;
            }
        }
    }
}
