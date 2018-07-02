package net.comsoria.engine.view.graph;

import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.Window;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Mesh implements Renderable {
    public Material material;
    public Geometry geometry;

    public Mesh(Geometry geometry, Material material) {
        this.geometry = geometry;
        this.material = material;

//        if (this.material.shaderProgram != null) {
//            this.geometry.bind();
//            this.material.shaderProgram.init();
//            this.geometry.unbind();
//        }
    }

    public void initShaderProgram() throws Exception {
        this.geometry.bind();
        this.material.shaderProgram.init();
        this.geometry.unbind();
    }

    public void cleanup() {
        geometry.cleanup();
        material.cleanup();
    }

    @Override
    public void render(Window window) throws Exception { // material and geometry assumed to be bound
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

        Texture.unbind();
    }
}
