package net.comsoria.game.terrain.terrainFeature.cave.cave;

import net.comsoria.engine.loaders.Shape;
import net.comsoria.engine.utils.random.Random;
import net.comsoria.engine.utils.Tuple;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.BufferAttribute;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.mesh.Mesh;
import net.comsoria.engine.view.graph.mesh.NoViewMatrixMesh;
import net.comsoria.game.terrain.terrainFeature.TerrainFeature;
import net.comsoria.game.terrain.terrainFeature.cave.cave.generation.CaveTerrainGenerator;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_FRONT;

public class Cave implements TerrainFeature {
    private Mesh mesh;
    private Vector3f position;
    private final float radius;
    private final int size;

    public Cave(int size, Vector3f position, float radius) {
        this.size = size;
        this.position = position;
        this.radius = radius;
    }

    public void loadGameObject(ShaderProgram shaderProgram, CaveTerrainGenerator generator) throws IOException {
        Tuple<List<BufferAttribute>, int[]> parts = Shape.genSphere(size, radius);
//        parts.getA().add(Shape.generateSphereDisplacement(parts.getA().get(0), 0.3f, 3, radius));

        final float xSeed = (float) Math.random() * 89.32f;
        final float ySeed = (float) Math.random() * 56.97f;
        final float zSeed = (float) Math.random() * 98.65f;
        final float inc = 0.1f;
        final float scale = 2;

        generator.updateBuffer(parts.getA().get(0));

        mesh = new NoViewMatrixMesh(new Geometry(parts), new Material(), shaderProgram);
//        mesh.geometry.setCullFace(GL_FRONT);

        mesh.position.set(position);
        mesh.scale = radius * 2;
    }

    @Override public Mesh getGameObject() {
        return mesh;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRadius() {
        return radius;
    }

    public int getSize() {
        return size;
    }
}
