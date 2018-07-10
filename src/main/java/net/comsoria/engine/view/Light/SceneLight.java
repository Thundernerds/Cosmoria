package net.comsoria.engine.view.Light;

import net.comsoria.engine.Color;

import java.util.ArrayList;
import java.util.List;

public class SceneLight {
    public Color ambientLight;
    public List<PointLight> pointLightList = new ArrayList<>();
    public List<SpotLight> spotLightList = new ArrayList<>();
    public DirectionalLight directionalLight;
}
