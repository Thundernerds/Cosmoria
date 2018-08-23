package net.comsoria.game.terrain.terrainFeature.surfaceChunk.generation;

import net.comsoria.engine.utils.Grid;
import org.joml.Vector2i;

public interface SurfaceChunkTerrainGenerator {
    void updateGrid(Grid<Float> grid, Vector2i chunkPosition);
}
