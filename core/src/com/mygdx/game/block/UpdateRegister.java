package com.mygdx.game.block;

import Content.Block.UpdateDirt;
import Content.Block.UpdateDirt2;
import Content.Block.UpdateGrass;
import Content.Block.UpdateVoid;

public class UpdateRegister {
    public static UpdateBlock VoidUpdate = new UpdateVoid();
    public static UpdateBlock GrassUpdate = new UpdateGrass();
    public static UpdateBlock DirtUpdate = new UpdateDirt();
    public static UpdateBlock Update3 = new UpdateDirt2();
    public static UpdateBlock UpdateAsphalt1 = new UpdateAsphalt1();
}
