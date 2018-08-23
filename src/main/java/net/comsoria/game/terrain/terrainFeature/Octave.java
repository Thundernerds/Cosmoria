package net.comsoria.game.terrain.terrainFeature;

public class Octave {
    public Float height = null;
    public Float multiplier = null;
    public Float seed = null;

    public Octave() {}

    public Octave(float multiplier, float height) {
        this.height = height;
        this.multiplier = multiplier;
    }

    public Octave(float multiplier, float height, float seed) {
        this.height = height;
        this.multiplier = multiplier;
        this.seed = seed;
    }
}
