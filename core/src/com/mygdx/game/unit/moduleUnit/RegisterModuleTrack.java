package com.mygdx.game.unit.moduleUnit;

import java.util.HashMap;

public class RegisterModuleTrack {
    public static Track Track;
    public static HashMap<String,Track>TrackID = new HashMap<>();
    public static void Create(){
        Track = new Track("Track",15,130,"image/animation/AnimationTrackUp.png",
                "image/animation/animationTrack.png");
    }
}
