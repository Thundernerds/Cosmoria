package net.comsoria.engine.math;

import net.comsoria.engine.view.GLSL.GLSLUniformBindable;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import org.joml.Vector2f;

public class Line implements GLSLUniformBindable {
    public float gradient = 0;
    public float yIntercept = 0;

    public Line(float gradient, float yIntercept) {
        this.gradient = gradient;
        this.yIntercept = yIntercept;
    }

    public Line() {

    }

    public Line(Vector2f p1, Vector2f p2) {
        this.gradient = (p2.y - p1.y) / (p2.x - p1.x);
        this.yIntercept = p1.y - (this.gradient * p1.x);
    }

    public float get(float x) {
        return (x * gradient) + yIntercept;
    }

    public Vector2f intersection(Line line) {
        float x = (line.yIntercept - this.yIntercept) / (this.gradient - line.gradient);
        float y = (this.gradient * x) + this.yIntercept;

        return new Vector2f(x, y);
    }

    public Vector2f getVec2() {
        return new Vector2f(this.gradient, this.yIntercept);
    }

    @Override
    public void set(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name, this.getVec2());
    }
}
