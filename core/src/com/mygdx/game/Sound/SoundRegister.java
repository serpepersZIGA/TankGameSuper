package com.mygdx.game.Sound;

import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

import static com.mygdx.game.main.Main.ContentSound;

public class SoundRegister {
    public static ArrayList<SoundPacket>SoundPack = new ArrayList<>();
    public static ArrayList<Object[]>IDSound = new ArrayList<>();
    public static void SoundAdd(){
        IDSound.add(new Object[]{ContentSound.motor,0});
        IDSound.add(new Object[]{ContentSound.motor_back,1});
        IDSound.add(new Object[]{ContentSound.acid_attack,2});
        IDSound.add(new Object[]{ContentSound.break_wooden,3});
        IDSound.add(new Object[]{ContentSound.cannon,4});
        IDSound.add(new Object[]{ContentSound.flame_attack,5});
        IDSound.add(new Object[]{ContentSound.flame_sound,6});
        IDSound.add(new Object[]{ContentSound.hit,7});
        IDSound.add(new Object[]{ContentSound.hit_not_penetration,8});
        IDSound.add(new Object[]{ContentSound.kill,9});
        IDSound.add(new Object[]{ContentSound.machinegun,10});
        IDSound.add(new Object[]{ContentSound.motor_helicopter,11});
    }
}
