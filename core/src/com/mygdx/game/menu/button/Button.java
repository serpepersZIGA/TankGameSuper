package com.mygdx.game.menu.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.Keyboard;

import static com.mygdx.game.main.Main.*;
import static java.lang.StrictMath.*;

public abstract class Button {
    public int x,y,width,height,radius,XTXT,YTXT;
    public static float[] RGBButton1,RGBButton2,RGBButton3,RGBTotal;
    public String txt,path,TankName;
    public static int YList = 0;
    public byte TypeClick;
    public byte ConfigMenu = 0;
    public boolean condition;
    public boolean TypeFont;
    public boolean TipOff;
    public int widthXY,heightXY,radiusX,radiusY;
    public void render(int i){
    }
    protected void ActionButton(){

    }
    protected void DataRect(){
        widthXY = x+width;
        heightXY = y+height;
        XTXT = x+width/3;
        YTXT = (int) (y+height/1.5);
        RGBTotal = RGBButton3;
    }
    protected void DataCircle(){
        radiusX = x+radius/2;
        radiusY = y+radius/2;
        RGBTotal = RGBButton3;
    }
    protected void XYDetectedButtonRect(){
        if(x*ZoomWindowX< Keyboard.MouseX &widthXY*ZoomWindowX>Keyboard.MouseX & y*ZoomWindowY<Keyboard.MouseY
                & heightXY*ZoomWindowY>Keyboard.MouseY){
            TipOff = true;
            RGBTotal = RGBButton2;
        }
        else{
            RGBTotal = RGBButton3;
            TipOff = false;
        }
    }
    protected void XYDetectedButtonCircle(){
        if(sqrt(pow((x-Keyboard.MouseX),2)+pow((y-Keyboard.MouseY),2))<radius){
            TipOff = true;
            RGBTotal = RGBButton2;
        }
        else{
            RGBTotal = RGBButton3;
            TipOff = false;
        }
    }
    protected void ActionButton1(){
        if(TipOff & Keyboard.LeftMouseClick){
            condition = true;
            Keyboard.LeftMouseClick = false;
            RGBTotal = RGBButton1;
        }
    }
    protected void ActionButton2(){
        if(TipOff & Keyboard.LeftMouseClick & !condition){
            condition = true;
            Keyboard.LeftMouseClick = false;
        }
        else if(TipOff & Keyboard.LeftMouseClick & condition){
            condition = false;
            Keyboard.LeftMouseClick = false;
        }
    }
    protected void RenderButtonRect(){
        Render.rect(this.x*ZoomWindowX,this.y*ZoomWindowY,this.width*ZoomWindowX,this.height*ZoomWindowY,
                new Color(RGBTotal[0],RGBTotal[1],RGBTotal[2],0.5f));
    }
    protected void RenderButtonCircle(){
        Render.circle(this.x*ZoomWindowX,this.y*ZoomWindowY,radius,new Color(RGBTotal[0],RGBTotal[1],RGBTotal[2],0.5f));
    }
    public void TXTRender(){
        //font = TXTFont((int) (64*ZoomWindowX),"font/Base/BaseFont4.ttf");
        font.setColor(0.1f,0.9f,0.8f,1f);
        font.draw(Batch,txt,XTXT*ZoomWindowX,YTXT*ZoomWindowY);
    }
    public void TXTRender2(){
        //font = TXTFont((int) (16*ZoomWindowX),"font/Base/BaseFont.ttf");
        font2.setColor(0.1f,0.9f,0.8f,1f);
        font2.draw(Batch,txt,XTXT*ZoomWindowX,YTXT*ZoomWindowY);
    }

}
