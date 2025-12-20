package com.mygdx.game.Parsing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class buff{
    public List<String> Cannon;
    public String Corpus;
    public String Engine;
    public int[][]TowerXY;
    public int MedicConf,Height;
}