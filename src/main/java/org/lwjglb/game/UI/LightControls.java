package org.lwjglb.game.UI;

import imgui.*;
import imgui.flag.ImGuiCond;
import org.joml.*;
import org.lwjglb.engine.*;
import org.lwjglb.engine.scene.Scene;
import org.lwjglb.engine.scene.lights.*;

public class LightControls implements IGuiInstance {

    private float[] ambientColor;
    private float[] ambientFactor;
    private float[] directionalConeX;
    private float[] directionalConeY;
    private float[] directionalConeZ;
    private float[] directionalLightColor;
    private float[] directionalLightIntensity;
    private float[] directionalLightX;
    private float[] directionalLightY;
    private float[] directionalLightZ;
    private float[] pointLightColor;
    private float[] pointLightIntensity;
    private float[] pointLightX;
    private float[] pointLightY;
    private float[] pointLightZ;
    private float[] spotLightColor;
    private float[] spotLightCuttoff;
    private float[] spotLightIntensity;
    private float[] spotLightX;
    private float[] spotLightY;
    private float[] spotLightZ;

    public LightControls(Scene scene) {
        SceneLights sceneLights = scene.getSceneLights();
        AmbientLight ambientLight = sceneLights.getAmbientLight();
        Vector3f color = ambientLight.getColor();

        ambientFactor = new float[]{ambientLight.getIntensity()};
        ambientColor = new float[]{color.x, color.y, color.z};

        PointLight pointLight = sceneLights.getPointLights().get(0);
        color = pointLight.getColor();
        Vector3f pos = pointLight.getPosition();
        pointLightColor = new float[]{color.x, color.y, color.z};
        pointLightX = new float[]{pos.x};
        pointLightY = new float[]{pos.y};
        pointLightZ = new float[]{pos.z};
        pointLightIntensity = new float[]{pointLight.getIntensity()};

        SpotLight spotLight = sceneLights.getSpotLights().get(0);
        pointLight = spotLight.getPointLight();
        color = pointLight.getColor();
        pos = pointLight.getPosition();
        spotLightColor = new float[]{color.x, color.y, color.z};
        spotLightX = new float[]{pos.x};
        spotLightY = new float[]{pos.y};
        spotLightZ = new float[]{pos.z};
        spotLightIntensity = new float[]{pointLight.getIntensity()};
        spotLightCuttoff = new float[]{spotLight.getCutOffAngle()};
        Vector3f coneDir = spotLight.getConeDirection();
        directionalConeX = new float[]{coneDir.x};
        directionalConeY = new float[]{coneDir.y};
        directionalConeZ = new float[]{coneDir.z};

        DirectionalLight directionalLight = sceneLights.getDirectionalLight();
        color = directionalLight.getColor();
        pos = directionalLight.getDirection();
        directionalLightColor = new float[]{color.x, color.y, color.z};
        directionalLightX = new float[]{pos.x};
        directionalLightY = new float[]{pos.y};
        directionalLightZ = new float[]{pos.z};
        directionalLightIntensity = new float[]{directionalLight.getIntensity()};
    }

    @Override
    public void drawGui() {
        ImGui.begin("Lights controls");
        if (ImGui.collapsingHeader("Ambient Light")) {
            ImGui.sliderFloat("Ambient factor", ambientFactor, 0.0f, 1.0f, "%.2f");
            ImGui.colorEdit3("Ambient color", ambientColor);
        }

        if (ImGui.collapsingHeader("Point Light")) {
            ImGui.sliderFloat("Point Light - x", pointLightX, -10.0f, 10.0f, "%.2f");
            ImGui.sliderFloat("Point Light - y", pointLightY, -10.0f, 10.0f, "%.2f");
            ImGui.sliderFloat("Point Light - z", pointLightZ, -10.0f, 10.0f, "%.2f");
            ImGui.colorEdit3("Point Light color", pointLightColor);
            ImGui.sliderFloat("Point Light Intensity", pointLightIntensity, 0.0f, 1.0f, "%.2f");
        }

        if (ImGui.collapsingHeader("Spot Light")) {
            ImGui.sliderFloat("Spot Light - x", spotLightX, -10.0f, 10.0f, "%.2f");
            ImGui.sliderFloat("Spot Light - y", spotLightY, -10.0f, 10.0f, "%.2f");
            ImGui.sliderFloat("Spot Light - z", spotLightZ, -10.0f, 10.0f, "%.2f");
            ImGui.colorEdit3("Spot Light color", spotLightColor);
            ImGui.sliderFloat("Spot Light Intensity", spotLightIntensity, 0.0f, 1.0f, "%.2f");
            ImGui.separator();
            ImGui.sliderFloat("Spot Light cutoff", spotLightCuttoff, 0.0f, 360.0f, "%2.f");
            ImGui.sliderFloat("Dir cone - x", directionalConeX, -1.0f, 1.0f, "%.2f");
            ImGui.sliderFloat("Dir cone - y", directionalConeY, -1.0f, 1.0f, "%.2f");
            ImGui.sliderFloat("Dir cone - z", directionalConeZ, -1.0f, 1.0f, "%.2f");
        }

        if (ImGui.collapsingHeader("Dir Light")) {
            ImGui.sliderFloat("Dir Light - x", directionalLightX, -1.0f, 1.0f, "%.2f");
            ImGui.sliderFloat("Dir Light - y", directionalLightY, -1.0f, 1.0f, "%.2f");
            ImGui.sliderFloat("Dir Light - z", directionalLightZ, -1.0f, 1.0f, "%.2f");
            ImGui.colorEdit3("Dir Light color", directionalLightColor);
            ImGui.sliderFloat("Dir Light Intensity", directionalLightIntensity, 0.0f, 1.0f, "%.2f");
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

        boolean consumed = imGuiIO.getWantCaptureMouse() || imGuiIO.getWantCaptureKeyboard();
        if (consumed) {
            SceneLights sceneLights = scene.getSceneLights();
            AmbientLight ambientLight = sceneLights.getAmbientLight();
            ambientLight.setIntensity(ambientFactor[0]);
            ambientLight.setColor(ambientColor[0], ambientColor[1], ambientColor[2]);

            PointLight pointLight = sceneLights.getPointLights().get(0);
            pointLight.setPosition(pointLightX[0], pointLightY[0], pointLightZ[0]);
            pointLight.setColor(pointLightColor[0], pointLightColor[1], pointLightColor[2]);
            pointLight.setIntensity(pointLightIntensity[0]);

            SpotLight spotLight = sceneLights.getSpotLights().get(0);
            pointLight = spotLight.getPointLight();
            pointLight.setPosition(spotLightX[0], spotLightY[0], spotLightZ[0]);
            pointLight.setColor(spotLightColor[0], spotLightColor[1], spotLightColor[2]);
            pointLight.setIntensity(spotLightIntensity[0]);
            spotLight.setCutOffAngle(spotLightColor[0]);
            spotLight.setConeDirection(directionalConeX[0], directionalConeY[0], directionalConeZ[0]);

            DirectionalLight directionalLight = sceneLights.getDirectionalLight();
            directionalLight.setPosition(directionalLightX[0], directionalConeY[0], directionalConeZ[0]);
            directionalLight.setColor(directionalLightColor[0], directionalLightColor[1], directionalLightColor[2]);
            directionalLight.setIntensity(directionalLightIntensity[0]);
        }
        return consumed;
    }
}