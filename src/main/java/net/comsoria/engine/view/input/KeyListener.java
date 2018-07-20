package net.comsoria.engine.view.input;

import net.comsoria.engine.Utils;

import java.util.List;

public class KeyListener {
    final KeyEvent keyEvent;
    private final List<Integer> keys;
    public boolean active = true;
    public boolean passive = false;

    public KeyListener(int[] keys, KeyEvent keyEvent, boolean passive) {
        this.keys = Utils.toIntList(keys);
        this.keyEvent = keyEvent;
        this.passive = passive;
    }

    boolean listenedKey(int key) {
        return active && keys.contains(key);
    }

    public interface KeyEvent {
        void call(int charCode, int action);
    }
}
