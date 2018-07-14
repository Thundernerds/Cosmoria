package net.comsoria.engine.view.GLSL;

import net.comsoria.engine.Scene;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.Closeable;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram implements Closeable {
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private final Map<String, Integer> uniforms;
    public final List<String> textures;
    private boolean updated = false;

    public ShaderProgram() {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new GLSLException("Could not create Shader");
        }
        uniforms = new HashMap<>();
        textures = new ArrayList<>();
    }

    public void createUniform(String uniformName) {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new GLSLException("Uniform '" + uniformName + "' not used in GLSL. Failed to create.");
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void createTextureUniform(String name) {
        textures.add(name);
        createUniform(name);
    }

    public void createListUniform(String name, int length) {
        for (int i = 0; i < length; i++) {
            createUniform(name + "[" + i + "]");
        }
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Dump the matrix into a float buffer
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            try {
                glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
            } catch (Exception e) {
                throw new GLSLException(e.getMessage());
            }

        }
    }

    public void setUniform(String uniformName, int value) {
        try {
            glUniform1i(uniforms.get(uniformName), value);
        } catch (Exception e) {
            throw new GLSLException(e.getMessage());
        }
    }

    public void setUniform(String uniformName, float value) {
        try {
            glUniform1f(uniforms.get(uniformName), value);
        } catch (Exception e) {
            throw new GLSLException(e.getMessage());
        }
    }

    public void setUniform(String uniformName, Vector3f value) {
        try {
            glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
        } catch (Exception e) {
            throw new GLSLException(e.getMessage());
        }
    }

    public void setUniform(String uniformName, Vector4f value) {
        try {
            glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
        } catch (Exception e) {
            throw new GLSLException(e.getMessage());
        }
    }

    public void setUniform(String name, GLSLUniformBindable object) {
        try {
            object.set(this, name);
        } catch (Exception e) {
            throw new GLSLException(e.getMessage());
        }
    }


    private void createVertexShader(String shaderCode) {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    private void createFragmentShader(String shaderCode) {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new GLSLException("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new GLSLException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }


    public void open() {
        this.updated = true;
}

    public void close() {
        this.updated = false;
    }

    public boolean isUpdated() {
        return this.updated;
    }


    public void setupScene(Scene scene, Matrix4f projectionMatrix, Matrix4f viewMatrix) {

    }

    public void setupMesh(Mesh mesh, Matrix4f modelMatrix) {

    }


    private void link() {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new GLSLException("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    public void create(String vertex, String fragment) {
        this.createVertexShader(vertex);
        this.createFragmentShader(fragment);
        this.link();
    }

    public void init() throws IOException {

    }


    public int getProgramId() {
        return programId;
    }
}
