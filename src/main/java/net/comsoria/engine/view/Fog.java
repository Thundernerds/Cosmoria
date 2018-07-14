package net.comsoria.engine.view;

import net.comsoria.engine.view.GLSL.GLSLException;
import net.comsoria.engine.view.GLSL.GLSLUniformBindable;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Fog implements GLSLUniformBindable {
    public float density;
    public float start;

    public Fog() {
        this.density = 0;
        this.start = 0;
    }

    public Fog(float density, float start) {
        this.density = density;
        this.start = start;
    }

    public static void create(ShaderProgram shaderProgram, String name) {
        shaderProgram.createUniform(name + ".density");
        shaderProgram.createUniform(name + ".start");
    }

    @Override
    public void set(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name + ".density", this.density);
        shaderProgram.setUniform(name + ".start", this.start);
    }
}
