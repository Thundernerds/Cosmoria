package net.comsoria.engine;

import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.batch.RenderData;
import net.comsoria.engine.view.Camera;
import net.comsoria.engine.view.FadeFog;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
import net.comsoria.engine.view.light.SceneLight;
import net.comsoria.engine.view.Renderable;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    public final SceneLight light;
    private final List<Renderable> children;
    public FadeFog fog;
    public final Camera camera;
    public Sky sky;

    public Scene() {
        this.light = new SceneLight();
        this.children = new ArrayList<>();
        this.camera = new Camera();
        this.sky = new Sky(-0.5f, 0.25f, 0.15f);
    }

    public void cleanup() {
        for (Renderable gameItem : this.children) {
            gameItem.cleanup();
        }
    }

    public void render(Transformation transformation, Window window) throws Exception {
        List<Closeable> toClose = new ArrayList<>();

        List<Renderable> start = new ArrayList<>();
        List<Renderable> middle = new ArrayList<>();
        List<Renderable> end = new ArrayList<>();
        for (Renderable renderable : this.children)
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

            Closeable item = gameItem.render(transformation, this, RenderData.defaultRenderData, window);
            if (item != null && !toClose.contains(item)) toClose.add(item);
        }

        for (Closeable closeable : toClose) closeable.close();
    }

    public void add(Renderable gameObject) throws Exception {
        this.children.add(gameObject);
    }

    public void updateLight(float time) {
        this.sky.calcColors(time);
        this.light.ambientLight.set(this.sky.getAmbience());
        this.light.directionalLight.direction.set(this.sky.getSunDirection());
    }
}
