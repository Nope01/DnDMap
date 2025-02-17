package org.lwjglb.game;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import org.joml.*;
import org.joml.primitives.Intersectionf;
import org.lwjglb.engine.*;
import org.lwjglb.engine.graph.*;
import org.lwjglb.engine.scene.*;
import org.lwjglb.engine.scene.lights.PointLight;
import org.lwjglb.engine.scene.lights.SceneLights;
import org.lwjglb.engine.scene.lights.SpotLight;
import org.lwjglb.game.UI.Gui;
import org.lwjglb.game.UI.LightControls;
import org.lwjglb.game.UI.Debug;

import java.lang.Math;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class Main implements IAppLogic, IGuiInstance {
    private Grid hexGrid;
    private Vector4f displInc = new Vector4f();
    private float rotation;
    private LightControls lightControls;
    private Debug debug;
    private Gui gui;



    public static void main(String[] args) {
        Main main = new Main();
        Engine gameEng = new Engine("Main", new Window.WindowOptions(), main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        Camera camera = scene.getCamera();
        camera.setPosition(3.5f, 10.0f, 5.0f);
        camera.setRotation(1.5f, 0.0f);

        //Grid
        hexGrid = new Grid(6, 6);
        hexGrid.makeGrid(scene);

        //Lights
        SceneLights sceneLights = new SceneLights();
        sceneLights.getAmbientLight().setIntensity(0.3f);
        scene.setSceneLights(sceneLights);
        sceneLights.getPointLights().add(new PointLight(new Vector3f(1, 1, 1),
                new Vector3f(0, 1, 1.4f), 1.0f));

        Skybox skybox = new Skybox("resources/models/skybox/skybox.obj", scene.getTextureCache());
        skybox.getSkyboxEntity().setScale(50);
        skybox.getSkyboxEntity().updateModelMatrix();
        scene.setSkybox(skybox);

        Vector3f coneDir = new Vector3f(0, 0, -1);
        sceneLights.getSpotLights().add(new SpotLight(new PointLight(new Vector3f(1, 1, 1),
                new Vector3f(0, 0, -1.4f), 0.0f), coneDir, 140.0f));

        gui = new Gui(scene);
        scene.setGuiInstance(gui);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) {
        Camera camera = scene.getCamera();
        camera.input(window, scene, diffTimeMillis, inputConsumed);

        MouseInput mouseInput = window.getMouseInput();

        if (mouseInput.isLeftButtonPressed()) {
            if (selectEntity(window, scene, mouseInput.getMousePos())) {
                Vector3f pos = scene.getSelectedEntity().getPosition();
                pos.set(mouseInput.getRayIntersection(scene));
            }

        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            hexGrid.redraw(scene, 3, 2);
        }


        if (inputConsumed) {
            return;
        }
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        for (Model model: scene.getModelMap().values()) {
            for (Entity entity: model.getEntitiesList()) {
                entity.updateModelMatrix();
                if (entity instanceof Hexagon) {
                    ((Hexagon) entity).update(window, scene, diffTimeMillis);
                }
            }
        }
    }

    @Override
    public void drawGui() {
        ImGui.newFrame();
        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
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

        return imGuiIO.getWantCaptureMouse() || imGuiIO.getWantCaptureKeyboard();
    }

    private boolean selectEntity(Window window, Scene scene, Vector2f mousePos) {
        int wdwWidth = window.getWidth();
        int wdwHeight = window.getHeight();

        MouseInput mouseInput = window.getMouseInput();
        Vector4f mouseDir = mouseInput.getMouseDir(scene);

        Vector4f min = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        Vector4f max = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        Vector2f nearFar = new Vector2f();

        Entity selectedEntity = null;
        float closestDistance = Float.POSITIVE_INFINITY;
        Vector3f center = scene.getCamera().getPosition();

        Collection<Model> models = scene.getModelMap().values();

        Matrix4f modelMatrix = new Matrix4f();
        for (Model model: models) {
            List<Entity> entities = model.getEntitiesList();
            for (Entity entity: entities) {
                modelMatrix.translate(entity.getPosition()).scale(entity.getScale());
                for (Material material : model.getMaterialList()) {
                    for (Mesh mesh : material.getMeshList()) {
                        Vector3f aabbMin = mesh.getAabbMin();
                        min.set(aabbMin.x, aabbMin.y, aabbMin.z, 1.0f);
                        min.mul(modelMatrix);
                        Vector3f aabbMax = mesh.getAabbMax();
                        max.set(aabbMax.x, aabbMax.y, aabbMax.z, 1.0f);
                        max.mul(modelMatrix);
                        if (Intersectionf.intersectRayAab(center.x, center.y, center.z,
                                mouseDir.x, mouseDir.y, mouseDir.z,
                                min.x, min.y, min.z, max.x, max.y, max.z, nearFar)
                        && nearFar.x < closestDistance) {
                            closestDistance = nearFar.x;
                            selectedEntity = entity;
                            scene.setSelectedEntity(selectedEntity);
                        }
                    }
                }
                modelMatrix.identity();
            }
        }
        if (selectedEntity == null) {
            scene.setSelectedEntity(selectedEntity);
            return false;
        }
        else {
            return true;
        }
    }
}