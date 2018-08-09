package net.comsoria.game.terrain.terrainFeature.surfaceChunk;

import net.comsoria.engine.utils.Grid;
import net.comsoria.engine.utils.Tuple;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.loaders.OBJLoader;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.BufferAttribute;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.mesh.Mesh;
import net.comsoria.game.terrain.mesh.ChunkMesh;
import net.comsoria.game.terrain.terrainFeature.TerrainFeature;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_BACK;

public class SurfaceChunk implements TerrainFeature {
    private static Tuple<List<BufferAttribute>, int[]> vertices;

    private final Grid<Float> grid;
    private final Vector2i chunkPosition;
    private Vector2f position;
    private Mesh gameObject;

    public SurfaceChunk(Grid<Float> grid, Vector2i position) {
        this.grid = grid;
        this.chunkPosition = position;
        this.position = new Vector2f(position);
    }

    public float random(int x, int y, float seed) {
        return (float) (((Math.abs(Math.sin((x * 172.192) + (y * 827.232)) * seed) % 1) - 0.5) * 2);
    }

    static {
        try {
            vertices = OBJLoader.loadGeometry(Utils.utils.p("$models/chunk_plane.obj"));
            vertices.getA().remove(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
    }

    private static Tuple<List<BufferAttribute>, int[]> getVertices() {
        Tuple<List<BufferAttribute>, int[]> result = new Tuple<>();
        result.setA(new ArrayList<>(Arrays.asList(vertices.getA().get(0).clone())));
        result.setB(vertices.getB());
        return result;
    }

    @Override
    public void loadGameObject(int graphicalSize, int range, ShaderProgram shaderProgram) throws IOException {
        this.position = this.position.mul(graphicalSize);

        Tuple<List<BufferAttribute>, int[]> data = getVertices();

        Float[] array = grid.getArray(Float.class);
        float[] displacement = new float[grid.getHeight() * grid.getWidth() * 2];

        Vector2i relative = new Vector2i((chunkPosition.x * grid.getWidth()) - chunkPosition.x, (chunkPosition.y * grid.getHeight()) - chunkPosition.y);

        for (int i = 0; i < array.length; i++) {
            Vector2i vec = grid.getXY(i).add(relative);

            displacement[i * 2] = random(vec.x, vec.y, 10) * 0.005f;
            displacement[(i * 2) + 1] = random(vec.x, vec.y, 76) * 0.005f;

            data.getA().get(0).set((i * 3) + 1, (array[i] / graphicalSize) * range);
        }

        data.getA().add(new BufferAttribute(displacement, 2));

        gameObject = new ChunkMesh(new Geometry(data), new Material());

        gameObject.material.ambientColour.set(0, 0, 0, 0);
        gameObject.material.shaderProgram = shaderProgram;
        gameObject.geometry.setCullFace(GL_BACK);

        gameObject.position.set(position.x, 0, position.y);
        gameObject.scale = graphicalSize;
    }

    @Override
    public Mesh getGameObject() {
        return gameObject;
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }
}
