package net.comsoria.controller;

import net.comsoria.engine.IHud;
import net.comsoria.engine.loaders.OBJLoader;
import net.comsoria.engine.view.GLSL.Programs.ShaderProgram2D;
import net.comsoria.engine.view.GameObject;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.Mesh;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Hud implements IHud {
    private List<GameObject> gameObjects = new ArrayList<>();
    private GameObject compass;

    @Override
    public List<GameObject> getGameItems() {
        return gameObjects;
    }

    void init() throws Exception {
        Mesh mesh = new Mesh(new Geometry(OBJLoader.loadGeometry("$compassobj")), new Material());
        mesh.material.ambientColour = new Vector4f(1, 0, 0, 1f);

        compass = new GameObject(mesh);
        compass.scale = 40.0f;
        gameObjects.add(compass);

        rotateCompass(0);
    }

    @Override
    public void updateSize(Window window) {
        float size = Math.min(window.getWidth(), window.getHeight()) * 0.1f;
        compass.position.set(window.getWidth() - size, size + 15, 0);
        compass.scale = size;
    }

    void rotateCompass(float rotation) {
        compass.rotation.set(0, 0, rotation + 180);
    }
}
