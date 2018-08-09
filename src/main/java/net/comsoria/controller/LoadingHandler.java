package net.comsoria.controller;

import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.loaders.xhtml.XHTMLLoader;
import net.comsoria.engine.loaders.xhtml.ui.Document;
import net.comsoria.engine.loaders.xhtml.ui.DocumentHandler;
import net.comsoria.engine.loaders.xhtml.ui.StyleSet;
import net.comsoria.engine.loaders.xhtml.ui.UINode;
import net.comsoria.engine.utils.Timer;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.color.Color4;

public class LoadingHandler extends DocumentHandler {
    private UINode center;

    private final static double wait = 50, fade = 50;

    @Override public Document init(Window window) throws Exception {
        Document document = XHTMLLoader.loadDocument(FileLoader.loadResourceAsStringFromPath("$uis/loading.xhtml"), window);
        document.updateAllStyleSets(window);

        this.center = document.getElementByID("center");

        return document;
    }

    @Override public void cleanup() {

    }

    @Override public DocumentHandler update(Window window, Document document, float interval) throws Exception {
        if (window.isResized()) {
            StyleSet.StyleRule rule = this.center.styleSet.ruleMap.get("scale");
            rule.setValue(Float.valueOf(rule.value) * (window.getWidth() / 800f));

            document.updateAllStyleSets(window);
        }

        float opacity = (1 - (float) Math.max((Timer.getGameLoopIndex() - wait) / fade, 0)) - 0.02f;
        center.styleSet.ruleMap.get("color").setValue(Color4.BLACK.clone().setA(opacity).toString(false));
        center.updateStyleSets(window);

        return Timer.getGameLoopIndex() == wait + fade? new GameHandler():null;
    }
}
