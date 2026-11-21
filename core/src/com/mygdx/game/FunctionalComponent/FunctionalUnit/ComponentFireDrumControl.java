package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.main.Main.Option;

public class ComponentFireDrumControl extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit) {
        unit.FireDrumControl();
        if(unit.ConfShotDrum) {
            unit.IndicatorReloadBetweenInit();
        }
        else{
            unit.IndicatorReloadInit();
        }
    }
    @Override final
    public void FunctionalIterationClientAnHost(Unit unit) {
        unit.FireDrumControl();
        if(unit.ConfShotDrum) {
            unit.IndicatorReloadBetweenInit();
        }
        else{
            unit.IndicatorReloadInit();
        }
    }
//    @Override final
//    public void FunctionalIterationAnClient(Unit unit){
//        if(unit.ConfShotDrum) {
//            unit.IndicatorReloadBetweenInit();
//        }
//        else{
//            unit.IndicatorReloadInit();
//        }
//    }
//    @Override final
//    public void FunctionalIterationOtherAnClient(Unit unit){
//        if(unit.ConfShotDrum) {
//            unit.IndicatorReloadBetweenInit();
//        }
//        else{
//            unit.IndicatorReloadInit();
//        }
//    }
}
