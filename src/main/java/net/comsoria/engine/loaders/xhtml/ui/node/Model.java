package net.comsoria.engine.loaders.xhtml.ui.node;

import net.comsoria.engine.loaders.OBJLoader;
import net.comsoria.engine.loaders.xhtml.ui.StyleSet;
import net.comsoria.engine.loaders.xhtml.ui.UINode;
import net.comsoria.engine.loaders.xml.XMLNode;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.GLSL.programs.ShaderProgram2D;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.mesh.Mesh2D;

public class Model extends UINode {
    public Model(StyleSet styleSet, XMLNode xmlNode) {
        super(styleSet, xmlNode);
    }

    @Override
    protected void genMesh(Window window) throws Exception {
        mesh = new Mesh2D(new Geometry(OBJLoader.loadGeometry(Utils.utils.p(this.originalXML.getParam("path")))), new Material(), new ShaderProgram2D());
        mesh.initShaderProgram();
    }

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.End;
    }
}
