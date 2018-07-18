package net.comsoria.game.terrain;

import net.comsoria.engine.Grid;
import net.comsoria.engine.view.batch.BatchRenderType;
import net.comsoria.engine.view.batch.BatchRenderer;
import net.comsoria.game.coordinate.ChunkPosition;
import net.comsoria.game.terrain.generation.ITerrainGenerator;
import org.joml.Vector2f;

import java.io.IOException;

public class ChunkLoader {
    private final ITerrainGenerator generator;
    private final int radius;

    private final int chunkSize;
    private final int graphicalSize;
    private final int range;

    public final BatchRenderer batchRenderer = new BatchRenderer(new BatchRenderType());

    public ChunkLoader(ITerrainGenerator generator, int chunkSize, int graphicalSize, int radius, int range) {
        this.generator = generator;

        this.chunkSize = chunkSize;
        this.range = range;
        this.graphicalSize = graphicalSize;

        this.radius = radius;

        batchRenderer.batchRenderType.shaderProgram = new ChunkShaderProgram((1.0f / graphicalSize) * this.range);
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
                }
                else if (!existing.getGameObject().visible)
                    existing.getGameObject().visible = true;
            }
        }
    }
}
