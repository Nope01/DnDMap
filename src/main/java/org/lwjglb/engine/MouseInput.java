package org.lwjglb.engine;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private Vector2f mousePos;
    private Vector2f displVec;
    private boolean inWindow;
    private boolean leftButtonPressed;
    private boolean rightButtonPressed;
    private boolean middleButtonPressed;
    private Vector2f previousPos;
    private Vector2f windowSize;
    private Vector2f viewPos;

    public MouseInput(Window window, long windowHandle) {
        previousPos = new Vector2f(-1, -1);
        mousePos = new Vector2f();
        displVec = new Vector2f();
        windowSize = new Vector2f();
        viewPos = new Vector2f();
        leftButtonPressed = false;
        rightButtonPressed = false;
        middleButtonPressed = false;
        inWindow = false;

        glfwSetCursorPosCallback(windowHandle, (handle, xpos, ypos) -> {
            mousePos.x = (float) xpos;
            mousePos.y = (float) ypos;
        });

        //Init the values
        windowSize.x = (float) window.getWidth();
        windowSize.y = (float) window.getHeight();

        //Update when resizing
        glfwSetWindowSizeCallback(windowHandle, (handle, width, height) -> {
            windowSize.x = (float) width;
            windowSize.y = (float) height;
        });

        glfwSetCursorEnterCallback(windowHandle, (handle, entered) -> inWindow = entered);
        glfwSetMouseButtonCallback(windowHandle, (handle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
            middleButtonPressed = button == GLFW_MOUSE_BUTTON_3 && action == GLFW_PRESS;
        });
    }

    public Vector2f getMousePos() {
        return mousePos;
    }

    public Vector2f getWindowSize() {
        return windowSize;
    }

    public Vector2f getViewPos() {
        return viewPos;
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public void input() {
        displVec.x = 0;
        displVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = mousePos.x - previousPos.x;
            double deltay = mousePos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        previousPos.x = mousePos.x;
        previousPos.y = mousePos.y;

        viewPos.x = (2*(mousePos.x - 0)/(windowSize.x - 0))-1;
        viewPos.y = -(2*(mousePos.y - 0)/(windowSize.y - 0))+1;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

    public boolean isMiddleButtonPressed() {
        return middleButtonPressed;
    }
}