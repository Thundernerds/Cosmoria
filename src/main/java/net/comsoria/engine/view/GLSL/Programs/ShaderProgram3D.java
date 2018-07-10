package net.comsoria.engine.view.GLSL.Programs;

import net.comsoria.Utils;
import net.comsoria.engine.Scene;
import net.comsoria.engine.view.Fog;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.Light.DirectionalLight;
import net.comsoria.engine.view.Light.PointLight;
import net.comsoria.engine.view.Light.SpotLight;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class ShaderProgram3D extends ShaderProgram {
    public ShaderProgram3D() throws Exception {
        super();
    }

    public void init() throws Exception {
        this.create(Utils.loadResourceAsString("$vertex"), Utils.loadResourceAsString("$fragment"));

        this.createUniform("projectionMatrix");
        this.createUniform("modelViewMatrix");

        this.createMaterialUniform("material");

        this.createUniform("specularPower");
        this.createUniform("ambientLight");

        this.createPointLightListUniform("pointLights", 5);
        this.createSpotLightListUniform("spotLights", 5);
        this.createDirectionalLightUniform("directionalLight");

        this.createFogUniform("fog");
    }

    protected void createFogUniform(String name) throws Exception {
        this.createUniform(name + ".density");
        this.createUniform(name + ".start");
    }

    public void createPointLightListUniform(String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            createPointLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createPointLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }

    public void createSpotLightListUniform(String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            createSpotLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createSpotLightUniform(String uniformName) throws Exception {
        createPointLightUniform(uniformName + ".pl");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }

    public void setUniform(String uniformName, PointLight pointLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", pointLight);
    }

    public void createDirectionalLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }


    public void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".colour", pointLight.color.getVec3());
        setUniform(uniformName + ".position", pointLight.position);
        setUniform(uniformName + ".intensity", pointLight.intensity);
        PointLight.Attenuation att = pointLight.attenuation;
        setUniform(uniformName + ".att.constant", att.constant);
        setUniform(uniformName + ".att.linear", att.linear);
        setUniform(uniformName + ".att.exponent", att.exponent);
    }

    public void setUniform(String uniformName, SpotLight spotLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }

    public void setUniform(String uniformName, SpotLight spotLight) {
        setUniform(uniformName + ".pl", spotLight.pointLight);
        setUniform(uniformName + ".conedir", spotLight.coneDirection);
        setUniform(uniformName + ".cutoff", spotLight.cutOff);
    }

    public void setUniform(String uniformName, DirectionalLight dirLight) {
        setUniform(uniformName + ".colour", dirLight.color.getVec3());
        setUniform(uniformName + ".direction", dirLight.direction);
        setUniform(uniformName + ".intensity", dirLight.intensity);
    }

    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".ambient", material.ambientColour);
        setUniform(uniformName + ".diffuse", material.diffuseColour);
        setUniform(uniformName + ".specular", material.specularColour);
        setUniform(uniformName + ".reflectance", material.reflectance);
    }

    public void setUniform(String name, Fog fog) {
        this.setUniform(name + ".density", fog.density);
        this.setUniform(name + ".start", fog.start);
    }


    @Override
    public void setupScene(Scene scene, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        this.setUniform("projectionMatrix", projectionMatrix);

        this.setUniform("ambientLight", scene.light.ambientLight.getVec3());
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

            this.setUniform("pointLights", currPointLight, i);
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

            this.setUniform("spotLights", currSpotLight, i);
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
