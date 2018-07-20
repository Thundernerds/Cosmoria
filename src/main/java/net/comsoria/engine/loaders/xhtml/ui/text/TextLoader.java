package net.comsoria.engine.loaders.xhtml.ui.text;

import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.GLSL.Programs.ShaderProgram2D;
import net.comsoria.engine.view.graph.BufferAttribute;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.mesh.Mesh;
import net.comsoria.engine.view.graph.mesh.Mesh2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextLoader {
    private static final float ZPOS = 0.0f;
    private static final int VERTICES_PER_QUAD = 4;

    public static Mesh buildMesh(String text, FontTexture texture) throws IOException {
        List<Float> positions = new ArrayList<>();
        List<Float> textCoords = new ArrayList<>();
        List<Integer> indices   = new ArrayList<>();
        char[] characters = text.toCharArray();
        int numChars = characters.length;

        float startx = 0;
        for(int i=0; i<numChars; i++) {
            FontTexture.CharInfo charInfo = texture.getCharInfo(characters[i]);

            // Build a character tile composed by two triangles

            // Left Top vertex
            positions.add(startx); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add( (float)charInfo.getStartX() / (float)texture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD);

            // Left Bottom vertex
            positions.add(startx); // x
            positions.add((float)texture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float)charInfo.getStartX() / (float)texture.getWidth());
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 1);

            // Right Bottom vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add((float)texture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)texture.getWidth());
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 2);

            // Right Top vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)texture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD + 3);

            // Add indices por left top and bottom right vertices
            indices.add(i*VERTICES_PER_QUAD);
            indices.add(i*VERTICES_PER_QUAD + 2);

            startx += charInfo.getWidth();
        }

        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();

        BufferAttribute pos = new BufferAttribute(Utils.listToArray(positions), 3);
        BufferAttribute texCoords = new BufferAttribute(Utils.listToArray(textCoords), 2);

        Geometry geometry = new Geometry(new ArrayList<>(Arrays.asList(pos, texCoords)), indicesArr);

        Mesh mesh = new Mesh2D(geometry, new Material());
        mesh.material.shaderProgram = new ShaderProgram2D();
        mesh.initShaderProgram();
        mesh.material.textures.add(texture.getTexture());
        mesh.material.shaderProgram.createTextureUniform("texture_sampler");
        mesh.material.ambientColour.set(1, 1, 1, 1);

        return mesh;
    }
}
