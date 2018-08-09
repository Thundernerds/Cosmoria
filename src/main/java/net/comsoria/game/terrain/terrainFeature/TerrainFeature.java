package net.comsoria.game.terrain.terrainFeature;

import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Vector2f;

import java.io.IOException;

public interface TerrainFeature {
    void loadGameObject(int graphicalSize, int range, ShaderProgram shaderProgram) throws IOException;
    Mesh getGameObject();
    Vector2f getPosition();
}
