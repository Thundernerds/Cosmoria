package net.comsoria.engine.view.batch;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.Renderable;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class BatchRenderer implements Renderable {
    public final List<Renderable> gameObjects;
    public BatchRenderType batchRenderType;

    public boolean shouldRender = true;
    public RenderOrder renderPosition = RenderOrder.Any;

    public BatchRenderer(BatchRenderType batchRenderType, List<Renderable> gameObjects) {
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
            @Override public boolean shouldBindOwnGeometry() {
                return batchRenderType.geometry == null;
            }
            @Override public boolean shouldBindOwnShaderProgram() {
                return batchRenderType.shaderProgram == null;
            }
        };

        if (!meshRenderData.shouldBindOwnGeometry()) batchRenderType.geometry.bind();
        if (!meshRenderData.shouldBindOwnShaderProgram()) batchRenderType.shaderProgram.bind();

        List<Renderable> start = new ArrayList<>();
        List<Renderable> middle = new ArrayList<>();
        List<Renderable> end = new ArrayList<>();
        for (Renderable renderable : gameObjects)
            switch (renderable.getRenderOrder()) {
                case First:
                    start.add(renderable);
                    break;
                case End:
                    end.add(renderable);
                    break;
                default:
                    middle.add(renderable);
                    break;
            }

        List<Renderable> total = new ArrayList<>(start);
        total.addAll(middle);
        total.addAll(end);

        for (Renderable gameItem : total) {
            if (!gameItem.shouldRender()) continue;

            Closeable item = gameItem.render(transformation, scene, meshRenderData);
            if (item != null && !toClose.contains(item)) toClose.add(item);
        }

        for (Closeable closeable : toClose) closeable.close();

        if (!meshRenderData.shouldBindOwnGeometry()) batchRenderType.geometry.unbind();
        if (!meshRenderData.shouldBindOwnShaderProgram()) batchRenderType.shaderProgram.unbind();

        return new BatchCloseable(toClose);
    }

    @Override public void cleanup() {
        for (Renderable renderable : gameObjects) renderable.cleanup();
    }

    @Override
    public boolean shouldRender() {
        return shouldRender;
    }

    @Override
    public RenderOrder getRenderOrder() {
        return this.renderPosition;
    }
}
