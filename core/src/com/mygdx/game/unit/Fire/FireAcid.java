package com.mygdx.game.unit.Fire;

import com.mygdx.game.Network.SoundPacket;
import com.mygdx.game.Network.BullPacket;
import com.mygdx.game.bull.Bullet;
import com.mygdx.game.bull.BulletRegister;
import com.mygdx.game.Sound.SoundPlay;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.Method;
import com.mygdx.game.method.rand;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.Sound.SoundRegister.SoundPack;
import static com.mygdx.game.bull.BulletRegister.PacketBull;
import static com.mygdx.game.main.Main.BulletList;
import static com.mygdx.game.method.Option.SoundConst;
import static com.mygdx.game.method.pow2.pow2;
import static java.lang.StrictMath.*;

public class FireAcid extends Fire{
    public void FireIteration(Unit unit){
        rotationTower = -unit.rotation_tower-90;
        SoundPlay.soundPlay(unit.x_rend,unit.y_rend, (int) unit.x, (int) unit.y,2, unit.sound_fire);

        float[] xy = Method.tower_xy_2(unit.tower_x+unit.const_tower_x,
                unit.tower_y+unit.const_tower_y
                ,unit.TowerFireConstY,unit.TowerFireConstX,-unit.rotation_tower);
        BulletRegister.BulletAcid.BulletAdd(xy[0], xy[1],rotationTower+ -10+rand.rand(20),unit.damage,unit.penetration,
                unit.damage_fragment,unit.penetration_fragment,unit.team,unit.height,unit.t_damage,unit.SpeedBullet
                ,unit.AmountFragment,unit.TimeBullet+rand.rand(unit.TimeBulletRand),unit);


        BulletRegister.BulletAcid.BulletAdd(xy[0], xy[1],rotationTower+ -10+rand.rand(20),unit.damage,unit.penetration,
                unit.damage_fragment,unit.penetration_fragment,unit.team,unit.height,unit.t_damage,unit.SpeedBullet
                ,unit.AmountFragment,unit.TimeBullet+rand.rand(unit.TimeBulletRand),unit);


    }
}

