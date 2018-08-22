package net.comsoria.engine.loaders;

import net.comsoria.engine.utils.Utils;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.lwjgl.BufferUtils.createByteBuffer;

public class FileLoader {
    public static InputStream loadResourceAsStream(String path) throws FileNotFoundException {
        return new FileInputStream(new File(path));
    }

    public static InputStream loadResourceAsStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
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

    public static String loadResourceAsString(File file) throws IOException {
        return loadResourceAsStringFromStream(loadResourceAsStream(file));
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

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) ;
            }
        } else {
            try (
                    InputStream source = Utils.class.getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = Utils.resizeBuffer(buffer, buffer.capacity() * 2);
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }
}
