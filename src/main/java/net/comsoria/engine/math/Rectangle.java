package net.comsoria.engine.math;

public class Rectangle {
    public int width;
    public int height;
    public int x;
    public int y;

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public Rectangle(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public Rectangle(int width, int height) {
        this(width, height, 0, 0);
    }
}
