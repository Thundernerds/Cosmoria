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
//    private Transformation transformation = new Transformation();

//    public List<FrameBuffer> frameBuffers = new ArrayList<>();

//    public void clear() {
//        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//    }

//    public void render(Window window, Scene scene) throws Exception {
//        if (window.isResized()) {
//            glViewport(0, 0, window.getWidth(), window.getHeight());
//            for (FrameBuffer frameBuffer : frameBuffers) frameBuffer.setSize(window.getWidth(), window.getHeight());
//            window.setResized(false);
//            scene.hud.updateSize(window);
//
////            transformation.genProjection(scene.camera, window);
////            transformation.genOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
//        }
//
////        transformation.genViewMatrices(scene.camera);
//
//        if (frameBuffers.size() != 0) frameBuffers.get(0).bind();
//
//        clear();
//        scene.render(transformation);
//
//        if (scene.hud != null) scene.hud.render(transformation);
//
//        List<Closeable> closeables = new ArrayList<>();
//
//        for (int i = 1; i < frameBuffers.size(); i++) {
//            FrameBuffer frameBuffer = frameBuffers.get(i);
//            frameBuffer.bind();
//
//            clear();
//            closeables.add(frameBuffers.get(i - 1).render(transformation, scene, RenderData.defaultRenderData, window));
//        }
//
//        if (frameBuffers.size() != 0) {
//            FrameBuffer.unbind();
//
//            clear();
//            closeables.add(frameBuffers.get(frameBuffers.size() - 1).render(transformation, scene, RenderData.defaultRenderData, window));
//        }
//
//        for (Closeable closeable : closeables) closeable.close();
//    }

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
