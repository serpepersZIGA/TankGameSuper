package com.mygdx.game.unit.moduleUnit;

import com.mygdx.game.FunctionalComponent.FunctionalList;
import com.mygdx.game.main.Main;

import static com.mygdx.game.main.Main.RegisterFunctionalComponent;

public class RegisterModuleSoldat {
    public static Soldat Soldat,Veteran,Jaeger;
    public static void Create(){
        FunctionalList functional = new FunctionalList();
        functional.Add(RegisterFunctionalComponent.SoldatCorrect);
        functional.Add(RegisterFunctionalComponent.SoldatControl);
        functional.Add(RegisterFunctionalComponent.BuildCollision);
        functional.Add(RegisterFunctionalComponent.FireControl);
        Soldat = new Soldat(35,2,20,20,Main.ContentImage.soldat_1,functional);
        Veteran = new Soldat(60,7,20,20,Main.ContentImage.soldat_1,functional);
        Jaeger = new Soldat(110,12,20,20,Main.ContentImage.soldat_1,functional);

    }
}
