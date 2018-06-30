package net.comsoria.engine;

import net.comsoria.engine.view.GameObject;
import net.comsoria.engine.view.Window;

import java.util.List;

public interface IHud {
    List<GameObject> getGameItems();

    default void cleanup() {
        List<GameObject> gameObjects = getGameItems();
        for (GameObject gameObject : gameObjects) {
            gameObject.getMesh().cleanup();
        }
    }

    void updateSize(Window window);
}
