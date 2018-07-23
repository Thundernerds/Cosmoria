package net.comsoria.engine.loaders;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GLSLLoader {
    public static String loadGLSL(String path, Map<String,String> constants) throws IOException {
        String text = FileLoader.loadResourceAsString(path);

        for (String key : constants.keySet()) {
            String value = constants.get(key);

            String pattern = "uniform ([a-zA-Z0-9]+) " + key + ";";
            Matcher m = Pattern.compile(pattern).matcher(text.replace("\n", ""));
            if (m.find()) {
                text = text.replaceAll(pattern, "const " + m.group(1) + " " + key + " = " + value + ";");
            }
        }

        return text;
    }
}
