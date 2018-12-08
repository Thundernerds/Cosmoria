package net.comsoria.controller;

import net.comsoria.engine.utils.JSONFile;
import net.comsoria.engine.utils.Logger;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.loaders.CSVLoader;
import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.loaders.WebLoader;

import java.io.*;
import java.util.Date;

public class StartupFileHandler {
    public static void load() throws IOException {
        Utils.utils.addName("home", System.getProperty("user.home") + "/Cosmoria");
        Utils.utils.addName("saves", "$home/saves");
        Utils.utils.addName("res", "$home/resources");

        Utils.utils.addName("shaders", "$res/shaders");
        Utils.utils.addName("models", "$res/models");
        Utils.utils.addName("textures", "$res/textures");
        Utils.utils.addName("uis", "$res/UIs");
        Utils.utils.addName("fonts", "$res/fonts");
        Utils.utils.addName("audio", "$res/audio");
        Utils.utils.addName("logs", "$home/logs");

        Utils.utils.createDirs(new String[] {
                "$home", "$saves", "$res", "$models", "$shaders", "$textures", "$uis", "$fonts", "$audio", "$logs"
        });

        File log = new File(Utils.utils.p("$logs/v" + Main.VERSION + ".log"));
        if (!log.exists()) log.createNewFile();

        String old = FileLoader.loadResourceAsString(log);
        PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(log)), true);
        Logger.addOutputStream(out);
        out.println(old + "\n-----------------------------------------------");
//        System.setOut(out);
//        System.setErr(out);
//        System.out.println(old);
//        System.out.println("-----------------------------------------------");
        Logger.log("Began on new output stream");

        Logger.log("Generating files...");

        File settings = new File(Utils.utils.p("$home/settings.json"));

        Utils.utils.addName("git", "https://raw.githubusercontent.com/Thundernerds/CosmoriaResources/master/", true);
        try {
            Logger.log("Downloading new data...");
            CSVLoader csvFile = new CSVLoader(WebLoader.loadResourceFromNet(Utils.utils.np("$git/gitPath.csv")));

            for (int i = 0; i < csvFile.rows(); i++) {
                CSVLoader.Row row = csvFile.getRow(i);

                String path = Utils.utils.getPath(row.getPart(0));
                String gitPath = row.getPart(1);

                File file = new File(path);
                if (!file.exists()) {
                    if (gitPath.equals("null")) {
                        file.mkdir();
                    } else if (gitPath.endsWith(".png") || gitPath.endsWith(".ttf") || gitPath.endsWith(".ogg")) {
                        WebLoader.copyImageFromNet(Utils.utils.np("$git/" + gitPath), path);
                    } else {
                        FileLoader.writeResource(file, WebLoader.loadResourceFromNet(Utils.utils.np("$git/" + gitPath)));
                    }
                }
            }
        } catch (Exception e) {
            if (!settings.exists()) {
                System.err.println("Failed to start - An internet connection is needed the first time you start the game");
                System.exit(-1);
            }
        }

        Utils.utils.settings = new JSONFile(settings);
        Utils.utils.settings.put("last", new Date().getTime());
        Utils.utils.settings.save();
    }
}
