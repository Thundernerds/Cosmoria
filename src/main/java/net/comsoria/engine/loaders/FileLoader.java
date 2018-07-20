package net.comsoria.engine.loaders;

import net.comsoria.engine.utils.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class FileLoader {
    public static InputStream loadResourceAsStream(String path) throws FileNotFoundException {
        return new FileInputStream(new File(path));
    }

    public static List<String> loadResourceLines(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(loadResourceAsStream(path)));
        List<String> lines = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }

    public static String loadResourceAsStringFromStream(InputStream stream) throws IOException {
        StringJoiner stringBuilder = new StringJoiner(System.getProperty("line.separator"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String inputLine;
        while ((inputLine = reader.readLine()) != null)
            stringBuilder.add(inputLine);
        reader.close();
        stream.close();

        return stringBuilder.toString();
    }

    public static String loadResourceAsStringFromPath(String path) throws IOException {
        return loadResourceAsString(Utils.utils.getPath(path));
    }

    public static String loadResourceAsString(String path) throws IOException {
        return loadResourceAsStringFromStream(loadResourceAsStream(path));
    }

    public static void writeResource(String path, String text) throws IOException {
        writeResource(new File(path), text);
    }

    public static void writeResource(File file, String text) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.close();
    }

    public boolean fileExists(String path) {
        return new File(path).exists();
    }
}
