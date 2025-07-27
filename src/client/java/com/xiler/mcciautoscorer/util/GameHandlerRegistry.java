package com.xiler.mcciautoscorer.util;

public class GameHandlerRegistry {

    private static GameHandler currentHandler;

    public static void set(GameHandler handler) {
        currentHandler = handler;
    }

    public static GameHandler get() {
        return currentHandler;
    }

    public static void reset() {
        if (currentHandler != null) {
            currentHandler.setCurrentGame(null);
            currentHandler.setGameActive(false);
            currentHandler.setAllowScore(true);
            currentHandler.setUsername(null);
            System.out.println("[GameHandlerRegistry] Handler reset.");
        }
    }

}
