package net.comsoria.game.terrain.generation;

import net.comsoria.engine.utils.Grid;
import org.joml.Vector2i;

public interface ITerrainGenerator {
    void updateGrid(Grid<Float> grid, Vector2i chunkPosition);
}
