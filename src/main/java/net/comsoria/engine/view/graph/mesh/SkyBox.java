package net.comsoria.engine.view.graph.mesh;

import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import org.joml.Matrix4f;

public class SkyBox extends Mesh {
    public SkyBox(Geometry geometry, Material material) {
        super(geometry, material);
    }

    @Override
    public Matrix4f getModelViewMatrix(Transformation transformation) {
        modelViewMatrix.identity().translate(this.position).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(this.scale);

        Matrix4f viewCurr = new Matrix4f(transformation.view);
        viewCurr.m30(0);
        viewCurr.m31(0);
        viewCurr.m32(0);
        return viewCurr.mul(modelViewMatrix);
    }
}
