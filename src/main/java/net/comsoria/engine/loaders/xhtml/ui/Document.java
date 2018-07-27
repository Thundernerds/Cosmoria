package net.comsoria.engine.loaders.xhtml.ui;

import net.comsoria.engine.view.FrameBufferRenderer;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
import net.comsoria.engine.view.Renderer;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.color.Color3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Document {
    public final List<UINode> nodeList = new ArrayList<>();
    public Color3 background = Color3.WHITE.clone();

    protected final FrameBufferRenderer frameBufferRenderer = new FrameBufferRenderer();

    private Transformation transformation = new Transformation();

    public Document() throws IOException {

    }

    public UINode getElementByID(String id) {
        for (UINode node : nodeList) {
            String elID = node.originalXML.getParam("id");
            if (elID != null && elID.equals(id)) return node;
        }

        return null;
    }

    public void addNode(UINode node, Window window) throws Exception {
        this.nodeList.add(node);

        node.genMesh(window);
    }

    public void updateAllStyleSets(Window window) throws Exception {
        for (UINode node : this.nodeList) {
            node.initStyleSets(window);
        }
    }

    public void render(Window window) throws Exception {
        frameBufferRenderer.renderBase(window);
        frameBufferRenderer.bindInitialFramebuffer();

        transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);

        Renderer.render(nodeList, null, transformation, window);

        frameBufferRenderer.renderFramebuffers(window);
    }

    public void cleanup() {
        for (UINode node : this.nodeList) node.getMesh().cleanup();
    }
}
