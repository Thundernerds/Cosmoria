package net.comsoria.engine.view;

import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BatchRenderer implements Renderable {
    public List<GameObject> gameObjects = new ArrayList<>();

    public Material material = null;
    public Geometry geometry = null;
    public ShaderProgram shaderProgram = null;

    @Override
    public void render(Window window) throws Exception {
//        for (GameObject gameObject)
    }
}
