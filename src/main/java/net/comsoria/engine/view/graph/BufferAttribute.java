package net.comsoria.engine.view.graph;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class BufferAttribute {
    private final float[] data;
    private final int indices;

    public BufferAttribute(float[] data, int indices) {
        this.data = data;
        this.indices = indices;
    }

    public int bind(int index) {
        int id;

        FloatBuffer buffer = null;
        try {
            id = glGenBuffers();
            buffer = MemoryUtil.memAllocFloat(data.length);
            buffer.put(data).flip();
            glBindBuffer(GL_ARRAY_BUFFER, id);
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
            glVertexAttribPointer(index, indices, GL_FLOAT, false, 0, 0);
        } finally {
            if (buffer != null) MemoryUtil.memFree(buffer);
        }

        return id;
    }

    public float[] get() {
        return data;
    }

    public void set(int x, float value) {
        data[x] = value;
    }
}
