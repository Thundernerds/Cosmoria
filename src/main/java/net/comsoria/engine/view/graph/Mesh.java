package net.comsoria.engine.view.graph;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.GLSL.Transformation0;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.Closeable;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Mesh implements Renderable {
    public Material material;
    public Geometry geometry;

    public final Vector3f position = new Vector3f();
    public final Vector3f rotation = new Vector3f();
    public float scale = 1f;
    public boolean visible = true;

    final static Matrix4f modelViewMatrix = new Matrix4f();

    public Mesh(Geometry geometry, Material material) {
        this.geometry = geometry;
        this.material = material;
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
    public boolean shouldRender() {
        return visible;
    }

    public Matrix4f getModelViewMatrix(Transformation transformation) {
        modelViewMatrix.identity().translate(this.position).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(this.scale);

        Matrix4f viewCurr = new Matrix4f(transformation.view);
        return viewCurr.mul(modelViewMatrix);
    }

    @Override
    public Closeable render(Window window, Transformation transformation, Scene scene) throws Exception {
        this.geometry.bind();
        this.material.shaderProgram.bind();

        if (!this.material.shaderProgram.isUpdated()) {
            this.material.shaderProgram.open();
            this.material.shaderProgram.setupScene(scene, transformation.projection, transformation.view);
        }

        this.material.shaderProgram.setupMesh(this, getModelViewMatrix(transformation));

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

        this.geometry.unbind();
        this.material.shaderProgram.unbind();

        return this.material.shaderProgram;
    }
}
