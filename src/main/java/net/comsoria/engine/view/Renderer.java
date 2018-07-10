package net.comsoria.engine.view;

import net.comsoria.Utils;
import net.comsoria.engine.Scene;
import net.comsoria.engine.view.Batch.RenderData;
import net.comsoria.engine.view.GLSL.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private Matrix4f projectionMatrix = new Matrix4f();
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f orthoMatrix = new Matrix4f();

    public List<FrameBuffer> frameBuffers = new ArrayList<>();

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    private final Matrix4f getProjectionMatrix(Camera camera, Window window) {
        return getProjectionMatrix(camera.fov, window.getWidth(), window.getHeight(), camera.near, camera.far);
    }

    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.position;
        Vector3f rotation = camera.rotation;

        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }

    public final Matrix4f getOrthoProjectionMatrix(float left, float right, float bottom, float top) {
        orthoMatrix.identity();
        orthoMatrix.setOrtho2D(left, right, bottom, top);
        return orthoMatrix;
    }

    public void render(Window window, Scene scene) throws Exception {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            for (FrameBuffer frameBuffer : frameBuffers) frameBuffer.setSize(window.getWidth(), window.getHeight());
            window.setResized(false);
            scene.hud.updateSize(window);
        }

        Transformation transformation = new Transformation();
        transformation.projection = getProjectionMatrix(scene.camera, window);
        transformation.view = getViewMatrix(scene.camera);
        transformation.ortho = getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);

        if (frameBuffers.size() != 0) frameBuffers.get(0).bind();

        clear();
        scene.render(transformation);
        if (scene.hud != null) scene.hud.render(transformation);

        for (int i = 1; i < frameBuffers.size(); i++) {
            FrameBuffer frameBuffer = frameBuffers.get(i);
            frameBuffer.bind();

            clear();
            frameBuffers.get(i - 1).render(transformation, null, RenderData.defaultRenderData);
        }

        if (frameBuffers.size() != 0) {
            FrameBuffer.unbind();

            clear();
            frameBuffers.get(frameBuffers.size() - 1).render(transformation, null, RenderData.defaultRenderData);
        }

//        clear();
//        postProcessingFBO.render(transformation, null, RenderData.defaultRenderData);
    }
}
