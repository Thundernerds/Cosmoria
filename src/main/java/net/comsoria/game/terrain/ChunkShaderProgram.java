package net.comsoria.game.terrain;

import net.comsoria.engine.Scene;
import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.view.Fog;
import net.comsoria.engine.view.GLSL.Programs.ShaderProgram3D;
import net.comsoria.engine.view.Light.DirectionalLight;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

public class ChunkShaderProgram extends ShaderProgram3D {
    private final float range;

    public ChunkShaderProgram(float range) {
        super();

        this.range = range;
    }

    public void init() throws IOException {
        this.create(FileLoader.loadResourceAsStringFromPath("$shaders/chunk/chunk_vertex.v.glsl"),
                FileLoader.loadResourceAsStringFromPath("$shaders/chunk/chunk_fragment.f.glsl"));

        this.createUniform("projectionMatrix");
        this.createUniform("modelViewMatrix");

        this.createUniform("ambientLight");

        Fog.create(this, "fog");
        DirectionalLight.create(this, "directionalLight");
    }

    @Override
    public void setupScene(Scene scene, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        this.setUniform("projectionMatrix", projectionMatrix);
        this.setUniform("ambientLight", scene.light.ambientLight.getVec3());

        this.setUniform("fog", scene.fog);

        DirectionalLight currDirLight = new DirectionalLight(scene.light.directionalLight);
        Vector4f dir = new Vector4f(currDirLight.direction, 0);
        dir.mul(viewMatrix);
        currDirLight.direction = new Vector3f(dir.x, dir.y, dir.z);
        this.setUniform("directionalLight", currDirLight);
    }

    @Override
    public void setupMesh(Mesh mesh, Matrix4f modelViewMatrix) {
        this.setUniform("modelViewMatrix", modelViewMatrix);
    }
}
