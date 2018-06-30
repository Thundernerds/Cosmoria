package net.comsoria.engine.view;

import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Fog {
    public float density;
    public Vector3f color;
    public float start;

    public Fog(float density, float start, Vector3f color) {
        this.density = density;
        this.color = color;
        this.start = start;
    }
}
