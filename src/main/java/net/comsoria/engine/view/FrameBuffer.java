package net.comsoria.engine.view;

import net.comsoria.engine.Scene;
import net.comsoria.engine.utils.Timer;
import net.comsoria.engine.utils.Tuple;
import net.comsoria.engine.view.batch.RenderData;
import net.comsoria.engine.view.GLSL.Programs.CustomShaderProgram;
import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.GLSL.Transformation;
import net.comsoria.engine.view.graph.*;
import net.comsoria.engine.view.graph.mesh.Mesh;
import org.joml.Matrix4f;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

public class FrameBuffer implements Renderable {
    private final Mesh mesh;

    private int fbo, drb;

    public FrameBuffer(int width, int height, String vertex, String fragment) throws IOException {
        this(width, height, FrameBuffer.generateFrameBufferShader(vertex, fragment));
    }

    public FrameBuffer(int width, int height, ShaderProgram shaderProgram) throws IOException {
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
        material.shaderProgram = shaderProgram;
        mesh = new Mesh(geometry, material);
        mesh.initShaderProgram();

        setSize(width, height);
    }

    public void setSize(int width, int height) {
        cleanup();
        mesh.material.textures.add(new Texture(width, height));
        mesh.material.textures.add(new Texture(() -> glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0)));

        fbo = glGenFramebuffersEXT();
        this.bind();

        drb = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, drb);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, drb);

        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, mesh.material.textures.get(0).getId(), 0);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_TEXTURE_2D, mesh.material.textures.get(1).getId(), 0);

        glDrawBuffers(new int[] {GL_COLOR_ATTACHMENT0, GL_DEPTH_ATTACHMENT});

        if (glCheckFramebufferStatusEXT(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE_EXT) {
            System.out.println("ERROR");
            return;
        }

        FrameBuffer.unbind();
    }

    public void bind() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public int getID() {
        return fbo;
    }

    @Override
    public Closeable render(Transformation transformation, Scene scene, RenderData renderData) throws Exception {
        return this.mesh.render(transformation, scene, renderData);
    }

    public void cleanup() {
        glDeleteFramebuffersEXT(fbo);
        if (this.mesh.material.isTextured()) {
            int len = this.mesh.material.textures.size();
            for (int i = 0; i < len; i++) {
                this.mesh.material.textures.get(0).cleanup();
                this.mesh.material.textures.remove(0);
            }
        }
        glDeleteRenderbuffers(drb);
    }

    @Override
    public boolean shouldRender() {
        return true;
    }

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.Any;
    }

    public static void unbind() {
        glBindFramebufferEXT(GL_FRAMEBUFFER, 0);
    }

    public static ShaderProgram generateFrameBufferShader(String vertex, String fragment) {
        return new CustomShaderProgram(vertex, fragment, Arrays.asList("time", "fog.density", "fog.start"), Arrays.asList("colorTexture", "depthTexture")) {
            @Override
            public void setupScene(Scene scene, Transformation transformation) {
                this.setUniform("time", Timer.getTime());
                this.setUniform("fog", scene.fog);
            }

            @Override
            public void setupMesh(Mesh mesh1, Matrix4f modelMatrix, Transformation transformation) {

            }
        };
    }
}
