package net.comsoria.engine.loaders;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class WebLoader {
    public static String loadResourceFromNet(String resourceName) throws IOException {
        URL resource = new URL(resourceName);
        return FileLoader.loadResourceAsStringFromStream(resource.openStream());
    }

    public static void copyImageFromNet(String imageUrl, String destinationFile) throws IOException {
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

    public static boolean netIsAvailable() {
        try {
            URL url = new URL("http://www.google.com");
            url.openStream();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
