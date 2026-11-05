package com.mygdx.game.Sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.io.File;

public class DataSound {
    public Sound acid_attack,break_wooden,cannon,flame_attack,flame_sound,hit,hit_not_penetration,kill,machinegun,motor,
            motor_helicopter,motor_back;
    public DataSound(){
        this.acid_attack = Gdx.audio.newSound(Gdx.files.internal("sound/acid_attack.wav"));
        this.break_wooden = Gdx.audio.newSound(Gdx.files.internal("sound/break_wooden.wav"));
        this.cannon = Gdx.audio.newSound(Gdx.files.internal("sound/cannon.wav"));
        this.flame_attack = Gdx.audio.newSound(Gdx.files.internal("sound/flame_attack.wav"));
        this.flame_sound = Gdx.audio.newSound(Gdx.files.internal("sound/flame_sound.wav"));
        this.hit = Gdx.audio.newSound(Gdx.files.internal("sound/hit.wav"));
        this.hit_not_penetration = Gdx.audio.newSound(Gdx.files.internal("sound/hit_not_penetration.wav"));
        this.kill = Gdx.audio.newSound(Gdx.files.internal("sound/kill.wav"));
        this.machinegun = Gdx.audio.newSound(Gdx.files.internal("sound/machinegun.wav"));
        this.motor = Gdx.audio.newSound(Gdx.files.internal("sound/motor.wav"));
        this.motor_helicopter = Gdx.audio.newSound(Gdx.files.internal("sound/motor_helicopter.wav"));
        this.motor_back = Gdx.audio.newSound(Gdx.files.internal("sound/motor_back.wav"));
    }
    public void dispose(){
        acid_attack.dispose();
        break_wooden.dispose();
        cannon.dispose();
        flame_attack.dispose();
        flame_sound.dispose();
        hit.dispose();
        hit_not_penetration.dispose();
        kill.dispose();
        machinegun.dispose();
        motor.dispose();
        motor_helicopter.dispose();
        motor_back.dispose();
    }

}
