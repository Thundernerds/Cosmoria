package net.comsoria.engine.loaders.xhtml.ui.node;

import net.comsoria.engine.loaders.text.FontTexture;
import net.comsoria.engine.loaders.text.TextLoader;
import net.comsoria.engine.loaders.xhtml.ui.UINode;
import net.comsoria.engine.loaders.xhtml.ui.StyleSet;
import net.comsoria.engine.loaders.xml.XMLNode;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.Renderable;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.graph.mesh.Mesh;

import java.awt.*;
import java.io.File;

public class Paragraph extends UINode {
    public Paragraph(StyleSet styleSet, XMLNode xmlNode) {
        super(styleSet, xmlNode);
    }

    @Override
    protected void genMesh(Window window) throws Exception {
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(Utils.utils.p(styleSet.ruleMap.get("font").value))).deriveFont(48f);
        this.mesh = TextLoader.buildMesh(originalXML.getInnerText(), new FontTexture(font, "ISO-8859-1"));
    }

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.End;
    }
}
