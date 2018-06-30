package net.comsoria.engine.view.input;

import net.comsoria.engine.view.Window;
import org.joml.Vector2d;
import org.lwjgl.system.MemoryUtil;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class MouseInput {
    private Vector2d cumulativePos = new Vector2d();
    private Vector2d lastPos = new Vector2d();
    private Vector2d movement = new Vector2d();

    private Vector2d start;

    private boolean inWindow = true;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    public boolean enabled = true;

    public void init(Window window) {
        DoubleBuffer x = MemoryUtil.memAllocDouble(1);
        DoubleBuffer y = MemoryUtil.memAllocDouble(1);
        glfwGetCursorPos(window.getWindowHandle(), x, y);
        start = new Vector2d(x.get(), y.get());

        glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
            if (!enabled) return;

            Vector2d currentPos = new Vector2d(xpos - start.x(), ypos - start.y());
            movement = new Vector2d(currentPos.x - lastPos.x, currentPos.y - lastPos.y);

            cumulativePos.set(cumulativePos.x + movement.x, cumulativePos.y + movement.y);
            lastPos = currentPos;
        });

        glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> {
            if (!enabled) return;

            inWindow = entered;
        });

        glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
            if (!enabled) return;

            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    public void input() {
        movement.set(0, 0);
    }

    public Vector2d getMovementVec() {
        return movement;
    }

    public Vector2d getDisplVec() {
        return cumulativePos;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
