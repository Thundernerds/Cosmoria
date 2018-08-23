package net.comsoria.game.terrain.terrainFeature.surfaceChunk;

import net.comsoria.engine.loaders.GLSLLoader;
import net.comsoria.engine.utils.Grid;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.GLSL.programs.FadeOutShaderProgram;
import net.comsoria.engine.view.batch.BatchRenderType;
import net.comsoria.engine.view.batch.BatchRenderer;
import net.comsoria.game.terrain.terrainFeature.TerrainFeature;
import net.comsoria.game.terrain.terrainFeature.TerrainFeatureLoader;
import net.comsoria.game.terrain.terrainFeature.surfaceChunk.generation.SurfaceChunkTerrainGenerator;
import org.joml.Vector2i;

import java.io.IOException;
import java.util.*;

public class SurfaceChunkLoader implements TerrainFeatureLoader {
    private final SurfaceChunkTerrainGenerator generator;
    private final BatchRenderer batchRenderer = new BatchRenderer(new BatchRenderType());
    private final int chunkSize;

    public SurfaceChunkLoader(SurfaceChunkTerrainGenerator generator, int chunkSize, float skyDomeRad) throws IOException {
        this.generator = generator;

        this.chunkSize = chunkSize;

        Map<String, String> constants = Utils.buildMap(
                "skyDomeRadius", String.valueOf(skyDomeRad),
                "width", String.valueOf(this.chunkSize - 1)
        );

        batchRenderer.batchRenderType.shaderProgram = new FadeOutShaderProgram(
                GLSLLoader.loadGLSL(Utils.utils.p("$shaders/chunk/chunk_vertex.v.glsl"), constants),
                GLSLLoader.loadGLSL(Utils.utils.p("$shaders/chunk/chunk_fragment.f.glsl"), constants)
        );
        batchRenderer.batchRenderType.shaderProgram.init();
    }

    @Override
    public List<TerrainFeature> load(Vector2i chunkPosition, float scale) throws IOException {
        Grid<Float> grid = new Grid<>(this.chunkSize, this.chunkSize);
        generator.updateGrid(grid, chunkPosition);

        SurfaceChunk chunk = new SurfaceChunk(grid, chunkPosition);
        chunk.loadGameObject(scale, this.batchRenderer.batchRenderType.shaderProgram);
        this.batchRenderer.gameObjects.add(chunk.getGameObject());

        return Collections.singletonList(chunk);
    }

    @Override
    public BatchRenderer getBatchRenderer() {
        return batchRenderer;
    }
}
