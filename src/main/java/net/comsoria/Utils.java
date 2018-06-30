package net.comsoria;

import java.io.*;
import java.net.URL;
import java.util.*;

public final class Utils {
    private static final LinkedHashMap<String, String> directories = new LinkedHashMap<>();
    private static final LinkedHashMap<String, UnwrittenFile> files = new LinkedHashMap<>();

    static {
        directories.put("home", getPath(System.getProperty("user.home") + "/Cosmoria"));
        directories.put("saves", getPath("$home/Saves"));
        directories.put("resources", getPath("$home/Resources"));
        directories.put("shaders", getPath("$resources/Shaders"));
        directories.put("models", getPath("$resources/Models"));
        directories.put("textures", getPath("$resources/Textures"));

        files.put("vertex", new UnwrittenFile("$shaders/vertex.vs", "vertex.vs"));
        files.put("fragment", new UnwrittenFile("$shaders/fragment.vs", "fragment.vs"));
        files.put("hud_vertex", new UnwrittenFile("$shaders/hud_vertex.vs", "hud_vertex.vs"));
        files.put("hud_fragment", new UnwrittenFile("$shaders/hud_fragment.vs", "hud_fragment.vs"));
        files.put("chunk_vertex", new UnwrittenFile("$shaders/chunk_vertex.vs", "chunk_vertex.vs"));
        files.put("chunk_fragment", new UnwrittenFile("$shaders/chunk_fragment.vs", "chunk_fragment.vs"));
        files.put("pp_vertex", new UnwrittenFile("$shaders/postprocessing.v.glsl", "pp_v.v.glsl"));
        files.put("pp_fragment", new UnwrittenFile("$shaders/postprocessing.f.glsl", "pp_f.f.glsl"));
        files.put("skydome_vertex", new UnwrittenFile("$shaders/skydome.v.glsl", "skydome.v.glsl"));
        files.put("skydome_fragment", new UnwrittenFile("$shaders/skydome.f.glsl", "skydome.f.glsl"));

        files.put("cubeobj", new UnwrittenFile("$models/cube.obj", "cube.obj"));
        files.put("compassobj", new UnwrittenFile("$models/compass.obj", "compass.obj"));
        files.put("chunkplaneobj", new UnwrittenFile("$models/chunk_plane.obj", "chunk_plane.obj"));
        files.put("skydomeobj", new UnwrittenFile("$models/skydome.obj", "skydome.obj"));
    }

    public static void init() throws IOException {
        for (String dirName : directories.keySet()) {
            File file = new File(directories.get(dirName));
            if (!file.exists()) file.mkdir();
        }

        for (String file : files.keySet()) {
            UnwrittenFile unwrittenFile = files.get(file);
            unwrittenFile.updateName();
            unwrittenFile.create();
        }
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

    public static String loadResourceAsStringFromStream(InputStream stream) throws IOException {
        StringJoiner stringBuilder = new StringJoiner(System.getProperty("line.separator"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String inputLine;
        while ((inputLine = reader.readLine()) != null)
            stringBuilder.add(inputLine);
        reader.close();

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

    private static class UnwrittenFile {
        private String clientPath;
        private final String gitPath;

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
}
