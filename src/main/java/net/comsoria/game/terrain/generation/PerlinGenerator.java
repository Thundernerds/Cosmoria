package net.comsoria.game.terrain.generation;

import net.comsoria.engine.utils.Grid;
import net.comsoria.game.coordinate.ChunkPosition;

public class PerlinGenerator implements ITerrainGenerator {
    private final double multiplier;
    private final float seed;

    public PerlinGenerator(double multiplier, float seed) {
        this.multiplier = multiplier;
        this.seed = seed;
    }

    @Override
    public void updateGrid(Grid<Float> grid, ChunkPosition chunkPosition) {
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                float val = (float) SimplexNoise.noise(
                        (x + (chunkPosition.getX() * grid.getWidth()) - chunkPosition.getX()) * this.multiplier,
                        (y + (chunkPosition.getY() * grid.getHeight()) - chunkPosition.getY()) * this.multiplier,
                        seed
                );

                grid.set(x, y, val);
            }
        }
    }
}
