package net.comsoria.game.terrain;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.batch.BatchCloseable;
import net.comsoria.engine.view.batch.RenderData;
import net.comsoria.game.terrain.terrainFeature.TerrainFeature;
import net.comsoria.game.terrain.terrainFeature.TerrainFeatureLoader;
import net.comsoria.game.terrain.terrainFeature.surfaceChunk.SurfaceChunk;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class World implements Renderable {
    public final static int MAX_DEPTH = 20000;

    private final List<TerrainFeatureLoader> loaders = new ArrayList<>();
    private List<Chunk> chunks = new ArrayList<>();

    private int radius = 4;
    private float scale;

    public World(float scale) {
        this.scale = scale;
    }

    public void addLoader(TerrainFeatureLoader loader) {
        this.loaders.add(loader);
    }

    public Chunk loadChunk(Vector2i position) throws IOException {
        Chunk chunk = new Chunk(position);
        chunk.load(loaders, scale);
        return chunk;
    }

    public Chunk getChunk(Vector2i position) {
        for (Chunk chunk : chunks)
            if (position.x == chunk.position.x && position.y == chunk.position.y) return chunk;

        return null;
    }

    public void updateAroundPlayer(Vector2f playerPosition) throws IOException {
        playerPosition = playerPosition.mul(1 / scale);

        Vector2i chunkPosition = new Vector2i((int) playerPosition.x, (int) playerPosition.y);

        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                Vector2i relativePosition = new Vector2i(x + chunkPosition.x, y + chunkPosition.y);

                Chunk existing = this.getChunk(relativePosition);

                if (existing == null) {
                    Chunk chunk = this.loadChunk(relativePosition);
                    this.chunks.add(chunk);
                } else if (!existing.isShown()) existing.show();
            }
        }

        for (Chunk chunk : this.chunks) {
            if (chunk.isShown() && new Vector2f(chunk.position).distance(playerPosition) > radius) {
                chunk.hide();
            }
        }
    }

    @Override
    public Closeable render(Transformation transformation, Scene scene, RenderData renderData, Window window) throws Exception {
        List<Closeable> toClose = new ArrayList<>();

        for (TerrainFeatureLoader loader : loaders) {
            toClose.add(loader.getBatchRenderer().render(transformation, scene, renderData, window));
        }

        return new BatchCloseable(toClose);
    }

    @Override
    public void cleanup() {
        for (TerrainFeatureLoader loader : loaders)
            loader.getBatchRenderer().cleanup();
    }

    @Override
    public boolean shouldRender() {
        return true;
    }

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.Any;
    }
}
