package net.comsoria.engine.view;

import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Fog {
    public float density;
    public float start;

    public Fog(float density, float start) {
        this.density = density;
        this.start = start;
    }
}
