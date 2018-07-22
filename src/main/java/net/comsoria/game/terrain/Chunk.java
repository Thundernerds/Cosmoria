package net.comsoria.game.terrain;

import net.comsoria.engine.utils.Grid;
import net.comsoria.engine.utils.Tuple;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.loaders.OBJLoader;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.BufferAttribute;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.mesh.Mesh;
import net.comsoria.game.coordinate.ChunkPosition;
import org.joml.Vector2i;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.comsoria.engine.utils.Utils.utils;
import static org.lwjgl.opengl.GL11.GL_BACK;

public class Chunk {
    private static Tuple<List<BufferAttribute>, int[]> vertices;

    private final Grid<Float> grid;
    public final ChunkPosition position;
    private Mesh gameObject;

    public Chunk(Grid<Float> grid, ChunkPosition position) {
        this.grid = grid;
        this.position = position;
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

    public double getHeight(int x, int y) {
        return grid.get(x, y);
    }

    private static Tuple<List<BufferAttribute>, int[]> getVertices() {
        Tuple<List<BufferAttribute>, int[]> result = new Tuple<>();
        result.setA(new ArrayList<>(Arrays.asList(vertices.getA().get(0).clone())));
        result.setB(vertices.getB());
        return result;
    }

    public void loadGameObject(int graphicalSize, int range, ShaderProgram shaderProgram) throws IOException {
        Tuple<List<BufferAttribute>, int[]> data = getVertices();

        Float[] array = grid.getArray(Float.class);
        float[] displacement = new float[grid.getHeight() * grid.getWidth() * 2];

        for (int i = 0; i < array.length; i++) {
            Vector2i vec = grid.getXY(i);
            vec.x = (vec.x + (position.getX() * grid.getWidth()) - position.getX());
            vec.y = (vec.y + (position.getY() * grid.getHeight()) - position.getY());

            displacement[i * 2] = random(vec.x, vec.y, 10) * 0.006f;
            displacement[(i * 2) + 1] = random(vec.x, vec.y, 76) * 0.006f;

            data.getA().get(0).set((i * 3) + 1, (array[i] / graphicalSize) * range);
        }

        data.getA().add(new BufferAttribute(displacement, 2));

        gameObject = new Mesh(new Geometry(data), new Material());

        gameObject.material.ambientColour.set(0, 0, 0, 0);
        gameObject.material.shaderProgram = shaderProgram;
        gameObject.geometry.setCullFace(GL_BACK);

        gameObject.position.set(position.getX() * graphicalSize, 0, position.getY() * graphicalSize);
        gameObject.scale = graphicalSize;
        gameObject.initShaderProgram();
    }

    public Mesh getGameObject() {
        return gameObject;
    }
}
