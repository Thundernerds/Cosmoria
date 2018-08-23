package net.comsoria.game.terrain.terrainFeature;

import net.comsoria.engine.view.batch.BatchRenderer;
import net.comsoria.game.terrain.World;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.io.IOException;
import java.util.List;

public interface TerrainFeatureLoader {
    List<TerrainFeature> load(Vector2i chunkPosition, float scale) throws IOException;
    BatchRenderer getBatchRenderer();
}
