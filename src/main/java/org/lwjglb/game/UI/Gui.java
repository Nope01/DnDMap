package org.lwjglb.game.UI;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import org.lwjglb.engine.IGuiInstance;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.scene.Scene;

public class Gui implements IGuiInstance {

    private boolean firstFrame = true;
    private Debug debug;
    private LightControls lightControls;
    private Menu menu;

    public Gui(Scene scene) {
        ImGui.getIO().setFontGlobalScale(1.5f);
        ImGui.getIO().setWantCaptureMouse(true);

        debug = new Debug(scene);
        lightControls = new LightControls(scene);

        menu = new Menu(scene);
    }

    @Override
    public void drawGui() {
        ImGui.newFrame();

        //For enabling resizing
        if (firstFrame) {
            ImGui.setNextWindowPos(0, 100, ImGuiCond.Always);
            ImGui.setNextWindowSize(550, 600);
            debug.drawGui();

            ImGui.setNextWindowPos(0, 700, ImGuiCond.Always);
            ImGui.setNextWindowSize(550, 400);
            lightControls.drawGui();

            ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
            ImGui.setNextWindowSize(1000, 50);
            menu.drawGui();

            firstFrame = false;
        }
        else {
            debug.drawGui();
            lightControls.drawGui();
            menu.drawGui();
        }

        ImGui.render();
    }

    @Override
    public boolean handleGuiInput(Scene scene, Window window) {
         debug.handleGuiInput(scene, window);
        lightControls.handleGuiInput(scene, window);
        return true;
    }


}
