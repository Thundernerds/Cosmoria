package net.comsoria.engine;

import net.comsoria.engine.view.Batch.RenderData;
import net.comsoria.engine.view.Camera;
import net.comsoria.engine.view.Fog;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.Light.SceneLight;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.graph.Geometry;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    public final SceneLight light;
    private final List<Renderable> children;
    public Hud hud;
    public Fog fog;
    public Camera camera;

    public Scene(Hud hud) {
        this.light = new SceneLight();
        this.children = new ArrayList<>();
        this.hud = hud;
        this.camera = new Camera();
    }

    public void cleanup() {
        for (Renderable gameItem : this.children) {
            gameItem.cleanup();
        }
        hud.cleanup();
    }

    public void render(Transformation transformation) throws Exception {
        List<Closeable> toClose = new ArrayList<>();

        for (Renderable gameItem : this.children) {
            if (!gameItem.shouldRender()) continue;

            Closeable item = gameItem.render(transformation, this, RenderData.defaultRenderData);
            if (item != null && !toClose.contains(item)) toClose.add(item);
        }

        for (Closeable closeable : toClose) closeable.close();
    }

    public void add(Renderable gameObject) throws Exception {
        this.children.add(gameObject);
    }
}
