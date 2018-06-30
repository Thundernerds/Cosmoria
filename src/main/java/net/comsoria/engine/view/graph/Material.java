package net.comsoria.engine.view.graph;

import net.comsoria.engine.view.GLSL.ShaderProgram;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glDeleteTextures;

public class Material {
    private static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    public Vector4f ambientColour;
    public Vector4f diffuseColour;
    public Vector4f specularColour;
    public float reflectance;
    public List<Texture> textures;
    public ShaderProgram shaderProgram = null;

    public Material() {
        this.ambientColour = DEFAULT_COLOUR;
        this.diffuseColour = DEFAULT_COLOUR;
        this.specularColour = DEFAULT_COLOUR;
        this.textures = new ArrayList<>();
        this.reflectance = 0;
    }

    public Material(Vector4f colour, float reflectance) {
        this(colour, colour, colour, null, reflectance);
    }

    public Material(Texture texture) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, 0);
    }

    public Material(Texture texture, float reflectance) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, reflectance);
    }

    public Material(Vector4f ambientColour, Vector4f diffuseColour, Vector4f specularColour, Texture texture, float reflectance) {
        this.ambientColour = ambientColour;
        this.diffuseColour = diffuseColour;
        this.specularColour = specularColour;

        this.textures = new ArrayList<>();
        if (texture != null) this.textures.add(texture);

        this.reflectance = reflectance;
    }

    public void cleanup() {
        for (Texture texture : textures) texture.cleanup();
        shaderProgram.cleanup();
    }

    public boolean isTextured() {
        return this.textures.size() != 0 && this.textures.get(0) != null;
    }
}
