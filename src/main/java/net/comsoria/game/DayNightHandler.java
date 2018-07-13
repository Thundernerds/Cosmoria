package net.comsoria.game;

import net.comsoria.engine.Color;
import net.comsoria.engine.view.Light.SceneLight;
import org.joml.Vector3f;

public class DayNightHandler {
    private final static Color day = new Color(102,150,186).getOneToZero();
    private final static Color night = new Color(23, 32, 42).getOneToZero();
    private final static Color sunset = new Color(230, 81, 0).getOneToZero();

    private final SkyDome skyDome;
    private final float dayStart;
    private final SceneLight sceneLight;

    private final float sunsetBand;
    private final float sunsetTime;

    public DayNightHandler(SkyDome skyDome, float dayStart, SceneLight sceneLight, float sunsetBand, float sunsetTime) throws Exception {
        this.skyDome = skyDome;
        this.dayStart = dayStart;
        this.sceneLight = sceneLight;
        this.sunsetBand = sunsetBand;
        this.sunsetTime = sunsetTime;
    }

    public void update(float time) {
        float cos = (float) Math.cos(time);
        float mod = (float) (time % (Math.PI * 2) / (Math.PI * 2));
        sceneLight.directionalLight.direction = new Vector3f((float) Math.sin(time), cos, 0);

        Color color = night;
        float ambient = 1;
        if (cos > dayStart) {
            float nCos = cos - dayStart;

            color = color.mix(day, nCos);
            ambient += nCos * 0.5;
        }

        sceneLight.ambientLight = Color.grayScale(ambient);

        float dist = Math.abs(mod - sunsetTime);
        Color secondary = color.mix(sunset, 1f - Math.min(Math.max(dist * (1f / sunsetBand), 0), 1));

        skyDome.setColor(color, secondary);
    }
}
