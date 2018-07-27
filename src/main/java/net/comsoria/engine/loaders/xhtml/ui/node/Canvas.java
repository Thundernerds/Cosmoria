package net.comsoria.engine.loaders.xhtml.ui.node;

import net.comsoria.engine.Scene;
import net.comsoria.engine.loaders.xhtml.ui.StyleSet;
import net.comsoria.engine.loaders.xml.XMLNode;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.Renderer;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.batch.RenderData;

import java.io.Closeable;

public class Canvas extends Rectangle {
    public Scene scene = new Scene();

    public Canvas(StyleSet styleSet, XMLNode xmlNode) {
        super(styleSet, xmlNode);
    }

    @Override
    public Closeable render(Transformation transformation, Scene s, RenderData renderData, Window window) throws Exception {
        transformation.getView(scene.camera);
        transformation.getProjectionMatrix(window, scene.camera);
        transformation.getViewNoRotation(scene.camera);
        transformation.getViewNoTranslation(scene.camera);

        Renderer.setViewPort(
                0, //TODO: turn into real values
                0,
                (int) (Float.valueOf(styleSet.ruleMap.get("width").value) * (window.getWidth() / 100f)),
                (int) (Float.valueOf(styleSet.ruleMap.get("height").value) * (window.getHeight() / 100f))
        );

        scene.render(transformation, window);

//        System.out.println("RENDER");

        return null;
    }

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.First;
    }
}
