package net.comsoria.engine.view;

import net.comsoria.Utils;
import net.comsoria.engine.IHud;
import net.comsoria.engine.Scene;
import net.comsoria.engine.view.GLSL.Programs.ShaderProgram3D;
import net.comsoria.engine.view.GLSL.Programs.ShaderProgram2D;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.graph.Mesh;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class Renderer {
    private final Transformation transformation = new Transformation();
    private FrameBuffer postProcessingFBO = null;

    public void init(Window window) throws Exception {
        postProcessingFBO = new FrameBuffer(window.getWidth(), window.getHeight(),
                Utils.loadResourceAsString("$pp_vertex"), Utils.loadResourceAsString("$pp_fragment"));
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, Scene scene) throws Exception {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            postProcessingFBO.setSize(window.getWidth(), window.getHeight());
            window.setResized(false);
            scene.hud.updateSize(window);
        }

        postProcessingFBO.bind();
        clear();
        renderScene(window, camera, scene);
        if (scene.hud != null) renderHud(window, scene.hud);

        FrameBuffer.unbind();

        clear();
        postProcessingFBO.render();
    }

    private void renderScene(Window window, Camera camera, Scene scene) throws Exception {
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(camera.fov, window.getWidth(), window.getHeight(), camera.near, camera.far);
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        List<ShaderProgram> toClose = new ArrayList<>();

        for (GameObject gameItem : scene.children) {
            if (!gameItem.visible) continue;

            Mesh mesh = gameItem.getMesh();
            if (mesh.needsInitialising()) mesh.init(ShaderProgram3D.class);

            ShaderProgram shaderProgram = mesh.material.shaderProgram;

            mesh.bind();

            if (!shaderProgram.isUpdated()) {
                shaderProgram.open();
                shaderProgram.setupScene(scene, projectionMatrix, viewMatrix);
                toClose.add(shaderProgram);
            }

            shaderProgram.setupMesh(mesh, transformation.getModelViewMatrix(gameItem, viewMatrix));
            mesh.render();

            mesh.unbind();
        }

        for (ShaderProgram shaderProgram : toClose) {
            shaderProgram.close();
        }
    }

    private void renderHud(Window window, IHud hud) throws Exception {
        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
        for (GameObject gameItem : hud.getGameItems()) {
            if (!gameItem.visible) continue;

            Mesh mesh = gameItem.getMesh();
            if (mesh.needsInitialising()) mesh.init(ShaderProgram2D.class);
            mesh.bind();

            Matrix4f projModelMatrix = transformation.getOrthoProjModelMatrix(gameItem, ortho);
            mesh.material.shaderProgram.setupMesh(mesh, projModelMatrix);

            mesh.render();
            mesh.unbind();
        }
    }
}
