package net.comsoria.controller;

import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.loaders.xhtml.XHTMLLoader;
import net.comsoria.engine.loaders.xhtml.ui.Document;
import net.comsoria.engine.loaders.xhtml.ui.DocumentHandler;
import net.comsoria.engine.loaders.xhtml.ui.StyleSet;
import net.comsoria.engine.utils.Timer;
import net.comsoria.engine.view.Window;

public class LoadingHandler extends DocumentHandler {
    @Override public Document init(Window window) throws Exception {
        Document document = XHTMLLoader.loadDocument(FileLoader.loadResourceAsStringFromPath("$uis/loading.xhtml"), window);
        document.updateAllStyleSets(window);

        return document;
    }

    @Override public DocumentHandler update(Window window, Document document, float interval) throws Exception {
        if (window.isResized()) {
            StyleSet.StyleRule rule = document.getElementByID("center").styleSet.ruleMap.get("scale");
            rule.value = String.valueOf(Float.valueOf(rule.value) * (window.getWidth() / 800f));

            document.updateAllStyleSets(window);
        }

        return Timer.getGameLoopIndex() == 100? new GameHandler():null;
    }
}
