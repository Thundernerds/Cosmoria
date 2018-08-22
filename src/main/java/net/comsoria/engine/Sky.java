package net.comsoria.engine;

import net.comsoria.engine.view.color.Color3;
import net.comsoria.engine.view.color.Color4;
import net.comsoria.engine.view.graph.mesh.SkyBox;
import org.joml.Vector3f;

public class Sky {
    private final static Color3 day = new Color3(102,150,186).getOneToZero();
    private final static Color3 night = new Color3(23, 32, 42).getOneToZero();
    private final static Color3 sunset = new Color3(230, 81, 0).getOneToZero();

    private final Color3 mainColor = Color3.WHITE.clone();
    private final Color3 secondColor = Color3.WHITE.clone();
    private float ambience = 1.5f;
    private final Vector3f sunDirection = new Vector3f();

    private final float dayStart;
    private final float sunsetTime;
    private final float sunsetBand;

    public Sky(float dayStart, float sunsetTime, float sunsetBand) {
        this.dayStart = dayStart;
        this.sunsetTime = sunsetTime;
        this.sunsetBand = sunsetBand;
    }

    public void calcColors(float time) {
        float cos = (float) Math.cos(time);
        float mod = (float) (time % (Math.PI * 2) / (Math.PI * 2));
        this.sunDirection.set((float) Math.sin(time), cos, 0);

        mainColor.set(night);
        ambience = 2f;
        if (cos > dayStart) {
            float nCos = cos - dayStart;

            mainColor.set(mainColor.mix(day, nCos));
            ambience += nCos * 0.1;
        }

        float dist = Math.abs(mod - sunsetTime);
        this.secondColor.set(mainColor.mix(sunset, 1f - Math.min(Math.max(dist * (1f / sunsetBand), 0), 1)));
    }

    public Color3 getMainColor() {
        return mainColor;
    }

    public Color3 getSecondColor() {
        return secondColor;
    }

    public float getAmbience() {
        return ambience;
    }

    public Vector3f getSunDirection() {
        return sunDirection;
    }
}
