package net.comsoria.engine;

import net.comsoria.engine.view.Fog;
import net.comsoria.engine.view.GameObject;
import net.comsoria.engine.view.Light.SceneLight;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    public final SceneLight light;
    public final List<GameObject> children;
    public IHud hud;
    public Fog fog;

    public Scene(IHud hud) {
        this.light = new SceneLight();
        this.children = new ArrayList<>();
        this.hud = hud;
    }

    public void cleanup() {
        for (GameObject gameItem : this.children) {
            gameItem.getMesh().cleanup();
        }
        hud.cleanup();
    }
}
