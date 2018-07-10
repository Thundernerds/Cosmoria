package net.comsoria.engine.view.Light;

import net.comsoria.engine.Color;
import org.joml.Vector3f;

public class DirectionalLight {
    public Color color;
    public Vector3f direction;
    public float intensity;

    public DirectionalLight(Color color, Vector3f direction, float intensity) {
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
    }

    public DirectionalLight(DirectionalLight light) {
        this(light.color.clone(), new Vector3f(light.direction), light.intensity);
    }
}
