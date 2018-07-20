package net.comsoria.game.terrain.generation;

import net.comsoria.engine.utils.Grid;
import net.comsoria.game.coordinate.ChunkPosition;

public class Perlin2Generator implements ITerrainGenerator {
    private final double multiplier1;
    private final double multiplier2;
    private float multiplier2AddRange;

    public Perlin2Generator(double multiplier1, double multiplier2, double multiplier2AddRange) {
        this.multiplier1 = multiplier1;
        this.multiplier2 = multiplier2;
        this.multiplier2AddRange = (float) multiplier2AddRange;
    }

    @Override
    public void updateGrid(Grid<Float> grid, ChunkPosition chunkPosition) {
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                float val1 = (float) SimplexNoise.noise(
                        (x + (chunkPosition.getX() * grid.getWidth()) - chunkPosition.getX()) * this.multiplier1,
                        (y + (chunkPosition.getY() * grid.getHeight()) - chunkPosition.getY()) * this.multiplier1
                );

                float val2 = (float) SimplexNoise.noise(
                        (x + (chunkPosition.getX() * grid.getWidth()) - chunkPosition.getX()) * this.multiplier2,
                        (y + (chunkPosition.getY() * grid.getHeight()) - chunkPosition.getY()) * this.multiplier2
                ) * multiplier2AddRange;

                grid.set(x, y, val1 + val2);
            }
        }
    }
}
