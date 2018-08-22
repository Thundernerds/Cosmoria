package net.comsoria.engine.view.graph;

import net.comsoria.engine.view.color.Color4;
import net.comsoria.engine.view.GLSL.GLSLUniformBindable;
import net.comsoria.engine.view.GLSL.ShaderProgram;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glDeleteTextures;

public class Material implements GLSLUniformBindable {
    private static final Color4 DEFAULT_COLOUR = Color4.WHITE;
    public Color4 ambientColour;
    public Color4 diffuseColour;
    public Color4 specularColour;
    public float reflectance;
    public List<Texture> textures;

    public Material() {
        this.ambientColour = DEFAULT_COLOUR.clone();
        this.diffuseColour = DEFAULT_COLOUR.clone();
        this.specularColour = DEFAULT_COLOUR.clone();
        this.textures = new ArrayList<>();
        this.reflectance = 0;
    }

    public Material(Color4 colour, float reflectance) {
        this(colour, colour, colour, null, reflectance);
    }

    public Material(Texture texture) {
        this(DEFAULT_COLOUR.clone(), DEFAULT_COLOUR.clone(), DEFAULT_COLOUR.clone(), texture, 0);
    }

    public Material(Texture texture, float reflectance) {
        this(DEFAULT_COLOUR.clone(), DEFAULT_COLOUR.clone(), DEFAULT_COLOUR.clone(), texture, reflectance);
    }

    public Material(Color4 ambientColour, Color4 diffuseColour, Color4 specularColour, Texture texture, float reflectance) {
        this.ambientColour = ambientColour;
        this.diffuseColour = diffuseColour;
        this.specularColour = specularColour;

        this.textures = new ArrayList<>();
        if (texture != null) this.textures.add(texture);

        this.reflectance = reflectance;
    }

    public void cleanup() {
        for (Texture texture : textures) texture.cleanup();
    }

    public boolean isTextured() {
        return this.textures.size() != 0 && this.textures.get(0) != null;
    }

    @Override
    public void set(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name + ".ambient", this.ambientColour);
        shaderProgram.setUniform(name + ".diffuse", this.diffuseColour);
        shaderProgram.setUniform(name + ".specular", this.specularColour);
        shaderProgram.setUniform(name + ".reflectance", this.reflectance);
    }

    public static void create(ShaderProgram shaderProgram, String name) {
        shaderProgram.createUniform(name + ".ambient");
        shaderProgram.createUniform(name + ".diffuse");
        shaderProgram.createUniform(name + ".specular");
        shaderProgram.createUniform(name + ".hasTexture");
        shaderProgram.createUniform(name + ".reflectance");
    }
}
