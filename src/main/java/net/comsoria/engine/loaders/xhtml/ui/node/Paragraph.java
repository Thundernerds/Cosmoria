package net.comsoria.engine.loaders.xhtml.ui.node;

import net.comsoria.engine.loaders.Shape;
import net.comsoria.engine.loaders.text.FontTexture;
import net.comsoria.engine.loaders.text.TextLoader;
import net.comsoria.engine.loaders.xhtml.ui.UINode;
import net.comsoria.engine.loaders.xhtml.ui.StyleSet;
import net.comsoria.engine.loaders.xml.XMLNode;
import net.comsoria.engine.utils.Tuple;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.programs.ShaderProgram2D;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.graph.BufferAttribute;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.mesh.Mesh;
import net.comsoria.engine.view.graph.mesh.Mesh2D;

import java.awt.*;
import java.io.File;
import java.util.List;

public class Paragraph extends UINode {
    public Paragraph(StyleSet styleSet, XMLNode xmlNode) {
        super(styleSet, xmlNode);
    }

    @Override
    protected void genMesh(Window window) throws Exception {
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(Utils.utils.p(styleSet.ruleMap.get("font").value))).deriveFont(90f);
        FontTexture texture = new FontTexture(font, "ISO-8859-1");

        Tuple<List<BufferAttribute>, int[]> geomData = TextLoader.buildGeometryData(originalXML.getInnerText(), texture);

        String offsetString = originalXML.getParam("anchor");
        if (offsetString != null) {
            String[] parts = offsetString.split(" ");

            Shape.reanchor(geomData.getA().get(0), Float.valueOf(parts[0]), Float.valueOf(parts[1]), Float.valueOf(parts[2]));
        }
        Geometry geometry = new Geometry(geomData);

        this.mesh = new Mesh2D(geometry, new Material(), new ShaderProgram2D());
        mesh.initShaderProgram();
        mesh.material.textures.add(texture.getTexture());
        mesh.shaderProgram.createTextureUniform("texture_sampler");
    }

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.End;
    }
}
