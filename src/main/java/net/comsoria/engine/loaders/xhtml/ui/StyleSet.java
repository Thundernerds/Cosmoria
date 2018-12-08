package net.comsoria.engine.loaders.xhtml.ui;

import net.comsoria.engine.utils.Logger;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.color.Color3;
import net.comsoria.engine.view.color.Color4;
import net.comsoria.engine.view.graph.Texture;
import net.comsoria.engine.view.graph.mesh.Mesh;
import net.comsoria.engine.view.graph.mesh.Mesh2D;
import org.joml.Vector2f;

import javax.swing.text.Style;
import java.util.*;

public class StyleSet {
    public final static StyleSet defaultStyleSet = new StyleSet();

    public Map<String, StyleRule> ruleMap = new HashMap<>();

    private void initRuleMap() {
        ruleMap.put("z-index", new StyleRule("0") {
            @Override void modifyMesh(Mesh mesh, Window window, String value) throws Exception {
                mesh.position.z = Float.valueOf(value);
            }
        });
        ruleMap.put("position", new StyleRule("0 0") {
            @Override
            void modifyMesh(Mesh mesh, Window window, String value) throws Exception {
                String[] parts = value.split(" ");
                mesh.position.x = (Float.valueOf(parts[0]) / 100f) * window.getWidth();
                mesh.position.y = (Float.valueOf(parts[1]) / 100f) * window.getHeight();
            }
        });
        ruleMap.put("color", new StyleRule("0 0 0") {
            @Override
            void modifyMesh(Mesh mesh, Window window, String value) throws Exception {
                String[] parts = value.split(" ");

                mesh.material.ambientColour.set(Color3.valueOf(value));
                if (parts.length == 4) mesh.material.ambientColour.setA(Float.valueOf(parts[3]));
            }
        });
        ruleMap.put("scale", new StyleRule("1") {
            @Override
            void modifyMesh(Mesh mesh, Window window, String value) throws Exception {
                mesh.scale = Float.valueOf(value);
            }
        });
        ruleMap.put("rotation", new StyleRule("0") {
            @Override
            void modifyMesh(Mesh mesh, Window window, String value) throws Exception {
                mesh.rotation.z = (float) ((Math.PI * 2) * Float.valueOf(value));
            }
        });
        ruleMap.put("width", new StyleRule("0"));
        ruleMap.put("height", new StyleRule("0"));
        ruleMap.put("font", new StyleRule("$fonts/spaceAge.ttf"));
    }

    public StyleSet(String text) throws Exception {
        this.initRuleMap();

        String[] lines = text.replace("\n", "").split(";");

        for (String line : lines) {
            if (line.trim().equals("")) continue;

            String[] parts = line.split(":");
            parts[0] = parts[0].trim();
            parts[1] = parts[1].trim();

            if (ruleMap.containsKey(parts[0])) {
                StyleRule rule = ruleMap.get(parts[0]);
                rule.value = parts[1];
                rule.written = true;
            }
            else Logger.log("Failed to find style rule of name '" + parts[0] + "'", Logger.LogType.WARN);
        }
    }

    public StyleSet() {
        this.initRuleMap();
    }

    public StyleSet(Map<String, StyleRule> map) {
        for (String key : map.keySet()) {
            ruleMap.put(key, map.get(key).clone());
        }
    }

    public void overwrite(StyleSet styleSet) {
        for (String key : styleSet.ruleMap.keySet()) {
            StyleRule rule = styleSet.ruleMap.get(key);
            if (rule.written) this.ruleMap.get(key).set(rule);
        }
    }

    @Override
    public String toString() {
        String text = "";
        for (String key : this.ruleMap.keySet()) {
            StyleRule rule = this.ruleMap.get(key);
            text += key + ": " + rule.value + ": " + rule.written + "\n";
        }
        return text.trim();
    }

    public StyleSet clone() {
        return new StyleSet(this.ruleMap);
    }

    public static class StyleRule {
        public String value = null;
        public boolean written = false;

        public StyleRule() {

        }

        public StyleRule(String value) {
            this.value = value;
        }

        public void updateMesh(Mesh mesh, Window window) throws Exception {
            if (value != null) this.modifyMesh(mesh, window, this.value);
        }

        void modifyMesh(Mesh mesh, Window window, String value) throws Exception {

        }

        void set(StyleRule rule2) {
            this.value = rule2.value;
            this.written = rule2.written;
        }

        public void setValue(String value) {
            this.value = value;
            this.written = true;
        }
        public void setValue(Object value) {
            this.value = value.toString();
            this.written = true;
        }
        public void setValue(float value) {
            this.value = String.valueOf(value);
            this.written = true;
        }

        protected StyleRule clone() {
            return new StyleRule(this.value) {
                @Override
                void modifyMesh(Mesh mesh, Window window, String value) throws Exception {
                    StyleRule.this.modifyMesh(mesh, window, value);
                }
            };
        }
    }
}
