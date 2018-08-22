package net.comsoria.engine.utils;

import org.joml.Vector3f;

public class Random {
    public static float noise(float x, float y, float seed) {
        return (float) (((Math.abs(Math.sin((x * 172.192) + (y * 827.232)) * seed) % 1) - 0.5) * 2);
    }

    public static float noise(float x, float y, float z, float seed) {
        return (float) (((Math.abs(Math.sin((x * 172.192) + (y * 827.232) + (z * 436.876)) * seed) % 1) - 0.5) * 2);
    }

    public static float noise(Vector3f vector3f, float seed) {
        return noise(vector3f.x, vector3f.y, vector3f.z, seed);
    }
}
