package Content.Particle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.rand;
import com.mygdx.game.particle.Particle;

import static Data.DataColor.*;


public class Acid extends Particle {
    public Acid(float x, float y){
        this.x = x;
        this.y = y;
        this.size = 24+rand.rand(8);
        //this.size_render = (int)(size*Main.zoom);
        this.speed_x = 0;
        this.speed_y = 0;
        r = AcidR; g = AcidG; b = AcidB;
        this.interval_rise_size = 0.02F;
        liquid_const();


    }
    public void all_action(int i){
        super.size_update();
        float[]xy = Main.RC.render_objZoom(this.x,this.y);
        Main.Render.circle(xy[0],xy[1],(int)(size*Main.Zoom),(int)size,new Color(r,g,b,0.6f));
        size_rise_delete();
    }

}
