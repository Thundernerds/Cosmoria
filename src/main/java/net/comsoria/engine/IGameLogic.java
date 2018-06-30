package net.comsoria.engine;

import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.input.KeyInput;
import net.comsoria.engine.view.input.MouseInput;

public interface IGameLogic {
    void init(Window window, KeyInput keyInput) throws Exception;

    void update(Window window, float interval, MouseInput mouse, KeyInput keys);
    void render(Window window) throws Exception;

    void cleanup();
}
