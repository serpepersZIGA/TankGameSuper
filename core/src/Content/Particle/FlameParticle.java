package Content.Particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.method.rand;
import com.mygdx.game.main.Main;
import com.mygdx.game.particle.Particle;

import java.text.Format;

import static Data.DataColor.*;

public class FlameParticle extends Particle {
    public FlameParticle(float x, float y){
        this.x = x;
        this.y = y;
        this.time_delete = 25+rand.rand(20);
        this.size = 5+rand.rand(5);
        this.size_render = (int)(size*Main.Zoom);
        this.speed_x = -6+rand.rand(12);
        this.speed_y = -6+rand.rand(12);
        this.r = FlameR;this.g = FlameG;this.b = FlameB;
    }
    public void all_action(int i){
        super.color_fire();
        super.move_particle();
        this.update();
        super.timer(Main.FlameParticleList);
    }
    public void update(){
        float[]xy = Main.RC.render_objZoom(this.x,this.y);
        Main.Render.circle(xy[0],xy[1],size_render,size_render,new Color(r,g,b,0.4f));
    }
}
