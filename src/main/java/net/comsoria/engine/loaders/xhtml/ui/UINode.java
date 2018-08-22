package net.comsoria.engine.loaders.xhtml.ui;

import net.comsoria.engine.Scene;
import net.comsoria.engine.loaders.xml.XMLNode;
import net.comsoria.engine.utils.Tuple;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.batch.RenderData;
import net.comsoria.engine.view.graph.mesh.Mesh;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public abstract class UINode implements Renderable {
    public StyleSet styleSet;

    protected XMLNode originalXML;

    protected Mesh mesh;

    public List<Tuple<EventType, EventListener>> eventListeners = new ArrayList<>();

    public UINode(StyleSet styleSet, XMLNode xmlNode) {
        this.styleSet = styleSet;
        this.originalXML = xmlNode;
    }

    protected abstract void genMesh(Window window) throws Exception;

    public void updateStyleSets(Window window) throws Exception {
        if (this.mesh != null)
            for (String name : styleSet.ruleMap.keySet()) {
                styleSet.ruleMap.get(name).updateMesh(mesh, window);
            }
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void addEventListener(EventType eventType, EventListener eventListener) {
        eventListeners.add(new Tuple<>(eventType, eventListener));
    }

    public void event(EventType type) {
        for (Tuple<EventType, EventListener> listenerTuple : eventListeners) {
            if (listenerTuple.getA() == type) listenerTuple.getB().call(this);
        }
    }

    @Override
    public Closeable render(Transformation transformation, Scene scene, RenderData renderData, Window window) throws Exception {
        return mesh.render(transformation, scene, renderData, window);
    }

    @Override
    public void cleanup() {
        if (this.mesh != null) mesh.cleanup();
    }

    @Override
    public boolean shouldRender() {
        return true;
    }

    public enum EventType {
        CLICK,
        HOVER_ON,
        HOVER_OFF
    }

    public interface EventListener {
        void call(UINode node);
    }
}
