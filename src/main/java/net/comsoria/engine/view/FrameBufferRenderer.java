package net.comsoria.engine.view;

import net.comsoria.engine.math.Rectangle;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
import net.comsoria.engine.view.batch.RenderData;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class FrameBufferRenderer {
    public List<FrameBuffer> frameBuffers = new ArrayList<>();
    private Transformation transformation = new Transformation();

    public void renderBase(Window window) {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            for (FrameBuffer frameBuffer : frameBuffers) frameBuffer.setSize(window.getWidth(), window.getHeight());
            window.setResized(false);
        }
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void bindInitialFramebuffer() {
        clear();
        if (frameBuffers.size() != 0) frameBuffers.get(0).bind();
    }

    public void renderFramebuffers(Window window) throws Exception {
        List<Closeable> closeables = new ArrayList<>();

        for (int i = 1; i < frameBuffers.size(); i++) {
            FrameBuffer frameBuffer = frameBuffers.get(i);
            frameBuffer.bind();

            clear();
            closeables.add(frameBuffers.get(i - 1).render(transformation, null, RenderData.defaultRenderData, window));
        }

        if (frameBuffers.size() != 0) {
            FrameBuffer.unbind();

            clear();
            closeables.add(frameBuffers.get(frameBuffers.size() - 1).render(transformation, null, RenderData.defaultRenderData, window));
        }

        for (Closeable closeable : closeables) closeable.close();
    }
}
