package net.comsoria.engine.view.Light;

import net.comsoria.engine.view.Color;

import java.util.ArrayList;
import java.util.List;

public class SceneLight {
    public final Color ambientLight = new Color().setTransparent(false);
    public List<PointLight> pointLightList = new ArrayList<>();
    public List<SpotLight> spotLightList = new ArrayList<>();
    public DirectionalLight directionalLight;
}
