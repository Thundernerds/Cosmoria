package net.comsoria.engine.view.graph;

import net.comsoria.engine.Tuple;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Geometry {
    private final int vaoId;
    private final List<Integer> vboIdList;
    private final int vertexCount;
    private final int indicesBufferID;
    private int culledFace = -1;

    public Geometry(Tuple<List<BufferAttribute>, int[]> data) {
        this(data.getA(), data.getB());
    }

    public Geometry(List<BufferAttribute> attributes, int[] indices) {
        vertexCount = indices.length;
        vboIdList = new ArrayList<>();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        for (BufferAttribute attribute : attributes) {
            int id = attribute.bind(this.vboIdList.size());
            if (id == -1) throw new RuntimeException("ERROR CREATING VBO");
            vboIdList.add(id);
        }

        IntBuffer indicesBuffer = null;
        try {
            indicesBufferID = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesBufferID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        } finally {
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void setCullFace(int face) {
        this.culledFace = face;
    }

    public void enableCull() {
        if (this.culledFace != -1) {
            glEnable(GL_CULL_FACE);
            glCullFace(this.culledFace);
        }
    }

    public void disableCull() {
        if (this.culledFace != -1) {
            glDisable(GL_CULL_FACE);
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }
        glDeleteBuffers(indicesBufferID);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public void bindAttributes() {
        for (int i = 0; i < vboIdList.size(); i++) {
            glEnableVertexAttribArray(i);
        }
    }

    public void unbindAttributes() {
        for (int i = 0; i < vboIdList.size(); i++) {
            glDisableVertexAttribArray(i);
        }
    }

    public void bind() {
        glBindVertexArray(vaoId);
    }

    public void unbind() {
        glBindVertexArray(0);
    }
}
