package Content.Particle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Shader.LightingMainSystem;
import com.mygdx.game.block.Block;
import com.mygdx.game.method.rand;
import com.mygdx.game.main.Main;
import com.mygdx.game.particle.Particle;

import static Data.DataColor.RGBFlame;
import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.Batch;
import static com.mygdx.game.main.Main.LightSystem;

public class FlameSpawn extends Particle {
    public FlameSpawn(float x, float y){
        this.x = x;
        this.y = y;
        this.size = 70;
        this.size_render = (int) (size*Main.Zoom);
        this.size_render2 = size_render/2;
        this.time_delete = 400;
        grass_delete();
        rgb = RGBFlame;
        light = LightSystem.addLight().set(this.x,this.y,new Color(RGBFlame[0],RGBFlame[1]
                ,RGBFlame[2],0.3f),3.2f,420,0.2f);

    }
    @Override final
    public void all_action(int i){
        //spawn_flame();
        sound_play();
        center_render();//Block.LightingAir(x_rend,y_rend,rgb);
        Batch.draw(TextureAtl.createSprite("Buffer"),this.x_rend-size_render2,this.y_rend-size_render2
                ,size_render,size_render);
        //Batch.draw(new Texture());
        timerFlame(Main.FlameSpawnList);
    }
}