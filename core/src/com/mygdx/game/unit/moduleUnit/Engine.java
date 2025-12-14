package com.mygdx.game.unit.moduleUnit;

import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.unit.Unit;

import java.util.Objects;

import static com.mygdx.game.main.Main.RegisterFunctionalComponent;
import static com.mygdx.game.unit.moduleUnit.RegisterModuleEngine.EngineListID;

public class Engine extends moduleUnit implements Cloneable{
    public float Acceleration;
    public float SpeedUp, SpeedDown;
    public FunctionalList functional;
    public float SpeedRotation;
    public float Speed;
    public float slowing = 0.05f;
    public static float speedMinimum = 0.5f;
    public Engine(String ID, float SpeedUp, float SpeedDown, float Acceleration, float SpeedRotation, boolean ConfControl){
        EngineListID.add(new Object[]{this,ID});
        this.functional = new FunctionalList();
        if(ConfControl){
            this.functional.Add(RegisterFunctionalComponent.MotorControl);
        }
        this.SpeedUp = SpeedUp;
        this.SpeedDown = SpeedDown;
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
    public static Engine EngineAdd(String string){
        try {
            for(Object[] engine : EngineListID) {
                if(Objects.equals(engine[1], string)) {
                    Engine engine1 = (Engine)engine[0];
                    return (Engine) engine1.clone();
                }
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException();
    }
    public void EngineLoad(Unit unit){
        unit.SpeedUp = SpeedUp;
        unit.SpeedDown = SpeedDown;
        unit.Acceleration = Acceleration;
        unit.SpeedUpBase = SpeedUp;
        unit.SpeedDownBase = SpeedDown;
        unit.AccelerationBase = Acceleration;

        unit.speed_rotation = SpeedRotation;
        for(int i = 0;i<functional.functional.size();i++){
            unit.functional.Add(functional.functional.get(i));
        }
    }
}
