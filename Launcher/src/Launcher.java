import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Launcher {
    private final static Pattern pattern = Pattern.compile("([0-9]+)\\.([0-9]+)\\.([0-9_]+)");

    private static void copyImageFromNet(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
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

    public static void main(String[] args) throws Exception {
        final String os = System.getProperty("os.name");

        File home = new File(System.getProperty("user.home") + "/Cosmoria");
        String jarPath = home + "/Game.jar";
        if (os.contains("Mac")) {
            if (!home.exists()) {
                home.mkdir();
            }
            if (!new File(jarPath).exists())
                copyImageFromNet("https://github.com/Thundernerds/Cosmoria/releases/download/v1.2/Cosmoria.jar", home + "/Game.jar");
        }

        String javaPath = os.contains("Mac")? "/Library/Java/JavaVirtualMachines/":"C:\\Program Files\\Java\\";

        File[] files = new File(javaPath).listFiles();

        List<String> names = new ArrayList<>();

        int max = -1;
        File found = null;

        for (File file : files) {
            Matcher matcher = pattern.matcher(file.getAbsolutePath());
            if (matcher.find()) {
                boolean skipped = matcher.group(1).equals("1");
                int[] parts = new int[]{
                        Integer.parseInt(skipped? matcher.group(2) : matcher.group(1)),
                        Integer.parseInt(skipped? matcher.group(3).replace("_", "") : matcher.group(2)),
                        skipped? 0 : Integer.parseInt(matcher.group(3).replace("_", ""))
                };

                int score = (parts[0] * 100) + (parts[1] / 10) + (parts[2] / 100);
                if (score > max) {
                    max = score;
                    found = file;
                }
            }
        }

//        System.out.print(found);
        if (max < 900) throw new Exception("ERROR: Cannot run with a java below 1.9");

        if (os.contains("Mac"))
            Runtime.getRuntime().exec("" + found + "/Contents/Home/bin/java -jar -XstartOnFirstThread " + jarPath);
    }
}
