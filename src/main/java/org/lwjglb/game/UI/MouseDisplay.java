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
import org.lwjglb.engine.scene.Scene;

public class MouseDisplay implements IGuiInstance {
    private Vector2f mousePos;
    private Vector2f viewPos;
    private Vector2f resolution;
    private Vector2f displVec;

    private float[] posX;
    private float[] posY;
    private float[] posZ;

    private Vector3f camPos;
    private Vector3f viewVec;

    public MouseDisplay(Scene scene) {
        mousePos = new Vector2f();
        viewPos = new Vector2f();
        resolution = new Vector2f();
        displVec = new Vector2f();

        Model model = scene.getModelMap().get("hex-model");
        Vector3f pos = model.getEntitiesList().getFirst().getPosition();
        posX = new float[]{pos.x};
        posY = new float[]{pos.y};
        posZ = new float[]{pos.z};

        camPos = scene.getCamera().getPosition();
        viewVec = new Vector3f();

        camPos.sub(pos, viewVec);
    }

    @Override
    public void drawGui() {
        ImGui.begin("Mouse Display");
        ImGui.text("View vec");
        ImGui.button(String.valueOf("X:" + viewVec.x()));
        ImGui.button(String.valueOf("Y:" + viewVec.y()));
        ImGui.button(String.valueOf("Z:" + viewVec.z()));
        ImGui.separator();
        ImGui.text("Camera pos");
        ImGui.button(String.valueOf("X:" + camPos.x()));
        ImGui.button(String.valueOf("Y:" + camPos.y()));
        ImGui.button(String.valueOf("Z:" + camPos.z()));
        ImGui.separator();
        ImGui.text("Model manipulation");
        ImGui.sliderFloat("Pos - x", posX, -3.0f, 3.0f, "%.2f");
        ImGui.sliderFloat("Pos - y", posY, -3.0f, 3.0f, "%.2f");
        ImGui.sliderFloat("Pos - z", posZ, -3.0f, 3.0f, "%.2f");

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
            Model model = scene.getModelMap().get("hex-model");
            model.getEntitiesList().getFirst().setPosition(posX[0], posY[0], posZ[0]);
        }

        this.mousePos = mousePos;
        this.resolution = mouseInput.getWindowSize();
        this.viewPos = mouseInput.getViewPos();
        this.displVec = mouseInput.getDisplVec();
        this.camPos = scene.getCamera().getPosition();

        Model model = scene.getModelMap().get("hex-model");
        this.camPos.sub(model.getEntitiesList().getFirst().getPosition(), this.viewVec);





        return consumed;
    }
}
