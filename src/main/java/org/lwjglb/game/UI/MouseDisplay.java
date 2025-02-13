package org.lwjglb.game.UI;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjglb.engine.IGuiInstance;
import org.lwjglb.engine.MouseInput;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Model;
import org.lwjglb.engine.scene.Entity;
import org.lwjglb.engine.scene.Scene;

public class MouseDisplay implements IGuiInstance {
    private Vector2f mousePos;
    private Vector2f viewPos;
    private Vector2f resolution;
    private Vector2f displVec;

    private float[] posX;
    private float[] posY;
    private float[] posZ;
    private Entity selectedEntity;
    private Vector3f selectedPos;

    private Vector3f camPos;
    private Vector3f viewVec;

    public MouseDisplay(Scene scene) {
        mousePos = new Vector2f();
        viewPos = new Vector2f();
        resolution = new Vector2f();
        displVec = new Vector2f();

        selectedPos = new Vector3f();

        camPos = scene.getCamera().getPosition();
        viewVec = new Vector3f();
        selectedEntity = scene.getSelectedEntity();
    }

    @Override
    public void drawGui() {
        ImGui.begin("Mouse Display");
        ImGui.text("Selected pos");
        ImGui.button(String.valueOf("X:" + selectedPos.x));
        ImGui.button(String.valueOf("Y:" + selectedPos.y));
        ImGui.button(String.valueOf("Z:" + selectedPos.z));
        ImGui.separator();
        ImGui.text("Camera pos");
        ImGui.button(String.valueOf("X:" + camPos.x()));
        ImGui.button(String.valueOf("Y:" + camPos.y()));
        ImGui.button(String.valueOf("Z:" + camPos.z()));
        ImGui.separator();

        ImGui.end();
    }

    @Override
    public boolean handleGuiInput(Scene scene, Window window) {
        ImGuiIO imGuiIO = ImGui.getIO();
        MouseInput mouseInput = window.getMouseInput();
        Vector2f mousePos = mouseInput.getMousePos();
        Vector2f displVec = mouseInput.getDisplVec();
        imGuiIO.addMousePosEvent(mousePos.x, mousePos.y);
        imGuiIO.addMouseButtonEvent(0, mouseInput.isLeftButtonPressed());
        imGuiIO.addMouseButtonEvent(1, mouseInput.isRightButtonPressed());

        boolean consumed = imGuiIO.getWantCaptureMouse();
        if (consumed) {

        }
        this.selectedEntity = scene.getSelectedEntity();

        if (this.selectedEntity != null) {
            this.selectedPos = scene.getSelectedEntity().getPosition();
        }
        this.mousePos = mousePos;
        this.resolution = mouseInput.getWindowSize();
        this.viewPos = mouseInput.getViewPos();
        this.displVec = mouseInput.getDisplVec();
        this.camPos = scene.getCamera().getPosition();

        return consumed;
    }
}
