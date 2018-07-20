package net.comsoria.engine.utils;

import net.comsoria.engine.loaders.FileLoader;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class JSONFile extends JSONObject {
    private final String path;

    public JSONFile(File file) throws IOException {
        this(file.toString());
    }

    public JSONFile(String path) throws IOException {
        super(FileLoader.loadResourceAsString(path));

        this.path = path;
    }

    public void save() throws IOException {
        FileLoader.writeResource(path, this.toString(4));
    }
}
