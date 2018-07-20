package net.comsoria.controller;

import net.comsoria.engine.JSONFile;
import net.comsoria.engine.Utils;
import net.comsoria.engine.loaders.CSVLoader;
import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.loaders.WebLoader;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class StartupFileHandler {
    private final static String github = "";

    public static void load() throws IOException {
        Utils.utils.addName("home", System.getProperty("user.home") + "/Cosmoria");
        Utils.utils.addName("saves", "$home/saves");
        Utils.utils.addName("res", "$home/resources");
        Utils.utils.addName("shaders", "$res/shaders");
        Utils.utils.addName("models", "$res/models");
        Utils.utils.addName("textures", "$res/textures");
        Utils.utils.createDirs(new String[] {
                "$home", "$saves", "$res", "$models", "$shaders", "$textures"
        });

        File settings = new File(Utils.utils.p("$home/settings.json"));

        Utils.utils.addName("git", "https://raw.githubusercontent.com/Thundernerds/CosmoriaResources/master/", true);
        try {
            CSVLoader csvFile = new CSVLoader(WebLoader.loadResourceFromNet(Utils.utils.p("$git/gitPath.csv")));

            for (int i = 0; i < csvFile.rows(); i++) {
                CSVLoader.Row row = csvFile.getRow(i);

                String path = Utils.utils.getPath(row.getPart(0));
                String gitPath = row.getPart(1);

                if (gitPath.equals("null")) {
                    new File(path).mkdir();
                } else if (gitPath.endsWith(".png")) {
                    WebLoader.copyImageFromNet(Utils.utils.p("$git/" + gitPath), path);
                } else {
                    FileLoader.writeResource(path, WebLoader.loadResourceFromNet(Utils.utils.p("$git/" + gitPath)));
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