package org.lwjglb.game.UI;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import org.lwjglb.engine.IGuiInstance;
import org.lwjglb.engine.MouseInput;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.scene.Scene;

public class Menu implements IGuiInstance {

    public Menu(Scene scene) {

    }
    @Override
    public void drawGui() {
        ImGui.begin("Menu", ImGuiWindowFlags.MenuBar
                | ImGuiWindowFlags.NoResize
                | ImGuiWindowFlags.NoMove
                | ImGuiWindowFlags.NoCollapse);

         if (ImGui.beginMenuBar()) {

             if (ImGui.beginMenu("File")) {
                 if (ImGui.menuItem("Bong")) {
                     System.out.println("Bingus");
                 }
                 ImGui.endMenu();
             }

             if (ImGui.beginMenu("Edit")) {
                 if (ImGui.menuItem("Save")) {
                     System.out.println("Save");
                 }
                 ImGui.endMenu();
             }
             ImGui.endMenuBar();
         }

         ImGui.end();
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

        }
        return consumed;
    }
}
