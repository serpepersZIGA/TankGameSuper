package com.mygdx.game.FunctionalComponent.FunctionalUnit;

import com.mygdx.game.FunctionalComponent.FunctionalComponent;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.main.Main.Option;

public class ComponentFireDrumControl extends FunctionalComponent {
    @Override final
    public void FunctionalIterationAnHost(Unit unit) {
        unit.FireDrumControl();
        if(unit.ConfShotDrum) {
            unit.green_len_reload = (unit.ReloadBetweenTotal / unit.ReloadBetweenShots) * Option.size_x_indicator;
        }
        else{
            unit.green_len_reload = (unit.reload / unit.reload_max) * Option.size_x_indicator;
        }
    }
    @Override final
    public void FunctionalIterationClientAnHost(Unit unit) {
        unit.FireDrumControl();
        if(unit.ConfShotDrum) {
            unit.green_len_reload = (unit.ReloadBetweenTotal / unit.ReloadBetweenShots) * Option.size_x_indicator;
        }
        else{
            unit.green_len_reload = (unit.reload / unit.reload_max) * Option.size_x_indicator;
        }
    }
}
