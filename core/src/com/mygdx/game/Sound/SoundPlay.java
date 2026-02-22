package com.mygdx.game.Sound;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.Network.SoundPacket;
import com.mygdx.game.main.Main;

import static com.mygdx.game.Sound.SoundRegister.IDSound;
import static com.mygdx.game.Sound.SoundRegister.SoundPack;
import static com.mygdx.game.main.Main.RC;
import static com.mygdx.game.method.Option.SoundConst;
import static com.mygdx.game.method.Option.SoundProcent;
import static com.mygdx.game.method.pow2.pow2;
import static java.lang.StrictMath.sqrt;

public class SoundPlay {
    public static void sound(Sound audioFile, float volume){
        try {
            if(volume>0) {
                audioFile.play(volume); // Установите желаемый уровень громкости в диапазоне -80.0 (тишина) до 6.0206 (макс.)
            }

        } catch (Exception e) {
            System.out.println("Error playing metod.sound: " + e.getMessage());
        }

    }
    public static void soundPlay(int x,int y,int X,int Y,int ID,Sound sound){
        SoundPlay.sound(sound, (1f-((float) sqrt(pow2(x) + pow2(y)) * SoundConst))*SoundProcent);
        SoundPacket soundPacket = new SoundPacket();
        soundPacket.x = X;
        soundPacket.y = Y;
        soundPacket.ID = ID;
        SoundPack.add(soundPacket);
    }
    public static void soundPlayClient(){
        SoundPacket pack;
        while (!SoundRegister.SoundPack.isEmpty()) {
            pack =  SoundRegister.SoundPack.get(0);
            SoundPlay.sound((Sound) IDSound.get(pack.ID)[0], (float)
                    (1f-sqrt(pow2(RC.x2-pack.x)+pow2(RC.y2-pack.y))* SoundConst)*SoundProcent);
            SoundRegister.SoundPack.remove(pack);
        }

    }
}

