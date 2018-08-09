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
import java.util.ArrayList;
import java.util.List;

public class World implements Renderable {
    private final List<TerrainFeature> buffer = new ArrayList<>();

    private final List<TerrainFeatureLoader> loaders = new ArrayList<>();
    private final List<TerrainFeature> features = new ArrayList<>();

    private int radius = 16000;

    public void addLoader(TerrainFeatureLoader loader) {
        this.loaders.add(loader);
    }

    public TerrainFeature getFeature(Vector2f position) {
        for (TerrainFeature feature : features) {
            if (position.x == feature.getPosition().x && position.y == feature.getPosition().y) return feature;
        }
        return null;
    }

    public void addFeature(TerrainFeature feature) {
        features.add(feature);
//        buffer.add(feature);
    }

    public List<TerrainFeature> getBuffer() {
//        List<TerrainFeature> oldBuffer = new ArrayList<>();
//        oldBuffer.addAll(buffer);
//        buffer.clear();

        return null;
    }

    public List<TerrainFeature> getFeatures() {
        return features;
    }

    public void updateAroundPlayer(Vector2f playerPosition) throws Exception {
        for (TerrainFeature feature : this.features) {
            if (feature.getGameObject().visible && feature.getPosition().distance(playerPosition) > radius) {
                feature.getGameObject().visible = false;
            }
        }

        for (TerrainFeatureLoader loader : loaders) loader.updateAroundPlayer(playerPosition, this, radius);
    }

    @Override
    public Closeable render(Transformation transformation, Scene scene, RenderData renderData, Window window) throws Exception {
        List<Closeable> toClose = new ArrayList<>();

        for (TerrainFeatureLoader loader : loaders)
            toClose.add(loader.getBatchRenderer().render(transformation, scene, renderData, window));

        return new BatchCloseable(toClose);
    }

    @Override
    public void cleanup() {
        for (TerrainFeatureLoader loader : loaders) loader.getBatchRenderer().cleanup();
        for (TerrainFeature feature : features) feature.getGameObject().cleanup();
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
