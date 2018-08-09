package net.comsoria.game.terrain.terrainFeature;

import net.comsoria.engine.view.batch.BatchRenderer;
import net.comsoria.game.terrain.World;
import org.joml.Vector2f;

public interface TerrainFeatureLoader {
    void updateAroundPlayer(Vector2f position, World world, int radius) throws Exception;
    BatchRenderer getBatchRenderer();
}
