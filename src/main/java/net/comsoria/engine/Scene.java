package net.comsoria.engine;

import net.comsoria.engine.view.*;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.Light.SceneLight;
import net.comsoria.engine.view.graph.Mesh;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class Scene implements Renderable {
    public final SceneLight light;
    private final List<GameObject> children;
    public Hud hud;
    public Fog fog;
    private Transformation transformation;
    public Camera camera;

    public Scene(Hud hud) {
        this.light = new SceneLight();
        this.children = new ArrayList<>();
        this.hud = hud;
        this.transformation = new Transformation();
        this.camera = new Camera();
    }

    public void cleanup() {
        for (GameObject gameItem : this.children) {
            gameItem.getMesh().cleanup();
        }
        hud.cleanup();
    }

    @Override
    public void render(Window window) throws Exception {
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(camera.fov, window.getWidth(), window.getHeight(), camera.near, camera.far);
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        List<ShaderProgram> toClose = new ArrayList<>();

        for (GameObject gameItem : this.children) {
            if (!gameItem.visible) continue;

            Mesh mesh = gameItem.getMesh();

            ShaderProgram shaderProgram = mesh.material.shaderProgram;

            mesh.geometry.bind();
            mesh.material.shaderProgram.bind();

            if (!shaderProgram.isUpdated()) {
                shaderProgram.open();
                shaderProgram.setupScene(this, projectionMatrix, viewMatrix);
                toClose.add(shaderProgram);
            }

            shaderProgram.setupMesh(mesh, transformation.getModelViewMatrix(gameItem, viewMatrix));
            mesh.render(window);

            mesh.geometry.unbind();
            mesh.material.shaderProgram.unbind();
        }

        for (ShaderProgram shaderProgram : toClose) {
            shaderProgram.close();
        }
    }

    public void add(GameObject gameObject) throws Exception {
        gameObject.getMesh().initShaderProgram();
        this.children.add(gameObject);
    }
}
