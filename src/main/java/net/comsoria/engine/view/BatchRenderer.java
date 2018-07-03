package net.comsoria.engine.view;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BatchRenderer implements Renderable {
    @Override
    public Closeable render(Window window, Transformation transformation, Scene scene) throws Exception {
        return null;
    }

    @Override
    public void cleanup() {

    }

    @Override
    public boolean shouldRender() {
        return false;
    }
//    public List<GameObject> gameObjects = new ArrayList<>();
//
//    public Material material = null;
//    public Geometry geometry = null;
//    public ShaderProgram shaderProgram = null;
//
//    @Override
//    public void render(Window window) throws Exception {
////        for (GameObject gameObject)
//    }
}
