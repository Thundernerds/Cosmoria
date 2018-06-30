package net.comsoria.engine.view.GLSL.Programs;

import net.comsoria.Utils;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.Mesh;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram2D extends ShaderProgram {
    public ShaderProgram2D() throws Exception {
        super();
    }

    public void init() throws Exception {
        this.create(Utils.loadResourceAsString("$hud_vertex"), Utils.loadResourceAsString("$hud_fragment"));

        this.createUniform("projModelMatrix");
        this.createUniform("color");
//        this.createUniform("texture_0");
    }

    @Override
    public void setupMesh(Mesh mesh, Matrix4f modelViewMatrix) {
        mesh.material.shaderProgram.setUniform("projModelMatrix", modelViewMatrix);
        mesh.material.shaderProgram.setUniform("color", mesh.material.ambientColour);
    }
}
