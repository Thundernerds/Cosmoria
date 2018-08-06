package net.comsoria.engine.view;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.batch.RenderData;
import net.comsoria.engine.view.GLSL.matrices.Transformation;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    public static void setViewPort(int x, int y, int width, int height) {
        glViewport(x, y, width, height);
    }

    public static void render(List<? extends Renderable> renderables, Scene scene, Transformation transformation, Window window) throws Exception {
        List<Closeable> toClose = new ArrayList<>();

        List<Renderable> start = new ArrayList<>();
        List<Renderable> middle = new ArrayList<>();
        List<Renderable> end = new ArrayList<>();
        for (Renderable renderable : renderables)
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

            Closeable item = gameItem.render(transformation, scene, RenderData.defaultRenderData, window);
            if (item != null && !toClose.contains(item)) toClose.add(item);
        }

        for (Closeable closeable : toClose) closeable.close();
    }
}
