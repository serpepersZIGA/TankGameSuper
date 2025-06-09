package com.mygdx.game.FunctionalComponent.FunctionalBullet;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.bull.Bullet;

public class ComponentColorFire extends FunctionalComponent {
    public void FunctionalIterationAnHost(Bullet bullet){
        if(bullet.b> 0){bullet.r-= Bullet.r_wane;}
        if(bullet.g> 0){bullet.g -=Bullet.g_wane;}
        if(bullet.r> 0){bullet.b -= Bullet.b_wane;}
    }
    public void FunctionalIterationAnClient(Bullet bullet){
        if(bullet.b> 0){bullet.r-= Bullet.r_wane;}
        if(bullet.g> 0){bullet.g -=Bullet.g_wane;}
        if(bullet.r> 0){bullet.b -= Bullet.b_wane;}
    }
}
