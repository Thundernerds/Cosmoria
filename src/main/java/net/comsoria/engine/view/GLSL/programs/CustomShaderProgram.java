package net.comsoria.engine.view.GLSL.programs;

import net.comsoria.engine.view.GLSL.ShaderProgram;

import java.util.List;

public abstract class CustomShaderProgram extends ShaderProgram {
    private final String vertex;
    private final String fragment;
    private final List<String> uniforms;

    public CustomShaderProgram(String vertex, String fragment, List<String> uniforms, List<String> textures) {
        super();

        this.vertex = vertex;
        this.fragment = fragment;
        this.uniforms = uniforms;
        this.textures.addAll(textures);
    }

    public CustomShaderProgram(String vertex, String fragment, List<String> uniforms) {
        super();

        this.vertex = vertex;
        this.fragment = fragment;
        this.uniforms = uniforms;
    }

    @Override
    public void init() {
        this.create(vertex, fragment);
        for (String name : uniforms) this.createUniform(name);
        for (String name : textures) this.createUniform(name);
    }
}
