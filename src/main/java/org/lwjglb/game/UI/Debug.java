package org.lwjglb.game.UI;

import imgui.ImGui;
import imgui.ImGuiIO;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjglb.engine.IGuiInstance;
import org.lwjglb.engine.MouseInput;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.scene.Entity;
import org.lwjglb.engine.scene.Hexagon;
import org.lwjglb.engine.scene.Scene;

public class Debug implements IGuiInstance {
    private Vector2f mousePos;
    private Vector2f viewPos;
    private Vector2f resolution;
    private Vector2f displVec;

    private Entity selectedEntity;
    private Vector3f selectedPos;
    private Vector2i selectedOffsetCoords;
    private Vector3i selectedCubeCoords;
    private Vector3i neighbour;

    private Vector3f camPos;
    private Vector3f viewVec;

    public Debug(Scene scene) {
        mousePos = new Vector2f();
        viewPos = new Vector2f();
        resolution = new Vector2f();
        displVec = new Vector2f();

        selectedPos = new Vector3f();
        selectedOffsetCoords = new Vector2i();
        selectedCubeCoords = new Vector3i();
        neighbour = new Vector3i();

        camPos = scene.getCamera().getPosition();
        viewVec = new Vector3f();
        selectedEntity = scene.getSelectedEntity();
    }

    @Override
    public void drawGui() {
        ImGui.begin("Debug");
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
        ImGui.text("Offset coords");
        ImGui.button(String.valueOf("X:" + selectedOffsetCoords.x()));
        ImGui.button(String.valueOf("Y:" + selectedOffsetCoords.y()));
        ImGui.separator();
        ImGui.text("Cube coords");
        ImGui.button(String.valueOf("X:" + selectedCubeCoords.x()));
        ImGui.button(String.valueOf("Y:" + selectedCubeCoords.y()));
        ImGui.button(String.valueOf("Z:" + selectedCubeCoords.z()));
        ImGui.button(String.valueOf("Above X:" + neighbour.x));
        ImGui.button(String.valueOf("Above Y:" + neighbour.y));
        ImGui.button(String.valueOf("Above Z:" + neighbour.z));

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
            //Checks if the entity is a hexagon before calling a hexagon specific method
            if (this.selectedEntity instanceof Hexagon) {
                this.selectedOffsetCoords = ((Hexagon) selectedEntity).getOffset();
                this.selectedCubeCoords = ((Hexagon) selectedEntity).offsetToCubeCoords(this.selectedOffsetCoords);
                this.neighbour = ((Hexagon)selectedEntity).getCubeNeighbour(this.selectedCubeCoords, Hexagon.N);
            }
        }

        this.mousePos = mousePos;
        this.resolution = mouseInput.getWindowSize();
        this.viewPos = mouseInput.getViewPos();
        this.displVec = mouseInput.getDisplVec();
        this.camPos = scene.getCamera().getPosition();

        return consumed;
    }
}
