package net.comsoria.game;

import net.comsoria.engine.Scene;
import net.comsoria.engine.Tuple;
import net.comsoria.engine.loaders.OBJLoader;
import net.comsoria.engine.Color;
import net.comsoria.engine.view.GLSL.Programs.custom.CustomShaderProgram;
import net.comsoria.engine.view.GLSL.Programs.custom.IExtractSceneData;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.BufferAttribute;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.Texture;
import net.comsoria.engine.view.graph.mesh.Mesh;
import net.comsoria.engine.view.graph.mesh.SkyBox;
import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.List;

public class SkyDome {
    private final Mesh dome;

    public SkyDome(String fragment, String vertex, float size, Texture sun) throws Exception {
        Tuple<List<BufferAttribute>, int[]> data = OBJLoader.loadGeometry("$skydomeobj");
        data.getA().remove(1);
        data.getA().remove(1);
        dome = new SkyBox(new Geometry(data), new Material());
        dome.material.shaderProgram = new CustomShaderProgram(fragment, vertex, Arrays.asList("color1", "color2", "modelViewMatrix", "projectionMatrix"), Arrays.asList(), new IExtractSceneData() {
            @Override public void extractScene(Scene scene, ShaderProgram shaderProgram, Matrix4f projMatrix, Matrix4f viewMatrix) {
                shaderProgram.setUniform("projectionMatrix", projMatrix);
            }

            @Override public void extractMesh(Mesh mesh, ShaderProgram shaderProgram, Matrix4f matrix) {
                shaderProgram.setUniform("modelViewMatrix", matrix);
            }
        });
        dome.scale = size;
        dome.initShaderProgram();
        dome.geometry.bind();
        dome.material.textures.add(sun);
        dome.material.shaderProgram.createTextureUniform("sun");
        dome.geometry.unbind();
    }

    public void setColor(Color main, Color second) {
        dome.material.shaderProgram.bind();
        dome.material.shaderProgram.setUniform("color1", main.getVec3());
        dome.material.shaderProgram.setUniform("color2", second.getVec3());
        dome.material.shaderProgram.unbind();
    }

    public Mesh getGameObject() {
        return dome;
    }
}
