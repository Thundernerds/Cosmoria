package net.comsoria.engine.math;

public class Circle {
    public float radius = 1;

    public Circle() {

    }

    public Circle(float r) {
        this.radius = r;
    }

    public Line getTangent(float angle) {
        Line result = new Line();
        result.gradient = (float) -Math.tan(angle);
        result.yIntercept = (float) (this.radius * (Math.cos(angle) + (result.gradient * -Math.sin(angle))));
        return result;
    }
}
