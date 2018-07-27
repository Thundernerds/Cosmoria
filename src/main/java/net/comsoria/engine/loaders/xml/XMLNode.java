package net.comsoria.engine.loaders.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XMLNode {
    public List<Parameter> parameters = new ArrayList<>();
    private final String tagName;
    public String innerText;
    public List<XMLNode> children;
    public final int parentStart;

    public XMLNode(String tagName, String innerText, int parentStart, List<XMLNode> children) {
        this.tagName = tagName;
        this.children = children;
        this.innerText = innerText;
        this.parentStart = parentStart;
    }

    public String getTagName() {
        return tagName;
    }

    public String getInnerText() {
        StringBuilder result = new StringBuilder();
        result.append(innerText);

        int offset = 0;
        for (XMLNode node : children) {
            String nodeXML = node.getInnerText();
            result.insert(node.parentStart + offset, nodeXML);
            offset += nodeXML.length();
        }

        return result.toString();
    }

    public String getOpener() {
        String text = "<" + tagName;
        for (Parameter parameter : parameters) {
            text += " " + parameter.name + "=\"" + parameter.value + "\"";
        }

        return text + ">";
    }

    public String getCloser() {
        return "</" + tagName + ">";
    }

    public String getInnerXML() {
        StringBuilder result = new StringBuilder();
        result.append(innerText);

        int offset = 0;
        for (XMLNode node : children) {
            String nodeXML = node.getOuterXML();
            result.insert(node.parentStart + offset, nodeXML);
            offset += nodeXML.length();
        }

        return result.toString();
    }
    public String getOuterXML() {
        return this.getOpener() + this.getInnerXML() + this.getCloser();
    }

    @Override
    public String toString() {
        return this.getOuterXML();
    }

    public boolean hasParam(String name) {
        for (Parameter parameter : parameters) {
            if (parameter.name.equals(name)) return true;
        }
        return false;
    }

    public String getParam(String name) {
        for (Parameter parameter : parameters) {
            if (parameter.name.equals(name)) return parameter.value;
        }

        return null;
    }

    public List<XMLNode> getAllChildren() {
        List<XMLNode> children = new ArrayList<>();
        for (XMLNode child : this.children) {
            children.add(child);
            children.addAll(child.getAllChildren());
        }
        return children;
    }

    public static class Parameter {
        final String name;
        final String value;

        public Parameter(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
