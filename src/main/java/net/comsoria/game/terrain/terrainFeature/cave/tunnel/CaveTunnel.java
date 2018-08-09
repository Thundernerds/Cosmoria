package net.comsoria.game.terrain.terrainFeature.cave.tunnel;

import net.comsoria.engine.loaders.Shape;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.mesh.Mesh;
import net.comsoria.game.terrain.terrainFeature.TerrainFeature;
import org.joml.Vector2f;

import java.io.IOException;

public class CaveTunnel implements TerrainFeature {
    private Vector2f position;
    private Mesh mesh;

    public CaveTunnel(Vector2f position) {
        this.position = position;
    }

    @Override
    public void loadGameObject(int graphicalSize, int range, ShaderProgram shaderProgram) throws IOException {
        this.mesh = new Mesh(new Geometry(Shape.genCylinder(10, 20)), new Material());
        this.mesh.material.shaderProgram = shaderProgram;
    }

    @Override
    public Mesh getGameObject() {
        return null;
    }

    @Override
    public Vector2f getPosition() {
        return null;
    }
}
