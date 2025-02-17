package org.lwjglb.engine;

import org.joml.*;
import org.lwjglb.engine.scene.Scene;

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
    private Vector3f ndcPos;
    private Vector3f worldPos;

    public MouseInput(Window window, long windowHandle) {
        previousPos = new Vector2f(-1, -1);
        mousePos = new Vector2f();
        displVec = new Vector2f();
        windowSize = new Vector2f();
        ndcPos = new Vector3f();
        worldPos = new Vector3f();

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

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

    public boolean isMiddleButtonPressed() {
        return middleButtonPressed;
    }

    public Vector2f getMousePos() {
        return mousePos;
    }

    public Vector2f getWindowSize() {
        return windowSize;
    }

    public Vector3f getNdcPos() {
        ndcPos.x = (2*(mousePos.x)/(windowSize.x))-1;
        ndcPos.y = -(2*(mousePos.y)/(windowSize.y))+1;
        ndcPos.z = -1.0f;
        return ndcPos;
    }

    public Vector4f getMouseDir(Scene scene) {
        float x = getNdcPos().x;
        float y = getNdcPos().y;
        float z = -1.0f;

        Matrix4f invProjMatrix = scene.getProjection().getInvProjMatrix();
        Vector4f dir = new Vector4f(x, y, z, 1.0f);
        dir.mul(invProjMatrix);
        dir.z = -1.0f;
        dir.w = 0.0f;

        Matrix4f invViewMatrix = scene.getCamera().getInvViewMatrix();
        dir.mul(invViewMatrix);
        return dir;
    }

    //Intersects with 0,0,0 plane
    public Vector3f getRayIntersection(Scene scene) {
        Vector4f dir = getMouseDir(scene);
        Vector3f rayDir = new Vector3f(
                dir.x, dir.y, dir.z
        );

        Vector3f center = scene.getCamera().getPosition();
        Vector3f point = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f normal = new Vector3f(0.0f, 1.0f, 0.0f);
        float f = Intersectionf.intersectRayPlane(center, rayDir, point, normal, 0.5f);

        //useful math for calculating intersection
        Vector3f intersectionPoint = new Vector3f(rayDir).mul(f).add(center);
        return intersectionPoint;
    }


    public Vector2f getDisplVec() {
        return displVec;
    }

    public void input(Scene scene) {
        displVec.x = 0;
        displVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = mousePos.x - previousPos.x;
            double deltay = mousePos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.x = (float) deltax;
            }
            if (rotateY) {
                displVec.y = (float) deltay;
            }
        }
        previousPos.x = mousePos.x;
        previousPos.y = mousePos.y;
        ndcPos = getNdcPos();
        worldPos = getRayIntersection(scene);
    }
}