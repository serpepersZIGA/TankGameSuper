package com.mygdx.game.Shader;


import com.mygdx.game.main.Main;


import static com.mygdx.game.main.Main.*;


public class LiquidShader {



    public static void LiquidShaderAdd() {
    }
    // Bullet.java spawns Acid particles into LiquidList from the background
    // bullet-iteration thread (an acid round hitting something), while this
    // runs on the GL thread - without a lock around both sides, a LinkedList
    // being walked here while it's mutated there corrupts the list's node
    // chain (NPE inside LinkedList.node()). R_LOCK is the same lock already
    // used elsewhere in this codebase for exactly this kind of cross-thread
    // list access.
    public static void AcidShaderIteration() {
        R_LOCK.lock();
        try {
            for (i = 0; i < Main.LiquidList.size(); i++) {
                Main.LiquidList.get(i).all_action();
            }
        } finally {
            R_LOCK.unlock();
        }
    }
    public static void BloodShaderIteration() {
        R_LOCK.lock();
        try {
            for (i = 0; i < BloodList.size(); i++) {
                Main.BloodList.get(i).all_action();
            }
        } finally {
            R_LOCK.unlock();
        }
    }
}
