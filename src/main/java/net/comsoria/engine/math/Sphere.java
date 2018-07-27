package net.comsoria.engine.math;

import org.joml.Vector3f;

public class Sphere {
    private Circle circle1;
    private Circle circle2;

    public Sphere(float radius) {
        this.circle1 = new Circle(radius);
        this.circle2 = new Circle(radius);
    }

    public Plane tangent(Vector3f angle) {
        Line tangent1 = this.circle1.getTangent((float) Math.atan2(angle.x, angle.y));
        Line tangent2 = this.circle2.getTangent((float) Math.atan2(angle.x, angle.z));

        return new Plane();
    }
}
