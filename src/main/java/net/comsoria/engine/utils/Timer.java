package net.comsoria.engine.utils;

public final class Timer {
    private static int gameLoopIndex = 0;

    public static long getTime() {
        return System.currentTimeMillis();
    }

    public static int getGameLoopIndex() {
        return gameLoopIndex;
    }

    public static void update() {
        gameLoopIndex += 1;
    }

    public static void update(int inc) {
        gameLoopIndex += inc;
    }
}
