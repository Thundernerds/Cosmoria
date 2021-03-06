package net.comsoria.engine.view.light;

import net.comsoria.engine.view.color.Color3;
import net.comsoria.engine.view.GLSL.GLSLUniformBindable;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import org.joml.Vector3f;

public class DirectionalLight implements GLSLUniformBindable {
    public final Color3 color;
    public Vector3f direction;
    public float intensity;

    public DirectionalLight(Color3 color, Vector3f direction, float intensity) {
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
    }

    public DirectionalLight(DirectionalLight light) {
        this(light.color.clone(), new Vector3f(light.direction), light.intensity);
    }

    @Override
    public void set(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name + ".color", this.color.getVec3());
        shaderProgram.setUniform(name + ".direction", this.direction);
        shaderProgram.setUniform(name + ".intensity", this.intensity);
    }

    public static void create(ShaderProgram shaderProgram, String name) {
        shaderProgram.createUniform(name + ".color");
        shaderProgram.createUniform(name + ".direction");
        shaderProgram.createUniform(name + ".intensity");
    }
}
