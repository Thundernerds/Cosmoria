package net.comsoria.engine.view.GLSL;

import net.comsoria.engine.view.Camera;
import net.comsoria.engine.view.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
    public Matrix4f projection = new Matrix4f();
    public Matrix4f ortho = new Matrix4f();

    public Matrix4f view;
    public Matrix4f viewNoTranslation;
    public Matrix4f viewNoRotation;

    private Matrix4f viewData = new Matrix4f();

    public void genProjection(Camera camera, Window window) {
        getProjectionMatrix(camera.fov, window.getWidth(), window.getHeight(), camera.near, camera.far);
    }

    private void getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        projection.identity();
        projection.perspective(fov, aspectRatio, zNear, zFar);
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

    public void genViewMatrices(Camera camera) {
        view = getViewMatrix(camera, true, true);
        viewNoTranslation = getViewMatrix(camera, true, false);
        viewNoRotation = getViewMatrix(camera, false, true);
    }

    public void genOrthoProjectionMatrix(float left, float right, float bottom, float top) {
        ortho.identity();
        ortho.setOrtho2D(left, right, bottom, top);
    }
}
