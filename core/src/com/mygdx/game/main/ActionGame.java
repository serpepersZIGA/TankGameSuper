package com.mygdx.game.main;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.mygdx.game.main.Main.BulletList;
import static com.mygdx.game.main.Main.UnitList;

public class ActionGame {
    public static ExecutorService executor = Executors.newFixedThreadPool(3);
    public static ActionGame ActionGameH = new ActionGameHost();
    public static ActionGame ActionGameCl = new ActionGameClient();
    public static ActionGame ActionMenu = new ActionMenu();
    public void action() throws ExecutionException, InterruptedException {

    }
    public static void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
    public static void submitTask(Runnable task) {
        if (!executor.isShutdown()) {
            executor.submit(task);
        } else {
            System.out.println("Не удалось добавить задачу: executor завершён.");
        }
    }
    public void ThreadAllAdd(){


    }
}
