package net.comsoria.engine.view.color;

import net.comsoria.engine.view.GLSL.GLSLUniformBindable;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Color3 implements GLSLUniformBindable {
    public final static Color3 WHITE = new Color3(1);
    public final static Color3 BLACK = new Color3(0);
    public final static Color3 GRAY = new Color3(0.5f);
    public final static Color3 RED = new Color3(1, 0, 0);
    public final static Color3 BLUE = new Color3(0, 0, 1);
    public final static Color3 GREEN = new Color3(0, 1, 0);

    public float r;
    public float g;
    public float b;

    public Color3() {
        this(0, 0, 0);
    }

    public Color3(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color3(float value) {
        this(value, value, value);
    }

    public Color3(Vector3f vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Color3(Vector4f vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Color3(Color3 color) {
        this(color.r, color.g, color.b);
    }

    public float getR() {
        return r;
    }
    public float getG() {
        return g;
    }
    public float getB() {
        return b;
    }

    public Color3 setR(float r) {
        this.r = r;
        return this;
    }
    public Color3 setG(float g) {
        this.g = g;
        return this;
    }
    public Color3 setB(float b) {
        this.b = b;
        return this;
    }

    public Color3 getOneToZero() {
        return new Color4(this.r / 255f, this.g / 255f, this.b / 255f);
    }

    public Vector3f getVec3() {
        return new Vector3f(this.r, this.g, this.b);
    }

    public Color3 mix(Color3 color2, float dist) {
        float x = this.r + ((color2.r - this.r) * dist);
        float y = this.g + ((color2.g - this.g) * dist);
        float z = this.b + ((color2.b - this.b) * dist);
        return new Color3(x, y, z);
    }

    public Color3 clone() {
        return new Color3(this);
    }

    public Color3 set(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;

        return this;
    }

    public Color3 set(float x) {
        this.r = x;
        this.g = x;

        return this;
    }

    public Color3 set(Color3 color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;

        return this;
    }

    @Override
    public void set(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name, this.getVec3());
    }

    public static Color3 valueOf(String string) {
        String[] parts = string.split(" ");

        return new Color3(Float.valueOf(parts[0]), Float.valueOf(parts[1]), Float.valueOf(parts[2]));
    }
}
