package net.comsoria.engine.utils.random;

public interface NoiseGenerator {
    float noise(float x, float y, float seed);
    float noise(float x, float y, float z, float seed);
}
