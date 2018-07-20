package net.comsoria.engine;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Utils {
    public static final Utils utils = new Utils();

    private Map<String, String> names = new HashMap<>();
    public JSONFile settings;

    public String getPath(String path) {
        for (String name : names.keySet()) {
            path = path.replace(name, names.get(name));
        }
        return path.replace("/", File.separator);
    }

    public void addName(String name, String path) {
        names.put("$" + name, getPath(path));
    }

    public void addName(String name, String path, boolean raw) {
        if (raw) names.put("$" + name, path);
        else addName(name, path);
    }

    public String p(String path) {
        return getPath(path);
    }

    public void createDirs(String[] paths) {
        for (String path : paths) {
            File file = new File(p(path));
            if (!file.exists()) file.mkdir();
        }
    }


    public final static String charset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String UUID() {
        StringJoiner result = new StringJoiner("-");

        int parts = (int) ((Math.random() * 3) + 4);
        for (int i = 0; i < parts; i++) {
            int chars = (int) ((Math.random() * 5) + 10);
            StringBuilder part = new StringBuilder();
            for (int c = 0; c < chars; c++) {
                part.append(charset.charAt((int) (Math.random() * charset.length())));
            }
            result.add(part.toString());
        }

        return result.toString();
    }


    public static List<Integer> toIntList(int[] input) {
        List<Integer> list = new ArrayList<>();
        for (int no : input) {
            list.add(no);
        }
        return list;
    }
}
