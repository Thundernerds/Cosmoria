package net.comsoria.game.terrain;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.batch.RenderData;
import net.comsoria.game.terrain.terrainFeature.TerrainFeature;
import net.comsoria.game.terrain.terrainFeature.TerrainFeatureLoader;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Chunk {
    public Vector2i position;
    public List<TerrainFeature> terrainFeatures = new ArrayList<>();

    public Chunk(Vector2i position) {
        this.position = position;
    }

    void load(List<TerrainFeatureLoader> terrainFeatureLoaders, float scale) throws IOException {
        for (TerrainFeatureLoader loader : terrainFeatureLoaders) {
            terrainFeatures.addAll(loader.load(this.position, scale));
        }
    }

    void hide() {
        for (TerrainFeature feature : this.terrainFeatures) {
            feature.getGameObject().visible = false;
        }
    }

    void show() {
        for (TerrainFeature feature : this.terrainFeatures) {
            feature.getGameObject().visible = true;
        }
    }

    boolean isShown() {
        if (terrainFeatures.size() > 0) return terrainFeatures.get(0).getGameObject().visible;

        return true;
    }
}
