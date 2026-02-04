package Content.Particle;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.RenderMethod;
import com.mygdx.game.method.rand;
import com.mygdx.game.particle.Particle;

import static Data.DataColor.*;
import static Data.DataImage.TextureAtl;
import static com.mygdx.game.main.Main.Batch;
import static com.mygdx.game.main.Main.BloodList;


public class Blood extends Particle {
    public Blood(float x, float y){
        this.size = 42+rand.rand(8);
        this.x = x-size/2;
        this.y = y-size/2;

        this.speed_x = 0;
        this.speed_y = 0;
        r = BloodR;g = BloodG;b = BloodB;
        this.interval_rise_size = 0.02f;
        liquidConst();
    }
    @Override final
    public void all_action(){
        this.size_render = (int)(size*Main.Zoom);
        //liquid_const();
        super.size_rise_delete(BloodList);
        this.move_particle();
        this.slow_particle();
        center_render();
        //Sprite spr = TextureAtl.createSprite("Buffer");
        //spr.setColor(0.1f,0.6f,0.2f,1.0f);
        Batch.draw(TextureAtl.createSprite("Buffer"),this.x_rend,
                this.y_rend
                ,size_render,size_render);
        //super.size_update();
    }

}
