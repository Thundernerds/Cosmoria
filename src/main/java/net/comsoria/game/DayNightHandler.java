package net.comsoria.game;

import net.comsoria.engine.Color;
import net.comsoria.engine.view.Light.SceneLight;
import org.joml.Vector3f;

public class DayNightHandler {
    private final static Color day = new Color(116, 197, 243).getOneToZero();
    private final static Color night = new Color(23, 32, 42).getOneToZero();

    private final SkyDome skyDome;
    private final float dayStart;
    private final SceneLight sceneLight;

    public DayNightHandler(SkyDome skyDome, float dayStart, SceneLight sceneLight) throws Exception {
        this.skyDome = skyDome;
        this.dayStart = dayStart;
        this.sceneLight = sceneLight;
    }

    public void update(float time) {
        float cos = (float) Math.cos(time);
        sceneLight.directionalLight.direction = new Vector3f((float) Math.sin(time), cos, 0);

        Color color;
        float ambient;
        if (cos < dayStart) {
            color = night;
            ambient = 0;
        } else {
            cos = (cos - dayStart) * 0.08f;
            color = day.mix(night, 1 - cos);
            ambient = cos;
        }

        sceneLight.ambientLight = Color.grayScale(ambient + 0.7f);
        skyDome.setColor(color, Color.WHITE);
    }
}
