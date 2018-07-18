package net.comsoria.engine.view;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.batch.RenderData;
import net.comsoria.engine.view.GLSL.Transformation;

import java.io.Closeable;

public interface Renderable {
    Closeable render(Transformation transformation, Scene scene, RenderData renderData) throws Exception;
    void cleanup();
    boolean shouldRender();
    RenderOrder getRenderOrder();

    enum RenderOrder {
        First,
        Middle,
        End,
        Any
    }
}
