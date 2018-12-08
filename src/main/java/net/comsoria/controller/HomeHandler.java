package net.comsoria.controller;

import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.loaders.xhtml.XHTMLLoader;
import net.comsoria.engine.loaders.xhtml.ui.Document;
import net.comsoria.engine.loaders.xhtml.ui.DocumentHandler;
import net.comsoria.engine.view.Window;

public class HomeHandler extends DocumentHandler {
    @Override public Document init(Window window) throws Exception {
        Document document = XHTMLLoader.loadDocument(FileLoader.loadResourceAsStringFromPath("$uis/home.xhtml"), window);
        document.updateAllStyleSets(window);
        return document;
    }

    @Override public void cleanup() {

    }

    @Override
    public DocumentHandler update(Window window, Document document, float interval) throws Exception {
        if (window.isResized()) document.updateAllStyleSets(window);
        return null;
    }
}
