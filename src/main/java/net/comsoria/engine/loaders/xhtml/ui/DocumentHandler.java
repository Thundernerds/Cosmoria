package net.comsoria.engine.loaders.xhtml.ui;

import net.comsoria.engine.view.FrameBufferRenderer;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.input.KeyInput;
import net.comsoria.engine.view.input.MouseInput;

import java.io.IOException;

public abstract class DocumentHandler {
    public final KeyInput keyInput = new KeyInput();
    public final MouseInput mouseInput = new MouseInput();

    public abstract Document init(Window window) throws Exception;
    public abstract DocumentHandler update(Window window, Document document, float interval) throws Exception;
}
