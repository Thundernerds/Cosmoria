package net.comsoria;

import org.joml.Vector2f;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;

public final class Utils {
    private static final LinkedHashMap<String, String> directories = new LinkedHashMap<>();
    private static final LinkedHashMap<String, UnwrittenFile> files = new LinkedHashMap<>();
    private static final String charset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static JSONObject settings;
    private static boolean couldStart = false;

    static {
        directories.put("home", System.getProperty("user.home") + "/Cosmoria");
        directories.put("saves", "$home/Saves");
        directories.put("resources", "$home/Resources");
        directories.put("shaders", "$resources/Shaders");
        directories.put("models", "$resources/Models");
        directories.put("textures", "$resources/Textures");
        directories.put(UUID(), "$shaders/chunk");
        directories.put(UUID(), "$shaders/main");
        directories.put(UUID(), "$shaders/hud");
        directories.put(UUID(), "$shaders/postprocessing");
        directories.put(UUID(), "$shaders/skydome");

        try {
            if (netIsAvailable()) {
                String csv = loadResourceFromGithub("gitPath.csv");

                String[] lines = csv.split("\n");
                for (String line : lines) {
                    if (line.trim().equals("")) continue;

                    String[] paths = line.replace("\"", "").split(",");
                    UnwrittenFile unwrittenFile = paths[1].endsWith(".png")? new UnwrittenImage(paths[1], paths[2]):new UnwrittenFile(paths[1], paths[2]);
                    files.put(paths[0], unwrittenFile);
                }
                couldStart = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String UUID() {
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

    public static void saveSettings() throws IOException {
        writeResource("$settings", settings.toString(4));
    }

    private static boolean netIsAvailable() {
        try {
            URL url = new URL("http://www.google.com");
            url.openStream();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void init() throws IOException {
        for (String dirName : directories.keySet()) {
            directories.put(dirName, getPath(directories.get(dirName)));
            File file = new File(directories.get(dirName));
            if (!file.exists()) file.mkdir();
        }

        for (String file : files.keySet()) {
            UnwrittenFile unwrittenFile = files.get(file);
            unwrittenFile.updateName();
            unwrittenFile.create();
        }

        if (!couldStart) {
            if (!fileExists("$home/settings.json")) {
                System.err.println("Failed to start - An internet connection is needed the first time you start the game");
                System.exit(-1);
            } else {
                String csv = loadResourceAsString("$home/gitPath.csv");

                String[] lines = csv.split("\n");
                for (String line : lines) {
                    if (line.trim().equals("")) continue;

                    String[] paths = line.replace("\"", "").split(",");
                    UnwrittenFile unwrittenFile = paths[1].endsWith(".png")? new UnwrittenImage(paths[1], paths[2]):new UnwrittenFile(paths[1], paths[2]);
                    files.put(paths[0], unwrittenFile);

                    unwrittenFile.updateName();
                    unwrittenFile.create();
                }
            }
        }
        settings = new JSONObject(loadResourceAsString("$settings"));
        settings.put("last", new Date().getTime());
        saveSettings();
    }

    public static InputStream loadResourceAsStream(String path) throws FileNotFoundException {
        return new FileInputStream(new File(getPath(path)));
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

    public static String loadResourceFromGithub(String resourceName) throws IOException {
        URL resource = new URL("https://raw.githubusercontent.com/Thundernerds/CosmoriaResources/master/" + resourceName);
        return loadResourceAsStringFromStream(resource.openStream());
    }

    public static void copyImageFromGithub(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/Thundernerds/CosmoriaResources/master/" + imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
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

    public static String loadResourceAsString(String path) throws IOException {
        return loadResourceAsStringFromStream(loadResourceAsStream(path));
    }

    public static void writeResource(String path, String text) throws IOException {
        FileWriter writer = new FileWriter(new File(getPath(path)));
        writer.write(text);
        writer.close();
    }

    public static void writeResource(File file, String text) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.close();
    }

    private static String getPath(String path) {
        for (String key : directories.keySet()) {
            path = path.replace("$" + key, directories.get(key));
        }
        for (String key : files.keySet()) {
            path = path.replace("$" + key, files.get(key).clientPath);
        }
        return path.replace("/", File.separator);
    }

    public static boolean fileExists(String path) {
        return new File(getPath(path)).exists();
    }

    private static class UnwrittenFile {
        String clientPath;
        final String gitPath;

        UnwrittenFile(String clientPath, String gitPath) {
            this.clientPath = clientPath;
            this.gitPath = gitPath;
        }

        void updateName() {
            this.clientPath = getPath(clientPath);
        }

        void create() throws IOException {
            File file = new File(clientPath);
            if (!file.exists()) {
                file.createNewFile();
                writeResource(file, loadResourceFromGithub(this.gitPath));
            }
        }
    }

    private static class UnwrittenImage extends UnwrittenFile {
        UnwrittenImage(String clientPath, String gitPath) {
            super(clientPath, gitPath);
        }

        void create() throws IOException {
            if (!new File(clientPath).exists())
                copyImageFromGithub(this.gitPath, this.clientPath);
        }
    }

    public static List<Integer> toIntList(int[] input) {
        List<Integer> list = new ArrayList<>();
        for (int no : input) {
            list.add(no);
        }
        return list;
    }

    public static float[] toFloatArray(List<Float> list) {
        float[] result = new float[list.size()];
        for (int i = 0; i < list.size(); i++)
            result[i] = list.get(i);
        return result;
    }

    public static void log(float[] array) {
        StringBuilder string = new StringBuilder();
        for (float f : array) string.append(f).append(" ");
        System.out.println(string.toString());
    }

    public static void log(int[] array) {
        StringBuilder string = new StringBuilder();
        for (int f : array) string.append(f).append(" ");
        System.out.println(string.toString());
    }

    public static float toAngle(Vector2f vector2f) {
        return (float) Math.atan2(vector2f.x, vector2f.y);
    }
}
