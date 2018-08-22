package net.comsoria.engine.view.graph.mesh;

import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import org.joml.Matrix4f;

public class NoViewMatrixMesh extends Mesh {
    public NoViewMatrixMesh(Geometry geometry, Material material, ShaderProgram shaderProgram) {
        super(geometry, material, shaderProgram);
    }

    @Override
    public Matrix4f getModelViewMatrix(Transformation transformation) {
        modelViewMatrix.identity().translate(this.position).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(this.scale);

        return modelViewMatrix;

        //Note that what is return is ->NOT<- a model view matrix but is just a model matrix instead
    }

    public NoViewMatrixMesh clone() {
        return (NoViewMatrixMesh) super.clone();
    }
}
