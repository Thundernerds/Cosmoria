package net.comsoria.game.terrain.terrainFeature;

import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Vector2f;

import java.io.IOException;

public interface TerrainFeature {
    Mesh getGameObject();
}
