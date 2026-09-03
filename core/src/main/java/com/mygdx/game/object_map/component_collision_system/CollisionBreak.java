package com.mygdx.game.object_map.component_collision_system;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.Sound.SoundPlay;
import com.mygdx.game.main.Main;
import com.mygdx.game.Network.PacketMapObject;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.main.Main.LightSystem;

public class CollisionBreak extends ComponentCollisionSystem{
    private Sound crushSound;
    private int crushSoundId = -1;
    public CollisionBreak(int x,int y,int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    /** Same as above, but plays a sound (see SoundRegister.IDSound for the id) when crushed. */
    public CollisionBreak(int x,int y,int width,int height,Sound crushSound,int crushSoundId){
        this(x,y,width,height);
        this.crushSound = crushSound;
        this.crushSoundId = crushSoundId;
    }
    @Override final
    public void collision(Unit tr, int ix, int iy) {
        if(rect_collision(x,y,width,height,0,
                (int)tr.x,(int)tr.y,(int)tr.corpus_width,(int)tr.corpus_height,tr.rotation_corpus)){
            tr.speed *= 0.2f;
            tr.SpeedInertionY *= 0.2f;
            tr.SpeedInertionX *= 0.2f;
            int n = Main.PacketServer.mapObject.size();
            Main.PacketServer.mapObject.add(new PacketMapObject());
            Main.PacketServer.mapObject.get(n).ix = ix;
            Main.PacketServer.mapObject.get(n).iy = iy;
            LightSystem.lights.remove(Main.BlockList2D.get(iy).get(ix).objMap.light);
            if(crushSound != null){
                int[] xy = Main.RC.render_objZoom(this.x, this.y);
                SoundPlay.soundPlay(xy[0], xy[1], this.x, this.y, crushSoundId, crushSound);
            }
            Main.BlockList2D.get(iy).get(ix).objMap = Main.VoidObj;
            //Main.BlockList2D.get(iy).get(ix).objMap.assets = "";
            //Main.BlockList2D.get(iy).get(ix).objMap.lighting

        }

    }
}
