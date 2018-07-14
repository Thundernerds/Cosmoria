package net.comsoria.engine.view.Light;

import net.comsoria.engine.view.GLSL.GLSLUniformBindable;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import org.joml.Vector3f;

public class SpotLight implements GLSLUniformBindable {
    public PointLight pointLight;
    public Vector3f coneDirection;
    public float cutOff;

    public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle) {
        this.pointLight = pointLight;
        this.coneDirection = coneDirection;
        setCutOffAngle(cutOffAngle);
    }

    public SpotLight(SpotLight spotLight) {
        this(new PointLight(spotLight.pointLight), new Vector3f(spotLight.coneDirection), 0);
        this.cutOff = spotLight.cutOff;
    }

    public final void setCutOffAngle(float cutOffAngle) {
        this.cutOff = (float) Math.cos(Math.toRadians(cutOffAngle));
    }

    @Override
    public void set(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name + ".pl", this.pointLight);
        shaderProgram.setUniform(name + ".conedir", this.coneDirection);
        shaderProgram.setUniform(name + ".cutoff", this.cutOff);
    }

    public static void create(ShaderProgram shaderProgram, String name) {
        PointLight.create(shaderProgram, name + ".pl");
        shaderProgram.createUniform(name + ".conedir");
        shaderProgram.createUniform(name + ".cutoff");
    }
}
