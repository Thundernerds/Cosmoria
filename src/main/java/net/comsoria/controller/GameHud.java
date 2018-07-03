package net.comsoria.controller;

import net.comsoria.engine.Hud;
import net.comsoria.engine.loaders.OBJLoader;
import net.comsoria.engine.view.GLSL.Programs.ShaderProgram2D;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.Mesh;
import net.comsoria.engine.view.graph.Mesh2D;
import org.joml.Vector4f;

public class GameHud extends Hud {
    private Mesh compass;

    void init() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram2D();

        compass = new Mesh2D(new Geometry(OBJLoader.loadGeometry("$compassobj")), new Material());
        compass.material.ambientColour = new Vector4f(1, 0, 0, 1f);
        compass.material.shaderProgram = shaderProgram;
        compass.initShaderProgram();

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
