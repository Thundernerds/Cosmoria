package net.comsoria.engine.view.GLSL.Programs.custom;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Matrix4f;

public interface IExtractSceneData {
    void extractScene(Scene scene, ShaderProgram shaderProgram, Matrix4f projMatrix, Matrix4f viewMatrix);
    void extractMesh(Mesh mesh, ShaderProgram shaderProgram, Matrix4f matrix);
}
