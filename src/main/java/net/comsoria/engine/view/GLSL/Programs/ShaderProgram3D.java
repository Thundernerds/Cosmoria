package net.comsoria.engine.view.GLSL.Programs;

import net.comsoria.engine.Scene;
import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.view.FadeFog;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
import net.comsoria.engine.view.Light.DirectionalLight;
import net.comsoria.engine.view.Light.PointLight;
import net.comsoria.engine.view.Light.SpotLight;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;
import java.util.List;

public class ShaderProgram3D extends ShaderProgram {
    public ShaderProgram3D() {
        super();
    }

    public void init() throws IOException {
        this.create(FileLoader.loadResourceAsStringFromPath("$shaders/main/vertex.v.glsl"),
                FileLoader.loadResourceAsStringFromPath("$shaders/main/fragment.f.glsl"));

        this.createUniform("projectionMatrix");
        this.createUniform("modelViewMatrix");

        Material.create(this, "material");

        this.createUniform("specularPower");
        this.createUniform("ambientLight");

        for (int i = 0; i < 5; i++) {
            PointLight.create(this, "pointLights[" + i + "]");
        }

        for (int i = 0; i < 5; i++) {
            SpotLight.create(this, "spotLights[" + i + "]");
        }

        DirectionalLight.create(this, "directionalLight");

        FadeFog.create(this, "fog");
    }


    @Override
    public void setupScene(Scene scene, Transformation transformation) {
        this.setUniform("projectionMatrix", transformation.getProjection());

        this.setUniform("ambientLight", scene.light.ambientLight);
        this.setUniform("specularPower", 10f);

        List<PointLight> pointLightList = scene.light.pointLightList;
        int numLights = pointLightList != null ? pointLightList.size() : 0;
        for (int i = 0; i < numLights; i++) {
            PointLight currPointLight = new PointLight(pointLightList.get(i));

            Vector3f lightPos = currPointLight.position;
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(transformation.getView());

            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            currPointLight.set(this, "pointLights[" + i + "]");
        }

        List<SpotLight> spotLightList = scene.light.spotLightList;
        numLights = spotLightList != null ? spotLightList.size() : 0;

        for (int i = 0; i < numLights; i++) {
            SpotLight currSpotLight = new SpotLight(spotLightList.get(i));

            Vector4f dir = new Vector4f(currSpotLight.coneDirection, 0);
            dir.mul(transformation.getView());
            currSpotLight.coneDirection = new Vector3f(dir.x, dir.y, dir.z);

            Vector3f lightPos = currSpotLight.pointLight.position;
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(transformation.getView());
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            currSpotLight.set(this, "spotLights[" + i + "]");
        }

        DirectionalLight currDirLight = new DirectionalLight(scene.light.directionalLight);
        Vector4f dir = new Vector4f(currDirLight.direction, 0);
        dir.mul(transformation.getView());
        currDirLight.direction = new Vector3f(dir.x, dir.y, dir.z);
        this.setUniform("directionalLight", currDirLight);

        this.setUniform("fog", scene.fog);
    }

    @Override
    public void setupMesh(Mesh mesh, Matrix4f modelViewMatrix, Transformation transformation) {
        this.setUniform("modelViewMatrix", modelViewMatrix);
        this.setUniform("material", mesh.material);
    }
}
