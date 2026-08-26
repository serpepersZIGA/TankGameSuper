package com.mygdx.game.Parsing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class buffObjMap {
    public String Image, Collision;
    public int X,Y,width,height,HP;
    public boolean LightingConf,SpawnUnit;
    public int Lighting;
}
