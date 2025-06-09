package com.mygdx.game.Sound;

import com.badlogic.gdx.audio.Sound;

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
}

