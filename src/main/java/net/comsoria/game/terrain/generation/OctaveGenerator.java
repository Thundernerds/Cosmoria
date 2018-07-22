package net.comsoria.game.terrain.generation;

import net.comsoria.engine.utils.Grid;
import net.comsoria.game.coordinate.ChunkPosition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OctaveGenerator implements ITerrainGenerator {
    public List<Octave> octaves;
    public float overallHeight = 1;
    public float overallMultiplier = 1;

    public OctaveGenerator(List<Octave> octaves) {
        this.octaves = octaves;
    }

    public OctaveGenerator(List<Octave> octaves, float seed) {
        for (Octave octave : octaves) {
            octave.seed = seed;
        }
        this.octaves = octaves;
    }

    public OctaveGenerator(int octaves, float seed) {
        this.octaves = new ArrayList<>();
        for (int i = 0; i < octaves; i++) {
            Octave octave = new Octave();
            octave.height = (float) (1 / (Math.pow(2, i)));
            octave.multiplier = (float) Math.pow(2, i);
            octave.seed = seed;
            this.octaves.add(octave);
        }
    }

    public OctaveGenerator(int octaves, float seed, float overallMultiplier, float overallHeight) {
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
    public void updateGrid(Grid<Float> grid, ChunkPosition chunkPosition) {
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                grid.set(x, y, this.get(
                        (x + (chunkPosition.getX() * grid.getWidth()) - chunkPosition.getX()) * this.overallMultiplier,
                        (y + (chunkPosition.getY() * grid.getHeight()) - chunkPosition.getY()) * this.overallMultiplier
                ) * this.overallHeight);
            }
        }
    }

    public void writeToImage(String path) throws IOException {
        int WIDTH = 100, HEIGHT = 100;
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                double value = this.get(x, y);
                System.out.println(value);
                int rgb = 0x010101 * (int)((value + 1) * 127.5);
                image.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(image, "png", new File(path));
    }

    public static class Octave {
        public Float height = null;
        public Float multiplier = null;
        public Float seed = null;

        public Octave() {}
        public Octave(float multiplier, float height) {
            this.height = height;
            this.multiplier = multiplier;
        }
    }
}
