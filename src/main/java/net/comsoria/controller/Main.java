package net.comsoria.controller;

import net.comsoria.engine.GameEngine;
import net.comsoria.engine.IGameLogic;
import net.comsoria.engine.utils.Logger;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Main {
    public static final String VERSION = "1.3";

    public static void main(String[] args) throws IOException {
        Logger.log("Started game Cosmoria");
        StartupFileHandler.load();
        GameEngine engine = new GameEngine("Cosmoria", 800, 520, new LoadingHandler(), true);
        engine.start();
    }
}
