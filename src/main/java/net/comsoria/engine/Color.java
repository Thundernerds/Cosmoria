package net.comsoria.engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Color {
    public final static Color WHITE = Color.grayScale(1);
    public final static Color BLACK = Color.grayScale(0);
    public final static Color GRAY = Color.grayScale(0.5f);
    public final static Color RED = new Color(1, 0, 0);
    public final static Color BLUE = new Color(0, 0, 1);
    public final static Color GREEN = new Color(0, 1, 0);

    public float r;
    public float g;
    public float b;
    public float a = 1;

    public Color() {
        this(0, 0, 0, 1f);
    }

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(float x, float y, float z) {
        this(x, y, z, 1f);
    }

    public Color(Vector3f vec) {
        this(vec.x, vec.y, vec.z, 1f);
    }

    public Color(Vector4f vec) {
        this(vec.x, vec.y, vec.z, vec.w);
    }

    public Color(Color color) {
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

    public Color setA(float a) {
        this.a = a;
        return this;
    }
    public Color setR(float r) {
        this.r = r;
        return this;
    }
    public Color setG(float g) {
        this.g = g;
        return this;
    }
    public Color setB(float b) {
        this.b = b;
        return this;
    }

    public Color getOneToZero() {
        return new Color(this.r / 255f, this.g / 255f, this.b / 255f, this.a);
    }

    public Vector3f getVec3() {
        return new Vector3f(this.r, this.g, this.b);
    }
    public Vector4f getVec4() {
        return new Vector4f(this.r, this.g, this.b, this.a);
    }

    public Color mix(Color color2, float dist) {
        float x = this.r + ((color2.r - this.r) * dist);
        float y = this.g + ((color2.g - this.g) * dist);
        float z = this.b + ((color2.b - this.b) * dist);
        float w = this.a + ((color2.a - this.a) * dist);
        return new Color(x, y, z, w);
    }

    public static Color grayScale(float dist) {
        return new Color(dist, dist, dist);
    }

    public Color clone() {
        return new Color(this);
    }

    public Color set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        return this;
    }

    public Color set(float r, float g, float b) {
        return this.set(r, g, b, this.a);
    }


}
