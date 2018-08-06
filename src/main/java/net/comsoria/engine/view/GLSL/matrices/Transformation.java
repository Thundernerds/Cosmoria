package net.comsoria.engine.view.GLSL.matrices;

import net.comsoria.engine.view.Camera;
import net.comsoria.engine.view.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class Transformation {
    private Matrix4f projection = new Matrix4f();
    private Matrix4f ortho = new Matrix4f();

    private Matrix4f view = new Matrix4f();
    private Matrix4f viewNoTranslation = new Matrix4f();
    private Matrix4f viewNoRotation = new Matrix4f();

    private Matrix4f viewData = new Matrix4f();

    public Matrix4f getProjectionMatrix(Window window, Camera camera) {
        float aspectRatio = window.getWidth() / window.getHeight();

        projection = new Matrix4f();
        projection.perspective(camera.fov, aspectRatio, camera.near, camera.far);

        return projection;
    }

    public Matrix4f getProjection() {
        return projection;
    }

    private Matrix4f getViewMatrix(Camera camera, boolean rot, boolean trans) {
        Vector3f cameraPos = camera.position;
        Vector3f rotation = camera.rotation;

        viewData.identity();
        if (rot) viewData.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));

        if (trans) viewData.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        return new Matrix4f(viewData);
    }

    public Matrix4f getView(Camera camera) {
        view = getViewMatrix(camera, true, true);
        return view;
    }

    public Matrix4f getView() {
        return view;
    }

    public Matrix4f getViewNoRotation(Camera camera) {
        viewNoRotation = getViewMatrix(camera, false, true);
        return viewNoRotation;
    }

    public Matrix4f getViewNoRotation() {
        return viewNoRotation;
    }

    public Matrix4f getViewNoTranslation(Camera camera) {
        viewNoTranslation = getViewMatrix(camera, true, false);
        return viewNoTranslation;
    }

    public Matrix4f getViewNoTranslation() {
        return viewNoTranslation;
    }

    public Matrix4f getOrthoProjectionMatrix(float left, float right, float bottom, float top) {
        ortho.identity();
        ortho.setOrtho2D(left, right, bottom, top);

        return ortho;
    }

    public Matrix4f getOrtho() {
        return ortho;
    }

    public static Matrix4f updateGenericViewMatrix(Vector3f position, Vector3f rotation, Matrix4f matrix) {
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float)Math.toRadians(rotation.x))
                .rotateY((float)Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }
}
