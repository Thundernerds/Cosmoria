package net.comsoria.game.terrain;

import net.comsoria.engine.loaders.GLSLLoader;
import net.comsoria.engine.utils.Grid;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.GLSL.Programs.FadeOutShaderProgram;
import net.comsoria.engine.view.batch.BatchRenderType;
import net.comsoria.engine.view.batch.BatchRenderer;
import net.comsoria.game.coordinate.ChunkPosition;
import net.comsoria.game.terrain.generation.ITerrainGenerator;
import org.joml.Vector2f;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChunkLoader {
    private final ITerrainGenerator generator;
    private final int radius;

    private final int chunkSize;
    private final int graphicalSize;
    private final int range;

    public final BatchRenderer batchRenderer = new BatchRenderer(new BatchRenderType());

    public ChunkLoader(ITerrainGenerator generator, int chunkSize, int graphicalSize, int radius, int range, float skyDomeRad) throws IOException {
        this.generator = generator;

        this.chunkSize = chunkSize;
        this.range = range;
        this.graphicalSize = graphicalSize;

        this.radius = radius;

        Map<String, String> constants = new HashMap<>();
        constants.put("skyDomeRadius", String.valueOf(skyDomeRad));
        constants.put("width", String.valueOf(this.chunkSize - 1));

        batchRenderer.batchRenderType.shaderProgram = new FadeOutShaderProgram(
                GLSLLoader.loadGLSL(Utils.utils.p("$shaders/chunk/chunk_vertex.v.glsl"), constants),
                GLSLLoader.loadGLSL(Utils.utils.p("$shaders/chunk/chunk_fragment.f.glsl"), constants)
        );
        batchRenderer.batchRenderType.shaderProgram.init();
    }

    private Chunk loadChunk(ChunkPosition position) throws IOException {
        Grid<Float> grid = new Grid<>(this.chunkSize, this.chunkSize);
        generator.updateGrid(grid, position);

        Chunk chunk = new Chunk(grid, position);
        chunk.loadGameObject(this.graphicalSize, this.range, this.batchRenderer.batchRenderType.shaderProgram);
        return chunk;
    }

    public void updateAroundPlayer(Vector2f position, World world) throws Exception {
        ChunkPosition chunkPosition = ChunkPosition.toChunkPosition(this.graphicalSize, position);

        for (Chunk chunk : world.chunks) {
            if (chunk.getGameObject().visible && chunk.position.distanceTo(chunkPosition) > radius) {
                chunk.getGameObject().visible = false;
            }
        }

        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                ChunkPosition relativePosition = new ChunkPosition(x + chunkPosition.getX(), y + chunkPosition.getY());
                Chunk existing = world.getChunk(relativePosition);

                if (existing == null) {
                    Chunk chunk = this.loadChunk(relativePosition);
                    world.addChunk(chunk);
                    this.batchRenderer.gameObjects.add(chunk.getGameObject());
                } else if (!existing.getGameObject().visible)
                    existing.getGameObject().visible = true;
            }
        }
    }

    public int getGraphicalSize() {
        return graphicalSize;
    }

    public int getRange() {
        return range;
    }
}
