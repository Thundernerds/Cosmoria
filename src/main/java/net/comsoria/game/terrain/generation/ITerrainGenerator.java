package net.comsoria.game.terrain.generation;

import net.comsoria.engine.Grid;
import net.comsoria.game.coordinate.ChunkPosition;

public interface ITerrainGenerator {
    void updateGrid(Grid<Float> grid, ChunkPosition chunkPosition);
}
