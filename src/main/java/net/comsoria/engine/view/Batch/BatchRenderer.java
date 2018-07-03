package net.comsoria.engine.view.Batch;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.graph.Mesh;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BatchRenderer implements Renderable {
    public final List<Mesh> gameObjects;
    public BatchRenderType batchRenderType;
    public boolean shouldRender = true;

    public BatchRenderer(BatchRenderType batchRenderType, List<Mesh> gameObjects) {
        this.gameObjects = gameObjects;
        this.batchRenderType = batchRenderType;
    }

    public BatchRenderer(BatchRenderType batchRenderType) {
        this(batchRenderType, new ArrayList<>());
    }

    @Override
    public Closeable render(Transformation transformation, Scene scene, RenderData renderData) throws Exception {
        List<Closeable> toClose = new ArrayList<>();

        RenderData meshRenderData = new RenderData() {
            @Override public boolean shouldBindGeometry() {
                return batchRenderType.geometry == null;
            }
            @Override public boolean shouldBindShaderProgram() {
                return batchRenderType.shaderProgram == null;
            }
        };

        if (!meshRenderData.shouldBindGeometry()) batchRenderType.geometry.bind();
        if (!meshRenderData.shouldBindShaderProgram()) batchRenderType.shaderProgram.bind();

        for (Mesh gameItem : gameObjects) {
            if (!gameItem.shouldRender()) continue;

            Closeable item = gameItem.render(transformation, scene, meshRenderData);
            if (item != null && !toClose.contains(item)) toClose.add(item);
        }

        for (Closeable closeable : toClose) closeable.close();

        if (!meshRenderData.shouldBindGeometry()) batchRenderType.geometry.unbind();
        if (!meshRenderData.shouldBindShaderProgram()) batchRenderType.shaderProgram.unbind();

        return new BatchCloseable(toClose);
    }

    @Override public void cleanup() {
        for (Mesh renderable : gameObjects) renderable.cleanup();
    }

    @Override
    public boolean shouldRender() {
        return shouldRender;
    }
}
