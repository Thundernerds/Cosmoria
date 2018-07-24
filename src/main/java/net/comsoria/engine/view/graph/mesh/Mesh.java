package net.comsoria.engine.view.graph.mesh;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.batch.RenderData;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.Closeable;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Mesh implements Renderable {
    public Material material;
    public Geometry geometry;

    public final Vector3f position = new Vector3f();
    public final Vector3f rotation = new Vector3f();
    public float scale = 1f;
    public boolean visible = true;
    public RenderOrder renderPosition = RenderOrder.Any;

    protected final static Matrix4f modelViewMatrix = new Matrix4f();

    public Mesh(Geometry geometry, Material material) {
        this.geometry = geometry;
        this.material = material;
    }

    public void initShaderProgram() throws IOException {
        this.geometry.bind();
        this.material.shaderProgram.init();
        this.geometry.unbind();
    }

    public void cleanup() {
        geometry.cleanup();
        material.cleanup();
    }

    @Override public boolean shouldRender() {
        return visible;
    }

    @Override public RenderOrder getRenderOrder() {
        return renderPosition;
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

    @Override public Closeable render(Transformation transformation, Scene scene, RenderData renderData) throws Exception {
        if (renderData.shouldBindOwnGeometry()) this.geometry.bind();
        if (renderData.shouldBindOwnShaderProgram()) this.material.shaderProgram.bind();

        if (!this.material.shaderProgram.isUpdated()) {
            this.material.shaderProgram.open();
            this.material.shaderProgram.setupScene(scene, transformation);
        }

        this.material.shaderProgram.setupMesh(this, getModelViewMatrix(transformation), transformation);

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

        if (this.material.textures.size() != 0) Texture.unbind();

        if (renderData.shouldBindOwnGeometry()) this.geometry.unbind();
        if (renderData.shouldBindOwnShaderProgram()) this.material.shaderProgram.unbind();

        return this.material.shaderProgram;
    }
}
