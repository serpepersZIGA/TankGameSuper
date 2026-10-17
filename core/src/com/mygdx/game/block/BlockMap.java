package com.mygdx.game.block;

import com.mygdx.game.block.Block;
import com.mygdx.game.block.UpdateRegister;
import com.mygdx.game.main.Main;

import java.awt.*;
import java.awt.geom.Area;

import static com.mygdx.game.main.Main.width_block;


public class BlockMap extends Block {
    public BlockMap(int x, int y){
        objMap = Main.VoidObj;
        this.x = x;
        this.y = y;
        block_xy();
        this.render_block = BlockID.get(1);
        Rectangle rect = new Rectangle(x,y,width_block,width_block); // Прямоугольник 1

        // Создаем аффинное преобразование для поворота

        // Преобразование прямоугольников с учетом поворота
        area = new Area(rect);

    }
}
