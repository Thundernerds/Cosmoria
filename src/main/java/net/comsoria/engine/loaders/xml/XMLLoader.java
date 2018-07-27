package net.comsoria.engine.loaders.xml;

import net.comsoria.engine.loaders.LoaderException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLLoader {
    private final static Pattern opening = Pattern.compile("<([A-Za-z0-9_]+)(( [^=^ ]+=\"[^\"]+\")*)>");
    private final static Pattern closing = Pattern.compile("</([A-Za-z0-9_]+)>");
    private final static Pattern element = Pattern.compile("<[^>]+>");
    private final static Pattern param = Pattern.compile("([^=^ ]+) ?= ?\"([^\"]+)\"");

    public static XMLNode loadXML(String xml) {
        Matcher elementMatcher = element.matcher(xml);

        List<GeneratedNode> found = new ArrayList<>();
        List<UnparentedNode> unparentedNodes = new ArrayList<>();

        int layer = 0;
        int lastIndex = 0;
        while (elementMatcher.find()) {
            String toAdd = xml.substring(lastIndex, elementMatcher.start());
            for (GeneratedNode generatedNode : found) {
                if (layer - 1 == generatedNode.layer) {
                    generatedNode.text += toAdd;
                }
            }

            lastIndex = elementMatcher.end();

            Matcher matcher = opening.matcher(elementMatcher.group(0));

            if (matcher.find()) { //Opening
                int others = 0;
                for (UnparentedNode unparentedNode : unparentedNodes) {
                    if (unparentedNode.layer == layer)
                        others += unparentedNode.node.toString().length();

                }

                found.add(new GeneratedNode(
                        matcher.group(1),
                        matcher.group(2),
                        layer,
                        found.size() == 0? 0:found.get(found.size() - 1).index,
                        lastIndex,
                        matcher.group().length(),
                        others
                ));

                layer += 1;
            } else {
                matcher = closing.matcher(elementMatcher.group(0));

                GeneratedNode last = found.get(found.size() - 1);
                if (matcher.find() && matcher.group(1).equals(last.tagName)) { //Closing
                    UnparentedNode unparentedNode = new UnparentedNode(
                            last.layer,
                            last.getNode()
                    );
                    found.remove(last);

                    for (UnparentedNode child : new ArrayList<>(unparentedNodes)) {
                        if (child.layer > last.layer) {
                            unparentedNode.node.children.add(child.node);
                            unparentedNodes.remove(child);
                        }
                    }

                    unparentedNodes.add(unparentedNode);

                    layer -= 1;
                } else { //Error
                    throw new LoaderException("Invalid syntax in XML around '" + elementMatcher.group(0) + "'");
                }
            }
        }

        return unparentedNodes.get(0).node;
    }

    private static class GeneratedNode {
        String tagName;
        String params;
        int layer;
        String text = "";
        int parentStart;
        private final int index;

        GeneratedNode(String tagName, String params, int layer, int lastIndex, int index, int size, int others) {
            this.tagName = tagName;
            this.params = params;
            this.layer = layer;
            this.parentStart = index - lastIndex - size - others;
            this.index = index;
        }

        XMLNode getNode() {
            XMLNode xmlNode = new XMLNode(this.tagName, text, parentStart, new ArrayList<>());

            Matcher paramMatcher = param.matcher(this.params);
            while (paramMatcher.find()) {
                xmlNode.parameters.add(new XMLNode.Parameter(paramMatcher.group(1), paramMatcher.group(2)));
            }

            return xmlNode;
        }
    }

    private static class UnparentedNode {
        int layer;
        XMLNode node;

        public UnparentedNode(int layer, XMLNode node) {
            this.layer = layer;
            this.node = node;
        }
    }
}
