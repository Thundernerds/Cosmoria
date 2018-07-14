package net.comsoria.engine.view.GLSL.Programs;

import net.comsoria.Utils;
import net.comsoria.engine.Scene;
import net.comsoria.engine.view.Fog;
import net.comsoria.engine.view.GLSL.GLSLUniformBindable;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.Light.DirectionalLight;
import net.comsoria.engine.view.Light.PointLight;
import net.comsoria.engine.view.Light.SpotLight;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;
import java.util.List;

public class ShaderProgram3D extends ShaderProgram {
    public ShaderProgram3D() throws Exception {
        super();
    }

    public void init() throws Exception {
        this.create(Utils.loadResourceAsString("$vertex"), Utils.loadResourceAsString("$fragment"));

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

        Fog.create(this, "fog");
    }


    @Override
    public void setupScene(Scene scene, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        this.setUniform("projectionMatrix", projectionMatrix);

        this.setUniform("ambientLight", scene.light.ambientLight);
        this.setUniform("specularPower", 10f);

        List<PointLight> pointLightList = scene.light.pointLightList;
        int numLights = pointLightList != null ? pointLightList.size() : 0;
        for (int i = 0; i < numLights; i++) {
            PointLight currPointLight = new PointLight(pointLightList.get(i));

            Vector3f lightPos = currPointLight.position;
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);

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
            dir.mul(viewMatrix);
            currSpotLight.coneDirection = new Vector3f(dir.x, dir.y, dir.z);

            Vector3f lightPos = currSpotLight.pointLight.position;
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            currSpotLight.set(this, "spotLights[" + i + "]");
        }

        DirectionalLight currDirLight = new DirectionalLight(scene.light.directionalLight);
        Vector4f dir = new Vector4f(currDirLight.direction, 0);
        dir.mul(viewMatrix);
        currDirLight.direction = new Vector3f(dir.x, dir.y, dir.z);
        this.setUniform("directionalLight", currDirLight);

        this.setUniform("fog", scene.fog);
    }

    @Override
    public void setupMesh(Mesh mesh, Matrix4f modelViewMatrix) {
        this.setUniform("modelViewMatrix", modelViewMatrix);
        this.setUniform("material", mesh.material);
    }
}
