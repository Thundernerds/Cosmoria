package net.comsoria.engine.view;

import net.comsoria.engine.Scene;
import net.comsoria.engine.Timer;
import net.comsoria.engine.Tuple;
import net.comsoria.engine.view.GLSL.Programs.PostProcessingProgram;
import net.comsoria.engine.view.GLSL.Programs.custom.CustomShaderProgram;
import net.comsoria.engine.view.GLSL.Programs.custom.IExtractSceneData;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.*;
import org.joml.Matrix4f;

import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class FrameBuffer {
    private final Mesh mesh;

    private int fbo, drb;

    public FrameBuffer(int width, int height, String vertex, String fragment) throws Exception {
        BufferAttribute vertices = new BufferAttribute(new float[] {
                -1, -1,
                1, -1,
                1, 1,
                -1, 1
        }, 2);
        Geometry geometry = new Geometry(new Tuple<>(Arrays.asList(vertices), new int[] {
                0, 1, 2, 0, 2, 3
        }));

        Material material = new Material();
        material.shaderProgram = FrameBuffer.generateFrameBufferShader(vertex, fragment);
        mesh = new Mesh(geometry, material);

//        fbo = glGenFramebuffers();
//        this.bind();
//
//        drb = glGenRenderbuffers();
//        glBindRenderbuffer(GL_RENDERBUFFER, drb);
//        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
//        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, drb);
//
//        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, material.textures.get(0).getId(), 0);
//        glDrawBuffers(new int[] {GL_COLOR_ATTACHMENT0});
//        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
//            System.out.println("ERROR");
//            return;
//        }
//
//        FrameBuffer.unbind();

        setSize(width, height);
    }

    public void setSize(int width, int height) {
        cleanup();
        mesh.material.textures.add(new Texture(width, height));

        fbo = glGenFramebuffers();
        this.bind();

        drb = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, drb);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, drb);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, mesh.material.textures.get(0).getId(), 0);
        glDrawBuffers(new int[] {GL_COLOR_ATTACHMENT0});
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("ERROR");
            return;
        }

        FrameBuffer.unbind();
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public int getID() {
        return fbo;
    }

    public void cleanup() {
        glDeleteFramebuffers(fbo);
        if (this.mesh.material.isTextured()) {
            this.mesh.material.textures.get(0).cleanup();
            this.mesh.material.textures.remove(0);
        }
        glDeleteRenderbuffers(drb);
    }

    public void render() throws Exception {
        if (this.mesh.needsInitialising()) this.mesh.init(null);
        this.mesh.bind();
        this.mesh.render();
        this.mesh.unbind();
    }

    public static void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public static ShaderProgram generateFrameBufferShader(String vertex, String fragment) throws Exception {
        return new CustomShaderProgram(vertex, fragment, Arrays.asList("time"), Arrays.asList("frameBufferTexture"), new IExtractSceneData() {
            @Override
            public void extractScene(Scene scene, ShaderProgram shaderProgram, Matrix4f projMatrix, Matrix4f viewMatrix) {
                shaderProgram.setUniform("time", Timer.getTime());
            }
            @Override
            public void extractMesh(Mesh mesh, ShaderProgram shaderProgram, Matrix4f matrix) {

            }
        });
    }
}
