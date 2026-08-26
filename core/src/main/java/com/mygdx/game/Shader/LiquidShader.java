package com.mygdx.game.Shader;


import com.mygdx.game.main.Main;


import static com.mygdx.game.main.Main.*;


public class LiquidShader {



    public static void LiquidShaderAdd() {
    }
    public static void AcidShaderIteration() {
        for (i= 0; i< Main.LiquidList.size(); i++){
            Main.LiquidList.get(i).all_action();}}
    public static void BloodShaderIteration() {
        for (i= 0; i< BloodList.size(); i++){
            Main.BloodList.get(i).all_action();}

    }
}
