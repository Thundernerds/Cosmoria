package net.comsoria.engine.view.GLSL.Programs;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.FadeFog;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
import net.comsoria.engine.view.Light.DirectionalLight;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

public class FadeOutShaderProgram extends ShaderProgram3D {
    private final String vertex;
    private final String fragment;

    public FadeOutShaderProgram(String vertex, String fragment) {
        super();

        this.vertex = vertex;
        this.fragment = fragment;
    }

    public void init() throws IOException {
        this.create(vertex, fragment);

        this.createUniform("projectionMatrix");
        this.createUniform("modelViewMatrix");
        this.createUniform("modelViewMatrixNoRot");

        this.createUniform("ambientLight");

        FadeFog.create(this, "fog");
        DirectionalLight.create(this, "directionalLight");

        this.createUniform("color1");
        this.createUniform("color2");
        this.createUniform("sunDirection");
    }

    @Override
    public void setupScene(Scene scene, Transformation transformation) {
        this.setUniform("projectionMatrix", transformation.getProjection());
        this.setUniform("ambientLight", scene.light.ambientLight.getVec3());

        this.setUniform("fog", scene.fog);

        DirectionalLight currDirLight = new DirectionalLight(scene.light.directionalLight);
        Vector4f dir = new Vector4f(currDirLight.direction, 0);
        dir.mul(transformation.getView());
        currDirLight.direction = new Vector3f(dir.x, dir.y, dir.z);
        this.setUniform("directionalLight", currDirLight);

        this.setUniform("color1", scene.sky.getMainColor().getVec3());
        this.setUniform("color2", scene.sky.getSecondColor().getVec3());
        this.setUniform("sunDirection", scene.light.directionalLight.direction);
    }

    @Override
    public void setupMesh(Mesh mesh, Matrix4f modelMatrix, Transformation transformation) {
        this.setUniform("modelViewMatrix", new Matrix4f(transformation.getView()).mul(modelMatrix));
        this.setUniform("modelViewMatrixNoRot", new Matrix4f(transformation.getViewNoRotation()).mul(modelMatrix));
    }
}
