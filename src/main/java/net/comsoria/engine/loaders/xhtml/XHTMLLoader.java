package net.comsoria.engine.loaders.xhtml;

import net.comsoria.engine.loaders.xhtml.ui.Document;
import net.comsoria.engine.loaders.xhtml.ui.UINode;
import net.comsoria.engine.loaders.xhtml.ui.StyleSet;
import net.comsoria.engine.loaders.xhtml.ui.node.Canvas;
import net.comsoria.engine.loaders.xhtml.ui.node.Paragraph;
import net.comsoria.engine.loaders.xhtml.ui.node.Rectangle;
import net.comsoria.engine.loaders.xml.XMLLoader;
import net.comsoria.engine.loaders.xml.XMLNode;
import net.comsoria.engine.utils.Logger;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.color.Color3;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XHTMLLoader {
    private static Map<String, Class<? extends UINode>> nodeTypes = new HashMap<>();

    static {
        nodeTypes.put("p", Paragraph.class);
        nodeTypes.put("canvas", Canvas.class);
        nodeTypes.put("rect", Rectangle.class);
    }

    public static Document loadDocument(String text, Window window) throws Exception {
        Document result = new Document();

        XMLNode page = XMLLoader.loadXML(text);

        Map<String, StyleSet> styles = new HashMap<>();

        String bg = page.getParam("bg");
        if (bg != null) result.background = Color3.valueOf(bg);

        for (XMLNode node : page.getAllChildren()) {
            if (node.getTagName().equals("style")) {
                styles.putAll(getStyleSets(node.getInnerXML()));
            } else if (nodeTypes.containsKey(node.getTagName())) {
                result.addNode(genNode(node.getTagName(), findStyleSet(styles, node), node), window);
            } else {
                Logger.log("Failed to find tag of type '" + node.getTagName() + "'", Logger.LogType.WARN);
            }
        }

        return result;
    }

    private static UINode genNode(String type, StyleSet styleSet, XMLNode xmlNode) throws Exception {
        return (UINode) nodeTypes.get(type).getConstructors()[0].newInstance(styleSet, xmlNode);
    }

    private static StyleSet findStyleSet(Map<String, StyleSet> existingStyles, XMLNode node) {
        StyleSet set = existingStyles.getOrDefault(node.getTagName(), StyleSet.defaultStyleSet).clone();

        String id = node.getParam("class");
        StyleSet set2 = existingStyles.getOrDefault("." + id, null);
        if (set2 != null) set.overwrite(set2);

        id = node.getParam("id");
        set2 = existingStyles.getOrDefault("#" + id, null);
        if (set2 != null) set.overwrite(set2);

        return set;
    }

    private static final Pattern blockStart = Pattern.compile("([a-zA-Z\\.,_0-9#]+) ?\\{([^}]*)\\}", Pattern.MULTILINE);

    private static Map<String, StyleSet> getStyleSets(String css) throws Exception {
        Map<String, StyleSet> result = new HashMap<>();

        Matcher matcher = blockStart.matcher(css);
        while (matcher.find()) {
            result.put(matcher.group(1), new StyleSet(matcher.group(2)));
        }

        return result;
    }
}
