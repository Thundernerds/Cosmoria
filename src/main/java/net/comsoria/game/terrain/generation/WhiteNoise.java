package net.comsoria.game.terrain.generation;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class WhiteNoise {
    private static Vector3f randomisationVector3D = new Vector3f(12.9898f, 78.233f, 78.2723f);
    private static Vector2f randomisationVector2D = new Vector2f(82.82737f, 289.2313f);

    public static float whiteNoise(float x, float seed) {
        return (float) ((Math.sin(x * 18723.73643) * seed) % 1.0);
    }

    public static float whiteNoise(Vector2f vector, float seed) {
        return (float) ((Math.sin(vector.dot(randomisationVector2D)) * seed) % 1.0);
    }

    public static float whiteNoise(Vector3f vector, float seed) {
        return (float) ((Math.sin(vector.dot(randomisationVector3D)) * seed) % 1.0);
    }
}
