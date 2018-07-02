package net.comsoria.engine.view;

import net.comsoria.Utils;
import net.comsoria.engine.Hud;
import net.comsoria.engine.Scene;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.graph.Mesh;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private FrameBuffer postProcessingFBO = null;

    public void init(Window window) throws Exception {
        postProcessingFBO = new FrameBuffer(window.getWidth(), window.getHeight(),
                Utils.loadResourceAsString("$pp_vertex"), Utils.loadResourceAsString("$pp_fragment"));
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Scene scene) throws Exception {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            postProcessingFBO.setSize(window.getWidth(), window.getHeight());
            window.setResized(false);
            scene.hud.updateSize(window);
        }

        postProcessingFBO.bind();
        clear();
        scene.render(window);
        if (scene.hud != null) scene.hud.render(window);

        FrameBuffer.unbind();

        clear();
        postProcessingFBO.render(window);
    }
}
