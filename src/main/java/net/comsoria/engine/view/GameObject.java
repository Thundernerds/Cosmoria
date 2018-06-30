package net.comsoria.engine.view;

import net.comsoria.engine.view.graph.Mesh;
import org.joml.Vector3f;

public class GameObject {
    private Mesh mesh;
    public final Vector3f position;
    public final Vector3f rotation;
    public float scale;
    public boolean visible;

    public GameObject(Mesh mesh) {
        this.mesh = mesh;

        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.scale = 1;
        this.visible = true;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
