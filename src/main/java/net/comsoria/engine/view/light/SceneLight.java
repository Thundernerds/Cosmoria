package net.comsoria.engine.view.light;

import net.comsoria.engine.view.color.Color3;

import java.util.ArrayList;
import java.util.List;

public class SceneLight {
    public final Color3 ambientLight = new Color3();
    public List<PointLight> pointLightList = new ArrayList<>();
    public List<SpotLight> spotLightList = new ArrayList<>();
    public DirectionalLight directionalLight;
}
