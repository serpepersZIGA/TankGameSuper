package com.mygdx.game.unit.moduleUnit;

import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.unit.Unit;

public class Engine extends moduleUnit implements Cloneable{
    public float Acceleration;
    public float MaxSpeed,MinSpeed;
    public FunctionalList functional;
    public float SpeedRotation;
    public float Speed;
    public float slowing = 0.05f;
    public static float speedMinimum = 0.5f;
    public Engine(float MaxSpeed, float MinSpeed, float Acceleration,float SpeedRotation, FunctionalList functional){
        this.MaxSpeed = MaxSpeed;
        this.MinSpeed = MinSpeed;
        this.Acceleration = Acceleration;
        this.SpeedRotation = SpeedRotation;
        this.functional = functional.clone();
    }
    public Engine EngineAdd(){
        try {
            return (Engine) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    public void EngineLoad(Unit unit){
        unit.max_speed = MaxSpeed;
        unit.min_speed = MinSpeed;
        unit.acceleration = Acceleration;
        unit.speed_rotation = SpeedRotation;
        for(int i = 0;i<functional.functional.size();i++){
            unit.functional.Add(functional.functional.get(i));
        }
    }
}
