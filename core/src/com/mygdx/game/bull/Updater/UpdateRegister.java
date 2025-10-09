package com.mygdx.game.bull.Updater;

public class UpdateRegister {
    public static UpdaterBullet RectBullet, CircleBullet;
    public static void UpdateBulletRegisterCreate(){
        RectBullet = new Rect();
        CircleBullet = new Circle();
    }
}
