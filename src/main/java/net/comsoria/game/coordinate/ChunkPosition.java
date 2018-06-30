package net.comsoria.game.coordinate;

import org.joml.Vector2f;

public class ChunkPosition {
    private int x;
    private int y;

    public ChunkPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) return false;

        ChunkPosition objCast = (ChunkPosition) obj;
        return objCast.x == this.x && objCast.y == this.y;
    }

    public static ChunkPosition toChunkPosition(int chunkSize, Vector2f worldPosition) {
        return new ChunkPosition((int) worldPosition.x / chunkSize, (int) worldPosition.y / chunkSize);
    }

    public double distanceTo(ChunkPosition other) {
        int xDist = Math.abs(this.x - other.getX());
        int yDist = Math.abs(this.y - other.getY());
        return Math.sqrt((xDist * xDist) + (yDist * yDist));
    }

    @Override
    public String toString() {
        return "ChunkPosition[x:" + x + ",y:" + y + "]";
    }

    public ChunkPosition translate(int x, int y) {
        return new ChunkPosition(this.x + x, this.y + y);
    }
}
