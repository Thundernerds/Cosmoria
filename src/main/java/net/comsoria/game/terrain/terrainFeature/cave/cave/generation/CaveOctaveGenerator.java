package net.comsoria.game.terrain.terrainFeature.cave.cave.generation;

import net.comsoria.engine.utils.SimplexNoise;
import net.comsoria.engine.view.graph.BufferAttribute;
import net.comsoria.game.terrain.terrainFeature.Octave;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class CaveOctaveGenerator implements CaveTerrainGenerator {
    public List<Octave> octaves;
    public float overallHeight = 1;
    public float overallMultiplier = 1;

    public CaveOctaveGenerator(List<Octave> octaves) {
        this.octaves = octaves;
    }

    public CaveOctaveGenerator(List<Octave> octaves, float seed) {
        for (Octave octave : octaves) {
            octave.seed = seed;
        }
        this.octaves = octaves;
    }

    public CaveOctaveGenerator(int octaves, float seed) {
        this.octaves = new ArrayList<>();
        for (int i = 0; i < octaves; i++) {
            Octave octave = new Octave();
            octave.height = (float) (1 / (Math.pow(2, i)));
            octave.multiplier = (float) Math.pow(2, i);
            octave.seed = seed;
            this.octaves.add(octave);
        }
    }

    public CaveOctaveGenerator(int octaves, float seed, float overallMultiplier, float overallHeight) {
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


    public void updateBuffer(BufferAttribute positions) {
        for (Octave octave : octaves) {
            for (int i = 0; i < positions.parts(); i++) {
                Vector3f seed = positions.getVec3(i).mul(octave.multiplier);

                positions.add(i * 3,
                        (float) SimplexNoise.noise(seed.x, seed.y, seed.z, octave.seed * 45.23f) * octave.height
                );
                positions.add((i * 3) + 1,
                        (float) SimplexNoise.noise(seed.x, seed.y, seed.z, octave.seed * 23.21f) * octave.height
                );
                positions.add((i * 3) + 2,
                        (float) SimplexNoise.noise(seed.x, seed.y, seed.z, octave.seed * 78.67f) * octave.height
                );
            }
        }
    }
}
