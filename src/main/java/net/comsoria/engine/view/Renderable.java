package net.comsoria.engine.view;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.GLSL.Transformation;

import java.io.Closeable;

public interface Renderable {
    Closeable render(Window window, Transformation transformation, Scene scene) throws Exception;
    void cleanup();
    boolean shouldRender();
}
