package org.lwjglb.engine.scene;

import org.joml.*;
import org.lwjglb.engine.MouseInput;
import org.lwjglb.engine.Window;

import java.lang.Math;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;

public class Camera implements SceneObject {

    private static final float MOUSE_SENSITIVITY = 0.08f;
    private static final float MOVEMENT_SPEED = 0.02f;
    private static final float PAN_SPEED = 0.005f;

    private Vector3f direction;
    private Vector3f position;
    private Vector3f right;
    private Vector2f rotation;
    private Vector3f up;
    private Matrix4f viewMatrix;
    private Matrix4f invViewMatrix;

    public Camera() {
        direction = new Vector3f();
        right = new Vector3f();
        up = new Vector3f();
        position = new Vector3f();
        viewMatrix = new Matrix4f();
        rotation = new Vector2f();
        invViewMatrix = new Matrix4f();
    }

    public void addRotation(float x, float y) {
        rotation.add(x, y);
        recalculate();
    }

    //For panning, uses rotational displVec so is not labelled correctly for x and y axis
    public void addPosition(float x, float y) {
        moveUp(x);
        moveLeft(y);
        recalculate();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public void moveBackwards(float inc) {
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.sub(direction);
        recalculate();
    }

    public void moveDown(float inc) {
        viewMatrix.positiveY(up).mul(inc);
        position.sub(up);
        recalculate();
    }

    public void moveForward(float inc) {
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.add(direction);
        recalculate();
    }

    public void moveLeft(float inc) {
        viewMatrix.positiveX(right).mul(inc);
        position.sub(right);
        recalculate();
    }

    public void moveRight(float inc) {
        viewMatrix.positiveX(right).mul(inc);
        position.add(right);
        recalculate();
    }

    public void moveUp(float inc) {
        viewMatrix.positiveY(up).mul(inc);
        position.add(up);
        recalculate();
    }

    private void recalculate() {
        viewMatrix.identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z);
        invViewMatrix.set(viewMatrix).invert();
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        recalculate();
    }

    public void setRotation(float x, float y) {
        rotation.set(x, y);
        recalculate();
    }

    public Matrix4f getInvViewMatrix() {
        return invViewMatrix;
    }

    @Override
    public void render() {

    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {

    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) {
        float move = diffTimeMillis * MOVEMENT_SPEED;
        if (window.isKeyPressed(GLFW_KEY_W)) {
            this.moveForward(move);
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            this.moveBackwards(move);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            this.moveLeft(move);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            this.moveRight(move);
        }
        if (window.isKeyPressed(GLFW_KEY_Q)) {
            this.moveUp(move);
        } else if (window.isKeyPressed(GLFW_KEY_E)) {
            this.moveDown(move);
        }

        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            this.addRotation((float) Math.toRadians(displVec.y * MOUSE_SENSITIVITY),
                    (float) Math.toRadians(displVec.x * MOUSE_SENSITIVITY));
        }
        if (mouseInput.isMiddleButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            this.addPosition(displVec.y * PAN_SPEED, displVec.x * PAN_SPEED);
        }
    }
}