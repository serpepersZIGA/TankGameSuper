package com.mygdx.game.menu.slider;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.method.Keyboard;

import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.main.Main.ZoomWindowX;
import static com.mygdx.game.main.Main.ZoomWindowY;
import static java.lang.StrictMath.abs;

public class Slider {
    public float Percentage;
    public int X,Y,WidthSlider = 40,HeightSlider =40,Width=300,Height=35,WidthSlider2 = 20,HeightSlider2 = 20
    , CenterX,CenterY,XUP,XDown,XSlider,YSlider,XTxT,YTxT,XPercentage,YPercentage;
    public boolean ConfClickSlider;
    public byte ConfigMenu;
    public String TXT;

    public Slider(int x,int y,byte ConfigMenu,String TxT){
        X = x;
        Y = y;
        XUP = Width+X;
        XSlider = x;
        YSlider = y;
        this.ConfigMenu = ConfigMenu;
        CenterX = X+WidthSlider2;
        TXT = TxT;
        CenterY = Y+HeightSlider2;
        XTxT = (int) (X+Width*0.5-4*TXT.length());
        YTxT = (int) (Y+Height*1.5);
        XPercentage = (int) (X+Width*0.5);
        YPercentage = Y;

    }
    public void SliderDown(){
        if(!Keyboard.LeftMouse & ConfClickSlider){
            ConfClickSlider =false;

        }
    }
    public void SliderFollow(){
        if(Keyboard.LeftMouse & ConfClickSlider){
            float n = Keyboard.MouseX-X;
            if(Width<n){
                Percentage = 1f;
                CenterX = XUP;
                XSlider = CenterX-WidthSlider2;
            } else if (0 > n) {
                Percentage = 0f;
                CenterX = X;
                XSlider = CenterX-WidthSlider2;
            }
            else {
                CenterX = Keyboard.MouseX;
                XSlider = CenterX-WidthSlider2;
                Percentage = n/Width;
            }

        }
    }
    public void SliderClick(){
        if(!ConfClickSlider) {
            if (Keyboard.LeftMouse & abs(Keyboard.MouseX - CenterX) < WidthSlider2 & abs(Keyboard.MouseY - (CenterY-HeightSlider)) < HeightSlider2) {
                ConfClickSlider = true;
            }
        }
    }
    public float PercentageGet(){
        //System.out.println(Percentage);
        return Percentage;
    }
    public void SizeSet(int width,int height){
        Width = width;
        Height = height;
    }
    public void RenderSlider(){
        Render.rect(this.X*ZoomWindowX,this.Y*ZoomWindowY,this.Width*ZoomWindowX,this.Height*ZoomWindowY,
                new Color(0.5f,0.3f,0.1f,0.5f));
        Render.rect(this.XSlider*ZoomWindowX,this.YSlider*ZoomWindowY,this.WidthSlider*ZoomWindowX,this.HeightSlider*ZoomWindowY,
                new Color(0.5f,0.3f,0.1f,0.5f));
        font2.setColor(0.1f,0.9f,0.8f,1f);
        font2.draw(Batch,""+(int)(Percentage*100),XPercentage*ZoomWindowX,YPercentage*ZoomWindowY);

        font2.draw(Batch,TXT,XTxT*ZoomWindowX,YTxT*ZoomWindowY);
    }
    public void AllAction(){
        RenderSlider();
        SliderClick();
        SliderFollow();
        SliderDown();
    }
}
