package org.lwjglb.game;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import org.joml.Vector2f;
import org.lwjglb.engine.IGuiInstance;
import org.lwjglb.engine.MouseInput;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.scene.Scene;

public class MouseDisplay implements IGuiInstance {
    private Vector2f mousePos;
    private Vector2f viewPos;
    private Vector2f resolution;

    public MouseDisplay(Scene scene) {
        mousePos = new Vector2f();
        viewPos = new Vector2f();
        resolution = new Vector2f();
    }
    @Override
    public void drawGui() {
        ImGui.newFrame();
        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
        ImGui.setNextWindowSize(450, 400);

        ImGui.begin("Mouse Display");
        ImGui.button(String.valueOf("X:" + viewPos.x()));
        ImGui.button(String.valueOf("Y:" + viewPos.y()));

        ImGui.end();
        ImGui.endFrame();
        ImGui.render();
    }

    @Override
    public boolean handleGuiInput(Scene scene, Window window) {
        ImGuiIO imGuiIO = ImGui.getIO();
        MouseInput mouseInput = window.getMouseInput();
        Vector2f mousePos = mouseInput.getMousePos();
        imGuiIO.addMousePosEvent(mousePos.x, mousePos.y);
        imGuiIO.addMouseButtonEvent(0, mouseInput.isLeftButtonPressed());
        imGuiIO.addMouseButtonEvent(1, mouseInput.isRightButtonPressed());

        boolean consumed = imGuiIO.getWantCaptureMouse();
        if (consumed) {
            this.mousePos = mousePos;
            this.resolution = mouseInput.getWindowSize();
            this.viewPos = mouseInput.getViewPos();
        }

        return consumed;
    }
}
