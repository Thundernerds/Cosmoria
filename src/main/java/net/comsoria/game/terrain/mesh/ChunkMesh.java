package net.comsoria.game.terrain.mesh;

import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Matrix4f;

public class ChunkMesh extends Mesh {
    public ChunkMesh(Geometry geometry, Material material) {
        super(geometry, material);
    }

    @Override
    public Matrix4f getModelViewMatrix(Transformation transformation) {
        modelViewMatrix.identity().translate(this.position).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(this.scale);

        return modelViewMatrix;
    }
}
