package net.comsoria.engine.utils.random;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Random implements NoiseGenerator {
    public static Random random = new Random();

    public float noise(float x, float y, float seed) {
        return (float) (((Math.abs(Math.sin((x * 172.192) + (y * 827.232)) * seed) % 1) - 0.5) * 2);
    }

    public float noise(Vector2f vector2f, float seed) {
        return noise(vector2f.x, vector2f.y, seed);
    }

    public float noise(Vector2i vector2i, float seed) {
        return noise(vector2i.x, vector2i.y, seed);
    }

    public float noise(float x, float y, float z, float seed) {
        return (float) (((Math.abs(Math.sin((x * 172.192) + (y * 827.232) + (z * 436.876)) * seed) % 1) - 0.5) * 2);
    }

    public float noise(Vector3f vector3f, float seed) {
        return noise(vector3f.x, vector3f.y, vector3f.z, seed);
    }
}
