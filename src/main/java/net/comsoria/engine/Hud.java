package net.comsoria.engine;

import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.GameObject;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.graph.Mesh;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class Hud implements Renderable {
    public List<GameObject> gameObjects = new ArrayList<>();
    private Transformation transformation = new Transformation();

    public void cleanup() {
        for (GameObject gameObject : gameObjects) {
            gameObject.getMesh().cleanup();
        }
    }

    public void updateSize(Window window) {

    }

    @Override
    public void render(Window window) throws Exception {
        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
        for (GameObject gameItem : this.gameObjects) {
            if (!gameItem.visible) continue;

            Mesh mesh = gameItem.getMesh();

            mesh.geometry.bind();
            mesh.material.shaderProgram.bind();

            Matrix4f projModelMatrix = transformation.getOrthoProjModelMatrix(gameItem, ortho);
            mesh.material.shaderProgram.setupMesh(mesh, projModelMatrix);

            mesh.render(window);

            mesh.geometry.unbind();
            mesh.material.shaderProgram.unbind();
        }
    }
}
