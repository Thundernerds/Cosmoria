package net.comsoria.engine.view.GLSL.Programs.custom;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;

public class CustomShaderProgram extends ShaderProgram {
    private final String vertex;
    private final String fragment;
    private final List<String> uniforms;
    private final IExtractSceneData extractSceneData;

    public CustomShaderProgram(String vertex, String fragment, List<String> uniforms, List<String> textures, IExtractSceneData extractSceneData) throws Exception {
        super();

        this.vertex = vertex;
        this.fragment = fragment;
        this.uniforms = uniforms;
        this.extractSceneData = extractSceneData;
        this.textures.addAll(textures);
    }

    public CustomShaderProgram(String vertex, String fragment, List<String> uniforms, IExtractSceneData extractSceneData) throws Exception {
        super();

        this.vertex = vertex;
        this.fragment = fragment;
        this.uniforms = uniforms;
        this.extractSceneData = extractSceneData;
    }

    @Override
    public void init() throws Exception {
        this.create(vertex, fragment);
        for (String name : uniforms) this.createUniform(name);
        for (String name : textures) this.createUniform(name);
    }

    @Override
    public void setupMesh(Mesh mesh, Matrix4f modelMatrix) {
        extractSceneData.extractMesh(mesh, this, modelMatrix);
    }

    @Override
    public void setupScene(Scene scene, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        extractSceneData.extractScene(scene, this, projectionMatrix, viewMatrix);
    }
}
