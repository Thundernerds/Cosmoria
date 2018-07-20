package net.comsoria.engine.view.input;

import net.comsoria.engine.view.Window;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

public class KeyInput extends GLFWKeyCallback {
    private Map<Integer, Key> keys = new HashMap<>();
    private List<KeyListener> keyListeners = new ArrayList<>();

    public void init(Window window) {
        glfwSetKeyCallback(window.getWindowHandle(), this);
    }

    public void addListener(KeyListener listener) {
        keyListeners.add(listener);
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        for (KeyListener listener : keyListeners) {
            if (listener.listenedKey(key)) {
                listener.keyEvent.call(key, action);
                if (!listener.passive) break;
            }
        }

        Key objKey;

        if (!keys.keySet().contains(key)) {
            objKey = new Key();
            keys.put(key, objKey);
        } else objKey = keys.get(key);

        objKey.action = action;
    }

    private boolean keyExists(int keyCode) {
        return keys.keySet().contains(keyCode);
    }

    public boolean isKeyPressed(int keyCode) {
        return keyExists(keyCode) && keys.get(keyCode).isPressed();
    }
}

class Key {
    public int action = GLFW_RELEASE;

    public boolean isPressed() {
        return action != GLFW_RELEASE;
    }
}