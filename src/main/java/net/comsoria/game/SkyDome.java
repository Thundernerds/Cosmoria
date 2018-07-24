package net.comsoria.game;

import net.comsoria.engine.Scene;
import net.comsoria.engine.utils.Tuple;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.loaders.OBJLoader;
import net.comsoria.engine.view.GLSL.Programs.CustomShaderProgram;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.graph.BufferAttribute;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.Texture;
import net.comsoria.engine.view.graph.mesh.Mesh;
import net.comsoria.engine.view.graph.mesh.SkyBox;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SkyDome {
    public static Mesh genSkyDome(String fragment, String vertex, float size, Texture sun) throws IOException {
        Tuple<List<BufferAttribute>, int[]> data = OBJLoader.loadGeometry(Utils.utils.p("$models/skydome.obj"));
        data.getA().remove(1);
        data.getA().remove(1);
        Mesh dome = new SkyBox(new Geometry(data), new Material());

        dome.material.shaderProgram = new CustomShaderProgram(fragment, vertex, Arrays.asList("color1", "color2", "modelViewMatrix", "projectionMatrix", "sunDirection", "sunLine"), Arrays.asList("sun")) {
            @Override
            public void setupScene(Scene scene, Transformation transformation) {
                this.setUniform("projectionMatrix", transformation.projection);
                Vector3f direction = scene.light.directionalLight.direction;
                this.setUniform("sunDirection", direction);
//                shaderProgram.setUniform("sunLine", new Circle().getTangent((float) Math.atan2(direction.x, direction.y)));

                this.setUniform("color1", scene.sky.getMainColor().getVec3());
                this.setUniform("color2", scene.sky.getSecondColor().getVec3());
            }

            @Override
            public void setupMesh(Mesh mesh, Matrix4f modelMatrix, Transformation transformation) {
                this.setUniform("modelViewMatrix", modelMatrix);
            }
        };

        dome.scale = size;
        dome.initShaderProgram();
        dome.material.textures.add(sun);

        return dome;
    }
}
