package net.comsoria.engine.math;

public class Plane {
    public float a, b, c, d;

    public Plane() {
        this(0, 0, 0, 0);
    }

    public Plane(float a, float b, float c, float d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
}
