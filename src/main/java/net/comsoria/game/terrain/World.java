package net.comsoria.game.terrain;

import net.comsoria.engine.Scene;
import net.comsoria.game.coordinate.ChunkPosition;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class World {
    final List<Chunk> chunks = new ArrayList<>();
    private final List<Chunk> buffer = new ArrayList<>();

    public Chunk getChunk(ChunkPosition position) {
        for (Chunk chunk : chunks) {
            if (chunk.position.equals(position))  return chunk;
        }
        return null;
    }

    public void addChunk(Chunk chunk) {
        chunks.add(chunk);
        buffer.add(chunk);
    }

    public List<Chunk> getBuffer() {
        List<Chunk> oldBuffer = new ArrayList<>();
        oldBuffer.addAll(buffer);
        buffer.clear();

        return oldBuffer;
    }
}
