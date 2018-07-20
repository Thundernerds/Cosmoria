package net.comsoria.controller;

import net.comsoria.engine.GameEngine;
import net.comsoria.engine.IGameLogic;
import net.comsoria.engine.utils.Logger;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Logger.log("Started game Cosmoria");

        IGameLogic gameLogic = new Game();
        GameEngine engine = new GameEngine("Cosmoria", 800, 520, gameLogic, true);
        engine.start();
    }
}
