package net.comsoria.engine.view.graph;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class BufferAttribute {
    private final float[] data;
    private final int indices;

    private int id;

    public BufferAttribute(List<Vector3f> vecs) {
        data = new float[vecs.size() * 3];
        indices = 3;
        for (int i = 0; i < vecs.size(); i++) {
            Vector3f vec = vecs.get(i);

            data[(i * 3)] = vec.x;
            data[(i * 3) + 1] = vec.y;
            data[(i * 3) + 2] = vec.z;
        }
    }

    public BufferAttribute(float[] data, int indices) {
        this.data = data;
        this.indices = indices;
    }

    public void bind(int index) {
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
    }

    public void cleanup() {
        glDeleteBuffers(this.id);
    }

    void enable(int index) {
        glEnableVertexAttribArray(index);
    }

    void disable(int index) {
        glDisableVertexAttribArray(index);
    }

    public float[] get() {
        return data;
    }

    public float get(int x) {
        return data[x];
    }

    public void set(int x, float value) {
        data[x] = value;
    }

    public BufferAttribute clone() {
        return new BufferAttribute(this.data.clone(), this.indices);
    }
}
