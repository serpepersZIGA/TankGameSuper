package com.mygdx.game.Parsing;

import com.mygdx.game.Inventory.TegItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class buffGun {
    public int Damage, Penetration, DamageFragment, PenetrationFragment, TemperatureDamage,
            ReloadMax, TimeBullet, AmountFragment, SpeedBullet,QuantityShots,ReloadBetweenShots,ReloadShots;
    public String Image,ID,Fire,TypeShot;
    public int Sound,Price;
    public float ReloadUP,OneShot;
    public ArrayList<String>TegList;

}


