package net.comsoria.engine.view.graph;

import de.matthiasmann.twl.utils.PNGDecoder;
import net.comsoria.engine.loaders.FileLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private final int id;

    public Texture(String fileName) throws Exception {
        this(loadTexture(fileName));
    }

    public Texture(InputStream is) throws Exception {
        this(loadTexture(is));
    }

    public Texture(int id) {
        this.id = id;
    }

    public Texture(int width, int height) {
        this(generateTexture(width, height));
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;
    }

    public static int loadTexture(String fileName) throws Exception {
        return loadTexture(FileLoader.loadResourceAsStream(fileName));
    }

    private static int loadTexture(InputStream inputStream) throws Exception {
        // Load Texture file
        PNGDecoder decoder = new PNGDecoder(inputStream);

        // Load texture contents into a byte buffer
        ByteBuffer buf = ByteBuffer.allocateDirect(
                4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buf.flip();

        // Create a new OpenGL texture
        return generateTexture(buf, decoder.getWidth(), decoder.getHeight());
    }

    private static int generateTexture(ByteBuffer buffer, int width, int height) {
        int textureId = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);
        return textureId;
    }

    private static int generateTexture(int width, int height) {
        int textureId = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);
        return textureId;
    }

    public void cleanup() {
        glDeleteTextures(id);
    }

    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
