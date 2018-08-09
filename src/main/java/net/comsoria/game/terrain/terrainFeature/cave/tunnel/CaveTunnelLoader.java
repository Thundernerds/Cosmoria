package net.comsoria.game.terrain.terrainFeature.cave.tunnel;

import net.comsoria.engine.view.batch.BatchRenderer;
import net.comsoria.game.terrain.World;
import net.comsoria.game.terrain.terrainFeature.TerrainFeatureLoader;
import org.joml.Vector2f;

public class CaveTunnelLoader implements TerrainFeatureLoader {
    private float probability;

    public CaveTunnelLoader(float probability) {
        this.probability = probability;
    }

    @Override
    public void updateAroundPlayer(Vector2f position, World world, int radius) throws Exception {

    }

    @Override
    public BatchRenderer getBatchRenderer() {
        return null;
    }
}
