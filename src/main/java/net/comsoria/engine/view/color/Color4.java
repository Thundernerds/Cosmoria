package net.comsoria.engine.view.color;

import net.comsoria.engine.view.GLSL.GLSLUniformBindable;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Color4 extends Color3 {
    public final static Color4 WHITE = new Color4(1);
    public final static Color4 BLACK = new Color4(0);
    public final static Color4 GRAY = new Color4(0.5f);
    public final static Color4 RED = new Color4(1, 0, 0);
    public final static Color4 BLUE = new Color4(0, 0, 1);
    public final static Color4 GREEN = new Color4(0, 1, 0);
    public final static Color4 TRANSPARENT = new Color4(0, 0, 0, 0);

    public float a = 1;

    public Color4() {
        this(0, 0, 0, 1f);
    }

    public Color4(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color4(float value) {
        this(value, value, value);
    }

    public Color4(float x, float y, float z) {
        this(x, y, z, 1f);
    }

    public Color4(Vector3f vec) {
        this(vec.x, vec.y, vec.z, 1f);
    }

    public Color4(Vector4f vec) {
        this(vec.x, vec.y, vec.z, vec.w);
    }

    public Color4(Color4 color) {
        this(color.r, color.g, color.b, color.a);
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
    public float getA() {
        return a;
    }

    public Color4 setA(float a) {
        this.a = a;
        return this;
    }
    public Color4 setR(float r) {
        this.r = r;
        return this;
    }
    public Color4 setG(float g) {
        this.g = g;
        return this;
    }
    public Color4 setB(float b) {
        this.b = b;
        return this;
    }

    public Color4 getOneToZero() {
        return new Color4(this.r / 255f, this.g / 255f, this.b / 255f, this.a);
    }

    public Vector4f getVec4() {
        return new Vector4f(this.r, this.g, this.b, this.a);
    }

    public Color4 mix(Color4 color2, float dist) {
        float x = this.r + ((color2.r - this.r) * dist);
        float y = this.g + ((color2.g - this.g) * dist);
        float z = this.b + ((color2.b - this.b) * dist);
        float w = this.a + ((color2.a - this.a) * dist);
        return new Color4(x, y, z, w);
    }

    public Color4 clone() {
        return new Color4(this);
    }

    public Color4 set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        return this;
    }

    public Color4 set(float x) {
        this.r = x;
        this.g = x;
        this.a = x;

        return this;
    }

    public Color4 set(Color4 color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;

        return this;
    }

    public Color4 set(float r, float g, float b) {
        return this.set(r, g, b, this.a);
    }

    @Override
    public void set(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name, this.getVec4());
    }

    public String toString(boolean full) {
        if (full)
            return "Color3[" + this.r + "," + this.g + "," + this.b + "," + this.a + "]";
        else
            return this.r + " " + this.g + " " + this.b + " " + this.a;
    }

    @Override
    public String toString() {
        return this.toString(true);
    }
}
