package net.comsoria.engine.view.Light;

import org.joml.Vector3f;

public class SpotLight {
    public PointLight pointLight;
    public Vector3f coneDirection;
    public float cutOff;

    public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle) {
        this.pointLight = pointLight;
        this.coneDirection = coneDirection;
        setCutOffAngle(cutOffAngle);
    }

    public SpotLight(SpotLight spotLight) {
        this(new PointLight(spotLight.pointLight), new Vector3f(spotLight.coneDirection), 0);
        this.cutOff = spotLight.cutOff;
    }

    public final void setCutOffAngle(float cutOffAngle) {
        this.cutOff = (float) Math.cos(Math.toRadians(cutOffAngle));
    }
}
