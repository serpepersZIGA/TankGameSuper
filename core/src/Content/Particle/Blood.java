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
        this.x = x-size*0.5f;
        this.y = y-size*0.5f;

        this.interval_rise_size = 0.02f;
        liquidConst();
    }
    @Override final
    public void all_action(){
        this.size_render = (int)(size*Main.Zoom);
        //liquid_const();
        this.move_particle();
        this.slow_particle();
        center_render();
        this.StateTime = BloodLiquid.render(x_rend,y_rend,size_render,size_render,0,0.5f,this.StateTime);
        super.size_rise_delete(BloodList);
        //Sprite spr = TextureAtl.createSprite("Buffer");
        //spr.setColor(0.1f,0.6f,0.2f,1.0f);

        //super.size_update();
    }

}
