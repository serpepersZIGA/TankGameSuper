package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.main.Main.Option;

public class ComponentFireClassisControl extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit) {
        unit.FireControl();
        //System.out.println(unit.green_len_reload);
        //System.out.println(unit.reload+" "+unit.reload_max);
        unit.IndicatorReloadInit();
    }
    @Override final
    public void FunctionalIterationClientAnHost(Unit unit) {
        unit.FireControl();
        unit.IndicatorReloadInit();
    }

//    @Override final
//    public void FunctionalIterationAnClient(Unit unit){
//        unit.IndicatorReloadInit();
//    }
//    @Override final
//    public void FunctionalIterationOtherAnClient(Unit unit){
//        unit.IndicatorReloadInit();
//    }
}
