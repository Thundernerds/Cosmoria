package net.comsoria.game.terrain;

import net.comsoria.Utils;
import net.comsoria.engine.Scene;
import net.comsoria.engine.view.GLSL.Programs.ShaderProgram3D;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Matrix4f;

public class ChunkShaderProgram extends ShaderProgram3D {
    private final float range;

    public ChunkShaderProgram(float range) throws Exception {
        super();

        this.range = range;
    }

    public void init() throws Exception {
        this.create(Utils.loadResourceAsString("$chunk_vertex"), Utils.loadResourceAsString("$chunk_fragment"));

        this.createUniform("projectionMatrix");
        this.createUniform("modelViewMatrix");

        this.createUniform("ambientLight");

        this.createFogUniform("fog");
        this.createDirectionalLightUniform("directionalLight");
    }

    @Override
    public void setupScene(Scene scene, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        this.setUniform("projectionMatrix", projectionMatrix);
        this.setUniform("ambientLight", scene.light.ambientLight);

        this.setUniform("fog", scene.fog);
        this.setUniform("directionalLight", scene.light.directionalLight);
    }

    @Override
    public void setupMesh(Mesh mesh, Matrix4f modelViewMatrix) {
        this.setUniform("modelViewMatrix", modelViewMatrix);
    }
}
