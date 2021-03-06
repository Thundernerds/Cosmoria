package net.comsoria.engine.view.GLSL.programs;

import net.comsoria.engine.Scene;
import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
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
        this.createUniform("hasTexture");
    }

    @Override
    public void setupScene(Scene scene, Transformation transformation) {

    }

    @Override
    public void setupMesh(Mesh mesh, Matrix4f modelViewMatrix, Transformation transformation) {
        this.setUniform("projModelMatrix", modelViewMatrix);
        this.setUniform("color", mesh.material.ambientColour);
        this.setUniform("hasTexture", mesh.material.textures.size() == 0? 0:1);

//        System.out.println(mesh.material.ambientColour);
//        System.out.println(mesh.geometry.getVertexCount());
//        System.out.println("-------");
    }
}
