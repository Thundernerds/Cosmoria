package net.comsoria.game.terrain.mesh;

import net.comsoria.engine.Scene;
import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.loaders.GLSLLoader;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.Fog;
import net.comsoria.engine.view.GLSL.Programs.ShaderProgram3D;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.Light.DirectionalLight;
import net.comsoria.engine.view.graph.mesh.Mesh;
import net.comsoria.game.SkyDome;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChunkShaderProgram extends ShaderProgram3D {
    public Map<String, String> constants;

    public ChunkShaderProgram(Map<String, String> constants) {
        super();

        this.constants = constants;
    }

    public ChunkShaderProgram() {
        this(new HashMap<>());
    }

    public void init() throws IOException {
        this.create(GLSLLoader.loadGLSL(Utils.utils.p("$shaders/chunk/chunk_vertex.v.glsl"), constants),
                GLSLLoader.loadGLSL(Utils.utils.p("$shaders/chunk/chunk_fragment.f.glsl"), constants));

        this.createUniform("projectionMatrix");
        this.createUniform("modelViewMatrix");
        this.createUniform("modelViewMatrixNoRot");

        this.createUniform("ambientLight");

        Fog.create(this, "fog");
        DirectionalLight.create(this, "directionalLight");

        this.createUniform("color1");
        this.createUniform("color2");
        this.createUniform("sunDirection");
    }

    @Override
    public void setupScene(Scene scene, Transformation transformation) {
        this.setUniform("projectionMatrix", transformation.projection);
        this.setUniform("ambientLight", scene.light.ambientLight.getVec3());

        this.setUniform("fog", scene.fog);

        DirectionalLight currDirLight = new DirectionalLight(scene.light.directionalLight);
        Vector4f dir = new Vector4f(currDirLight.direction, 0);
        dir.mul(transformation.view);
        currDirLight.direction = new Vector3f(dir.x, dir.y, dir.z);
        this.setUniform("directionalLight", currDirLight);

        this.setUniform("color1", scene.sky.getMainColor().getVec3());
        this.setUniform("color2", scene.sky.getSecondColor().getVec3());
        this.setUniform("sunDirection", scene.light.directionalLight.direction);

//        this.setUniform("modelViewMatrix", transformation.view);
//        this.setUniform("viewMatrixNoRot", transformation.viewNoRotation);
    }

    @Override
    public void setupMesh(Mesh mesh, Matrix4f modelMatrix, Transformation transformation) {
        this.setUniform("modelViewMatrix", new Matrix4f(transformation.view).mul(modelMatrix));
        this.setUniform("modelViewMatrixNoRot", new Matrix4f(transformation.viewNoRotation).mul(modelMatrix));
    }
}
