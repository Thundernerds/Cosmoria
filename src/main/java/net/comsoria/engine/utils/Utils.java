package net.comsoria.engine.utils;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.ByteBuffer;
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

    public static float[] listToArray(List<Float> input) {
        float[] result = new float[input.size()];
        for (int i = 0; i < input.size(); i++) result[i] = input.get(i);
        return result;
    }

    public static float map(float startMin, float startMax, float endMin, float endMax, float number) {
        float pc = (number - startMin) / (startMax - startMin);
        return ((endMax - endMin) * pc) + endMin;
    }

    public static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
