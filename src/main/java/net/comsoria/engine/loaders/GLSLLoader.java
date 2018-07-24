package net.comsoria.engine.loaders;

import net.comsoria.engine.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GLSLLoader {
    public static String loadGLSL(String path, Map<String,String> constants) throws IOException {
        String text = FileLoader.loadResourceAsString(path);
        File file = new File(path);

        final String noLines = text.replace("\n", "");

        for (String key : constants.keySet()) {
            String value = constants.get(key);

            String pattern = "uniform ([a-zA-Z0-9]+) " + key + ";";
            Matcher m = Pattern.compile(pattern).matcher(noLines);
            if (m.find()) {
                text = text.replaceAll(pattern, "const " + m.group(1) + " " + key + " = " + value + ";");
            } else {
                pattern = "const ([a-zA-Z0-9]+) " + key + ";";
                m = Pattern.compile(pattern).matcher(noLines);
                if (m.find()) {
                    text = text.replaceAll(pattern, "const " + m.group(1) + " " + key + " = " + value + ";");
                } else {
                    Logger.log("Constant of name '" + key + "' not found in '" + file.getName() + "'", Logger.LogType.WARN);
                }
            }
        }

        return text;
    }
}
