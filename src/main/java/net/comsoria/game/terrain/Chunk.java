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
        result.setA(Arrays.asList(vertices.getA().get(0)));
        result.setB(vertices.getB());
        return result;
    }

    public void loadGameObject(int graphicalSize, int range, ShaderProgram shaderProgram) throws IOException {
        Tuple<List<BufferAttribute>, int[]> data = getVertices();

        Float[] array = grid.getArray(Float.class);
        for (int i = 0; i < array.length; i++) {
            data.getA().get(0).set((i * 3) + 1, (array[i] / graphicalSize) * range);
        }

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
