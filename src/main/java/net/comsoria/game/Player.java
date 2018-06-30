package net.comsoria.game;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Player {
    private Vector3f position;

    private float speed = 10;
    public float sprintMultiplier = 10;

    public Player(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector2f get2DPosition() {
        return new Vector2f(position.x, position.z);
    }

    public float getSpeed(boolean sprint) {
        return speed * (sprint? sprintMultiplier:1);
    }
}
