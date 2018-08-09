package net.comsoria.game.terrain.terrainFeature.surfaceChunk;

import net.comsoria.engine.loaders.GLSLLoader;
import net.comsoria.engine.utils.Grid;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.GLSL.Programs.FadeOutShaderProgram;
import net.comsoria.engine.view.batch.BatchRenderType;
import net.comsoria.engine.view.batch.BatchRenderer;
import net.comsoria.game.terrain.World;
import net.comsoria.game.terrain.generation.ITerrainGenerator;
import net.comsoria.game.terrain.terrainFeature.TerrainFeature;
import net.comsoria.game.terrain.terrainFeature.TerrainFeatureLoader;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SurfaceChunkLoader implements TerrainFeatureLoader {
    private final ITerrainGenerator generator;

    private final int chunkSize;
    private final int graphicalSize;
    private final int range;

    private final BatchRenderer batchRenderer = new BatchRenderer(new BatchRenderType());

    public SurfaceChunkLoader(ITerrainGenerator generator, int chunkSize, int graphicalSize, int range, float skyDomeRad) throws IOException {
        this.generator = generator;

        this.chunkSize = chunkSize;
        this.range = range;
        this.graphicalSize = graphicalSize;

        Map<String, String> constants = new HashMap<>();
        constants.put("skyDomeRadius", String.valueOf(skyDomeRad));
        constants.put("width", String.valueOf(this.chunkSize - 1));

        batchRenderer.batchRenderType.shaderProgram = new FadeOutShaderProgram(
                GLSLLoader.loadGLSL(Utils.utils.p("$shaders/chunk/chunk_vertex.v.glsl"), constants),
                GLSLLoader.loadGLSL(Utils.utils.p("$shaders/chunk/chunk_fragment.f.glsl"), constants)
        );
        batchRenderer.batchRenderType.shaderProgram.init();
    }

    private SurfaceChunk loadChunk(Vector2i position) throws IOException {
        Grid<Float> grid = new Grid<>(this.chunkSize, this.chunkSize);
        generator.updateGrid(grid, position);

        SurfaceChunk chunk = new SurfaceChunk(grid, position);
        chunk.loadGameObject(this.graphicalSize, this.range, this.batchRenderer.batchRenderType.shaderProgram);
        return chunk;
    }

    public void updateAroundPlayer(Vector2f position, World world, int radius) throws Exception {
        Vector2f chunkPosition = new Vector2f((int) position.x / graphicalSize, (int) position.y / graphicalSize);

        radius /= graphicalSize;

        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                Vector2i relativePosition = new Vector2i((int) (x + chunkPosition.x),(int) (y + chunkPosition.y));
                TerrainFeature existing = world.getFeature(new Vector2f(relativePosition).mul(graphicalSize));

                if (existing == null) {
                    SurfaceChunk chunk = this.loadChunk(relativePosition);
                    world.addFeature(chunk);
                    this.batchRenderer.gameObjects.add(chunk.getGameObject());
                } else if (!existing.getGameObject().visible)
                    existing.getGameObject().visible = true;
            }
        }
    }

    @Override
    public BatchRenderer getBatchRenderer() {
        return batchRenderer;
    }

    public int getGraphicalSize() {
        return graphicalSize;
    }

    public int getRange() {
        return range;
    }
}
