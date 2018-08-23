package net.comsoria.game.terrain.terrainFeature.cave.cave;

import net.comsoria.engine.loaders.GLSLLoader;
import net.comsoria.engine.utils.random.NoiseGenerator;
import net.comsoria.engine.utils.random.Random;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.GLSL.programs.FadeOutShaderProgram;
import net.comsoria.engine.view.batch.BatchRenderType;
import net.comsoria.engine.view.batch.BatchRenderer;
import net.comsoria.game.terrain.terrainFeature.TerrainFeature;
import net.comsoria.game.terrain.terrainFeature.TerrainFeatureLoader;
import net.comsoria.game.terrain.terrainFeature.cave.cave.generation.CaveTerrainGenerator;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CaveLoader implements TerrainFeatureLoader {
    private final float positionSeed;
    private final float sizeSeed;
    private final float iterationSeed;
    private final int maxPerChunk;
    private final CaveTerrainGenerator generator;
    private final int maxSize;
    private final float maxRealSize;
    private BatchRenderer batchRenderer = new BatchRenderer(new BatchRenderType());

    public CaveLoader(float skyDomeR, float positionSeed, float sizeSeed, float iterationSeed, int maxPerChunk, CaveTerrainGenerator generator, int maxSize, float maxRealSize) throws IOException {
        this.positionSeed = positionSeed;
        this.sizeSeed = sizeSeed;
        this.iterationSeed = iterationSeed;
        this.maxPerChunk = maxPerChunk;
        this.generator = generator;
        this.maxSize = maxSize;
        this.maxRealSize = maxRealSize;

        Map<String, String> constants = Utils.buildMap("skyDomeRadius", String.valueOf(skyDomeR), "width", String.valueOf(2));
        batchRenderer.batchRenderType.shaderProgram = new FadeOutShaderProgram(
                GLSLLoader.loadGLSL(Utils.utils.p("$shaders/underground/chunk_vertex.v.glsl"), constants),
                GLSLLoader.loadGLSL(Utils.utils.p("$shaders/underground/chunk_fragment.f.glsl"), constants)
        );

        batchRenderer.batchRenderType.shaderProgram.init();
    }

    @Override
    public List<TerrainFeature> load(Vector2i chunkPosition, float scale) throws IOException {
        List<TerrainFeature> result = new ArrayList<>();

        float x = chunkPosition.x + 78.23f;
        float y = chunkPosition.y + 12.56f;

        int iterations = (int) (((Random.random.noise(x, y, 87.92f * iterationSeed) + 1) * 0.5) * maxPerChunk);

        for (int i = 0; i < iterations; i++) {
            float radius = (Random.random.noise(x, y,  54.27f * sizeSeed * (i + 34.23f)) + 1) * 0.5f;

            Cave cave = new Cave(
                    2 * (Math.round((int) (radius * maxSize) / 2)),
                    new Vector3f(
                            (Random.random.noise((float) i + 83.54f, x, y, 34.73f * positionSeed) + chunkPosition.x) * scale,
                            ((Random.random.noise((float) i + 73.23f, x, y, 62.93f * positionSeed) - 1) * 0.5f) * scale,
                            (Random.random.noise((float) i + 92.34f, x, y, 12.34f * positionSeed) + chunkPosition.y) * scale
                    ),
                    radius * maxRealSize * scale

            );
            cave.loadGameObject(batchRenderer.batchRenderType.shaderProgram, generator);
            batchRenderer.gameObjects.add(cave.getGameObject());
            result.add(cave);
        }

        return result;
    }

    @Override
    public BatchRenderer getBatchRenderer() {
        return batchRenderer;
    }
}
