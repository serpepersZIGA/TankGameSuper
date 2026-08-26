package com.mygdx.game.bull.Updater;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.main.Main;

public class Rect extends UpdaterBullet{
    public void Update(Bullet bullet) {
        bullet.center_render();
        Main.Render.rect(bullet.x_rend,bullet.y_rend,(int)(bullet.x2* Main.Zoom),(int)(bullet.y2* Main.Zoom),new Color
                (bullet.r,bullet.g,bullet.b,1));
    }
}
