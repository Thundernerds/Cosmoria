package net.comsoria.engine.view.graph;

import net.comsoria.engine.view.GLSL.Transformation;
import org.joml.Matrix4f;

public class Mesh2D extends Mesh {
    public Mesh2D(Geometry geometry, Material material) {
        super(geometry, material);
    }

    @Override
    public Matrix4f getModelViewMatrix(Transformation transformation) {
        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity().translate(this.position).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(this.scale);

        Matrix4f orthoMatrixCurr = new Matrix4f(transformation.ortho);
        orthoMatrixCurr.mul(modelMatrix);
        return orthoMatrixCurr;
    }
}
