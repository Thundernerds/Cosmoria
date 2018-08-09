package net.comsoria.engine.loaders.xhtml.ui.node;

import net.comsoria.engine.loaders.Shape;
import net.comsoria.engine.loaders.xhtml.ui.StyleSet;
import net.comsoria.engine.loaders.xhtml.ui.UINode;
import net.comsoria.engine.loaders.xml.XMLNode;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.GLSL.Programs.ShaderProgram2D;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.Texture;
import net.comsoria.engine.view.graph.mesh.Mesh;
import net.comsoria.engine.view.graph.mesh.Mesh2D;

public class Rectangle extends UINode {
    public Rectangle(StyleSet styleSet, XMLNode xmlNode) {
        super(styleSet, xmlNode);
    }

    @Override
    protected void genMesh(Window window) throws Exception {
        Geometry geometry = new Geometry(Shape.genRect(
                Float.valueOf(styleSet.ruleMap.get("width").value) * (window.getWidth() / 100f),
                Float.valueOf(styleSet.ruleMap.get("height").value) * (window.getHeight() / 100f)
        ));
        mesh = new Mesh2D(geometry, new Material());
        mesh.material.shaderProgram = new ShaderProgram2D();
        mesh.initShaderProgram();

        String texturePath = originalXML.getParam("imageSrc");
        if (texturePath != null) {
            Texture texture = new Texture(Utils.utils.p(texturePath));
            mesh.material.textures.add(texture);
            mesh.material.shaderProgram.createTextureUniform("texture_sampler");
        }
    }

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.First;
    }
}
