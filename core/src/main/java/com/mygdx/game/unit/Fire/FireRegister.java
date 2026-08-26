package com.mygdx.game.unit.Fire;

public class FireRegister {
    public static Fire FireMortar,FireFlame, FireBullet,FireAcid,FireVoid;
    public static void Create(){
        FireVoid = new FireVoid();
        FireMortar = new FireMortar();
        FireFlame = new FireFlame();
        FireBullet = new FireBull();
        FireAcid = new FireAcid();
    }
}
