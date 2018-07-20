package net.comsoria.engine.view.GLSL.Programs;

import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Matrix4f;

import java.io.IOException;

public class ShaderProgram2D extends ShaderProgram {
    public ShaderProgram2D() {
        super();
    }

    public void init() throws IOException {
        this.create(FileLoader.loadResourceAsStringFromPath("$shaders/hud/hud_vertex.v.glsl"),
                FileLoader.loadResourceAsStringFromPath("$shaders/hud/hud_fragment.f.glsl"));

        this.createUniform("projModelMatrix");
        this.createUniform("color");
    }

    @Override
    public void setupMesh(Mesh mesh, Matrix4f modelViewMatrix) {
        mesh.material.shaderProgram.setUniform("projModelMatrix", modelViewMatrix);
        mesh.material.shaderProgram.setUniform("color", mesh.material.ambientColour);
    }
}
