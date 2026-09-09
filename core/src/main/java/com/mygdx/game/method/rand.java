package com.mygdx.game.method;

public class rand {
    public static float rand(float x) {
        return (float)Math.random() * x;
    }
    public static int rand(int x) {
        return (int) (Math.random() * x);
    }
    public static short rand(short x) {
        return (short) (Math.random() * x);
    }
    public static byte rand(byte x) {
        return (byte) (Math.random() * x);
    }
    public static double rand(double x) {
        return Math.random() * x;
    }

}
