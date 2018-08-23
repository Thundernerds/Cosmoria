package net.comsoria.game.terrain.terrainFeature.surfaceChunk.generation;

import net.comsoria.engine.utils.Grid;
import net.comsoria.engine.utils.SimplexNoise;
import net.comsoria.game.terrain.terrainFeature.Octave;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class SurfaceChunkOctaveGenerator implements SurfaceChunkTerrainGenerator {
    public List<Octave> octaves;
    public float overallHeight = 1;
    public float overallMultiplier = 1;

    public SurfaceChunkOctaveGenerator(List<Octave> octaves) {
        this.octaves = octaves;
    }

    public SurfaceChunkOctaveGenerator(List<Octave> octaves, float seed) {
        for (Octave octave : octaves) {
            octave.seed = seed;
        }
        this.octaves = octaves;
    }

    public SurfaceChunkOctaveGenerator(int octaves, float seed) {
        this.octaves = new ArrayList<>();
        for (int i = 0; i < octaves; i++) {
            Octave octave = new Octave();
            octave.height = (float) (1 / (Math.pow(2, i)));
            octave.multiplier = (float) Math.pow(2, i);
            octave.seed = seed;
            this.octaves.add(octave);
        }
    }

    public SurfaceChunkOctaveGenerator(int octaves, float seed, float overallMultiplier, float overallHeight) {
        this(octaves, seed);
        this.overallHeight = overallHeight;
        this.overallMultiplier = overallMultiplier;
    }

    public float get(float x, float y) {
        float value = 0;
        for (Octave octave : octaves) {
            value += SimplexNoise.noise(x * octave.multiplier, y * octave.multiplier, octave.seed) * octave.height;
        }
        return value;
    }

    @Override
    public void updateGrid(Grid<Float> grid, Vector2i chunkPosition) {
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                grid.set(x, y, this.get(
                        (x + (chunkPosition.x * grid.getWidth()) - chunkPosition.x) * this.overallMultiplier,
                        (y + (chunkPosition.y * grid.getHeight()) - chunkPosition.y) * this.overallMultiplier
                ) * this.overallHeight);
            }
        }
    }
}
