package net.comsoria.controller;

import net.comsoria.engine.Hud;
import net.comsoria.engine.loaders.text.FontTexture;
import net.comsoria.engine.loaders.text.TextLoader;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.loaders.OBJLoader;
import net.comsoria.engine.view.color.Color4;
import net.comsoria.engine.view.GLSL.Programs.ShaderProgram2D;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.mesh.Mesh;
import net.comsoria.engine.view.graph.mesh.Mesh2D;

import java.awt.*;

public class GameHud extends Hud {
    private Mesh compass;
    public Mesh label;

    void init() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram2D();

        compass = new Mesh2D(new Geometry(OBJLoader.loadGeometry(Utils.utils.p("$models/compass.obj"))), new Material());
        compass.material.ambientColour = Color4.RED.clone();
        compass.material.shaderProgram = shaderProgram;
        compass.initShaderProgram();

//        Mesh crosshair = new Mesh2D(new Geometry(OBJLoader.loadGeometry("")), new Material());
//        crosshair.material.shaderProgram = shaderProgram.clone();
//        crosshair.material.shaderProgram.createTextureUniform("crosshair");
//        crosshair.material.textures.add(new Texture("$textures/VeryVeryVeryBadCrosshair"));
//        crosshair.initShaderProgram();

        label = TextLoader.buildMesh("Hello World!", new FontTexture(new Font("Arial", Font.PLAIN, 40), "ISO-8859-1"));
        label.scale = 1.5f;

        gameObjects.add(label);


        compass.scale = 40.0f;
        gameObjects.add(compass);

        rotateCompass(0);
    }

    @Override
    public void updateSize(Window window) {
        float size = Math.min(window.getWidth(), window.getHeight()) * 0.1f;
        compass.position.set(window.getWidth() - size, size + 15, 0);
        compass.scale = size;

//        label.position.set(10f, window.getHeight() - 50f, 0);
    }

    void rotateCompass(float rotation) {
        compass.rotation.set(0, 0, rotation + 180);
    }
}
