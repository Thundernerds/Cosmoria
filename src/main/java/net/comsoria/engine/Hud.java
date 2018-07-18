package net.comsoria.engine;

import net.comsoria.engine.view.batch.RenderData;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.Window;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class Hud {
    public List<Renderable> gameObjects = new ArrayList<>();

    public void cleanup() {
        for (Renderable gameObject : gameObjects) {
            gameObject.cleanup();
        }
    }

    public void updateSize(Window window) {

    }

    public void render(Transformation transformation) throws Exception {
        List<Closeable> toClose = new ArrayList<>();

        for (Renderable gameItem : this.gameObjects) {
            if (!gameItem.shouldRender()) continue;

            Closeable item = gameItem.render(transformation, null, RenderData.defaultRenderData);
            if (item != null && !toClose.contains(item)) toClose.add(item);
        }

        for (Closeable closeable : toClose) closeable.close();
    }
}
