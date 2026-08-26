package com.mygdx.game.main;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.mygdx.game.main.Main.BulletList;
import static com.mygdx.game.main.Main.UnitList;

public class ActionGame {
    public static ActionGame ActionGameH = new ActionGameHost();
    public static ActionGame ActionGameCl = new ActionGameClient();
    public static ActionGame ActionMenu = new ActionMenu();
    public void action() throws ExecutionException, InterruptedException {

    }
    public void ThreadAllAdd(){


    }
}
