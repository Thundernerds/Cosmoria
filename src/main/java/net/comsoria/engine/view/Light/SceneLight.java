package net.comsoria.engine.view.Light;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SceneLight {
    public Vector3f ambientLight;
    public List<PointLight> pointLightList = new ArrayList<>();
    public List<SpotLight> spotLightList = new ArrayList<>();
    public DirectionalLight directionalLight;
}
