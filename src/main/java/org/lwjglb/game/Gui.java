package org.lwjglb.game;

import imgui.ImGui;
import org.lwjglb.engine.IGuiInstance;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.scene.Scene;

public class Gui implements IGuiInstance {

    private MouseDisplay mouseDisplay;
    private LightControls lightControls;

    public Gui(Scene scene) {
        mouseDisplay = new MouseDisplay(scene);
        lightControls = new LightControls(scene);
    }

    @Override
    public void drawGui() {
        ImGui.newFrame();
        mouseDisplay.drawGui();
        lightControls.drawGui();
        ImGui.render();
    }

    @Override
    public boolean handleGuiInput(Scene scene, Window window) {
        boolean mouse = mouseDisplay.handleGuiInput(scene, window);
        boolean light = lightControls.handleGuiInput(scene, window);
        return mouse || light;
    }
}
