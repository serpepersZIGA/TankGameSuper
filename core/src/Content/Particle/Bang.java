package Content.Particle;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.main.Main;
import com.mygdx.game.method.rand;
import com.mygdx.game.particle.Particle;


public class Bang extends Particle {
    public Bang(float x, float y, float size){
        this.x = x;
        this.y = y;
        this.size = size;
        this.size_render = (int)(size*Main.Zoom);
        this.interval_rise_size = 12;
        this.time_delete = 30;
        this.r = (float)1/255*(160+rand.rand(70));
        this.g = (float)1/255*(90+rand.rand(60));
        this.b = (float)1/255*(20+rand.rand(3));
    }
    public void all_action(int i) {
        size_rise();
        float[]xy = Main.RC.render_objZoom(this.x,this.y);
        Main.Render.circle(xy[0],xy[1],(int)(size* Main.Zoom),(int)(size* Main.Zoom),new Color(r,g,b,(float)0.4));
        timer(Main.BangList);
    }

}
