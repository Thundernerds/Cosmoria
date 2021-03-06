package net.comsoria.engine.view;

import net.comsoria.engine.view.color.Color3;
import net.comsoria.engine.view.color.Color4;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final String title;
    private int width, height;

    private boolean resized = false;

    private long windowHandle;

    public Window(String title, int width, int height) {
        this.title = title;

        this.width = width;
        this.height = height;
    }

    public void init() {
        System.setProperty("java.awt.headless", "true");

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
//        glfwWindowHint(GLFW_FOCUSED, GL_TRUE);

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) throw new RuntimeException("Failed to create the GLFW window");

        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        this.setPosition(0.5, 0.5);

        IntBuffer w = MemoryUtil.memAllocInt(1);
        IntBuffer h = MemoryUtil.memAllocInt(1);
        glfwGetFramebufferSize(windowHandle, w, h);
        this.width = w.get();
        this.height = h.get();
        this.setResized(true);

        glfwMakeContextCurrent(windowHandle);

        glfwSwapInterval(1);

        GL.createCapabilities();

        this.setClearColor(0, 0, 0, 1);

        glEnable(GL_DEPTH_TEST);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //Enable for point lines
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    public void show() {
        glfwShowWindow(windowHandle);
        glfwFocusWindow(windowHandle);
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }
    public void setClearColor(Color4 color) {
        setClearColor(color.r, color.g, color.b, color.a);
    }
    public void setClearColor(Color3 color) {
        setClearColor(color.r, color.g, color.b, 1.0f);
    }

    public static GLFWVidMode getVidMode() {
        return glfwGetVideoMode(glfwGetPrimaryMonitor());
    }

    public void setPosition(double xP, double yP) { //Percentages
        GLFWVidMode vidmode = getVidMode();
        int x = (int) ((vidmode.width() - width) * xP);
        int y = (int) ((vidmode.height() - height) * yP);

        glfwSetWindowPos(windowHandle, x, y);
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isResized() {
        return resized;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void hideCursor() {
        glfwSetInputMode(this.windowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public void showCursor() {
        glfwSetInputMode(this.windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public void setMousePos(double x, double y) {
        glfwSetCursorPos(this.windowHandle, x, y);
    }
}
