package net.comsoria.controller;

import net.comsoria.Utils;
import net.comsoria.engine.GameEngine;
import net.comsoria.engine.IGameLogic;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Utils.init();

        IGameLogic gameLogic = new Game();
        GameEngine engine = new GameEngine("Cosmoria", 800, 520, gameLogic);
        engine.start();
    }
}
