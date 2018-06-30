package net.comsoria.engine.view.graph;

import net.comsoria.engine.view.GLSL.ShaderProgram;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Mesh {
    public Material material;
    public Geometry geometry;
    private boolean initialised = false;

    public static Texture texture;

    public Mesh(Geometry geometry, Material material) {
        this.geometry = geometry;
        this.material = material;

        if (texture == null)
            try {
                texture = new Texture(System.getProperty("user.home") + "/Desktop/img2.png");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void init(Class<? extends ShaderProgram> shaderProgram) throws Exception {
        this.initialised = true;

        if (this.material.shaderProgram == null)
            this.material.shaderProgram = shaderProgram.getConstructor().newInstance();

        this.geometry.bind();
        this.material.shaderProgram.init();
        this.geometry.unbind();
    }

    public boolean needsInitialising() {
        return !initialised;
    }

    public void render() throws Exception {
        if (this.material.textures.size() != this.material.shaderProgram.textures.size())
            throw new Exception("Unequal textures to texture uniforms");

        for (int i = 0; i < this.material.textures.size(); i++) {
            Texture texture = this.material.textures.get(i);
            this.material.shaderProgram.setUniform(this.material.shaderProgram.textures.get(i), i);

            glActiveTexture(GL_TEXTURE0 + i);
            texture.bind();
        }

        this.geometry.enableCull();
        this.geometry.bindAttributes();
        glDrawElements(GL_TRIANGLES, this.geometry.getVertexCount(), GL_UNSIGNED_INT, 0);
        this.geometry.unbindAttributes();
        this.geometry.disableCull();

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup() {
        geometry.cleanup();
        material.cleanup();
    }

    public void bind() {
        this.material.shaderProgram.bind();
        this.geometry.bind();
    }

    public void unbind() {
        this.material.shaderProgram.unbind();
        this.geometry.unbind();
    }
}
