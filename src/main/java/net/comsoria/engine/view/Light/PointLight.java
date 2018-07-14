package net.comsoria.engine.view.Light;

import net.comsoria.engine.view.Color;
import net.comsoria.engine.view.GLSL.GLSLUniformBindable;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import org.joml.Vector3f;

public class PointLight implements GLSLUniformBindable {
    public final Color color;
    public Vector3f position;
    public float intensity;
    public Attenuation attenuation;

    public PointLight(Color color, Vector3f position, float intensity) {
        attenuation = new Attenuation(1, 0, 0);
        this.color = color.setTransparent(false);
        this.position = position;
        this.intensity = intensity;
    }

    public PointLight(Color color, Vector3f position, float intensity, Attenuation attenuation) {
        this(color, position, intensity);
        this.attenuation = attenuation;
    }

    public PointLight(PointLight pointLight) {
        this(pointLight.color.clone(), new Vector3f(pointLight.position), pointLight.intensity, pointLight.attenuation);
    }

    @Override
    public void set(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name + ".colour", this.color.getVec3());
        shaderProgram.setUniform(name + ".position", this.position);
        shaderProgram.setUniform(name + ".intensity", this.intensity);
        PointLight.Attenuation att = this.attenuation;
        shaderProgram. setUniform(name + ".att.constant", att.constant);
        shaderProgram.setUniform(name + ".att.linear", att.linear);
        shaderProgram.setUniform(name + ".att.exponent", att.exponent);
    }

    public static void create(ShaderProgram shaderProgram, String name) {
        shaderProgram.createUniform(name + ".colour");
        shaderProgram.createUniform(name + ".position");
        shaderProgram.createUniform(name + ".intensity");
        shaderProgram.createUniform(name + ".att.constant");
        shaderProgram.createUniform(name + ".att.linear");
        shaderProgram.createUniform(name + ".att.exponent");
    }

    public static class Attenuation {
        public float constant;
        public float linear;
        public float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }
    }
}
