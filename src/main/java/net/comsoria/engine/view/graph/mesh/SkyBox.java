package net.comsoria.engine.view.graph.mesh;

import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_FRONT;

public class SkyBox extends Mesh {
    public SkyBox(Geometry geometry, Material material) {
        super(geometry, material);
        this.renderPosition = RenderOrder.First;
        this.geometry.setCullFace(GL_FRONT);
    }

    @Override
    public Matrix4f getModelViewMatrix(Transformation transformation) {
        modelViewMatrix.identity().translate(this.position).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(this.scale);

        Matrix4f viewCurr = new Matrix4f(transformation.viewNoTranslation);
        return viewCurr.mul(modelViewMatrix);
    }
}
