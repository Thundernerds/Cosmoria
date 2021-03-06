package net.comsoria.engine.view.graph.mesh;

import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_FRONT;

public class SkyBox extends Mesh {
    public SkyBox(Geometry geometry, Material material, ShaderProgram shaderProgram) {
        super(geometry, material, shaderProgram);
        this.renderPosition = RenderOrder.End;
        this.geometry.setCullFace(GL_FRONT);
    }

    @Override
    public Matrix4f getModelViewMatrix(Transformation transformation) {
        modelViewMatrix.identity().translate(this.position).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(this.scale);

        Matrix4f viewCurr = new Matrix4f(transformation.getViewNoTranslation());
        return viewCurr.mul(modelViewMatrix);
    }

    public SkyBox clone() {
        return (SkyBox) super.clone();
    }
}
