package org.lwjglb.game;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import org.joml.*;
import org.joml.primitives.Intersectionf;
import org.lwjglb.assets.Hexagon;
import org.lwjglb.assets.Line;
import org.lwjglb.assets.Plane;
import org.lwjglb.engine.*;
import org.lwjglb.engine.graph.*;
import org.lwjglb.engine.scene.*;
import org.lwjglb.engine.scene.lights.PointLight;
import org.lwjglb.engine.scene.lights.SceneLights;
import org.lwjglb.engine.scene.lights.SpotLight;
import org.lwjglb.game.UI.Gui;
import org.lwjglb.game.UI.LightControls;
import org.lwjglb.game.UI.MouseDisplay;

import java.lang.Math;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Main implements IAppLogic, IGuiInstance {

    private static final float MOUSE_SENSITIVITY = 0.08f;
    private static final float MOVEMENT_SPEED = 0.02f;
    private static final float PAN_SPEED = 0.005f;
    private static final int NUM_CHUNKS = 4;

    private Entity cubeEntity1;
    private Entity cubeEntity2;
    private Entity treeEntity;
    private Entity planeEntity;
    private Entity hexagonEntity;
    private Entity lineEntity;
    private Entity[][] gridEntity;
    private Entity quadEntity;

    private Vector4f displInc = new Vector4f();
    private float rotation;
    private LightControls lightControls;
    private MouseDisplay mouseDisplay;
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

        //Plane
//        String quadModelId = "quad-model";
//        Model quadModel = ModelLoader.loadModel("quad-model", "resources/models/quad/quad.obj",
//                scene.getTextureCache());
//        scene.addModel(quadModel);
//
//        quadEntity = new Entity("quad-entity", quadModel.getId());
//        quadEntity.setScale(5.0f);
//        scene.addEntity(quadEntity);

        //Hexagon
        Model hexagonModel = ModelLoader.loadModel("hexagon-model", "resources/models/hexagon/hexagon.obj",
                scene.getTextureCache());
        scene.addModel(hexagonModel);

        //Grid
        int numRows = 6;
        int numCols = 6;
        float size = 1f;

        float width = 2*size;
        float height = (float) (width * Math.sqrt(3) / 2);

        float horizSpacing = 0.75f * width;
        float vertSpacing = height;

        gridEntity = new Entity[numRows][numCols];
        for (int col = 0; col < numCols; col++) {
            for (int row = 0; row < numRows; row++) {
                float x, z;

                if (col % 2 == 0) {
                    z = row * vertSpacing;
                }
                else {
                    z = row * vertSpacing + (height/2);
                }
                x = col * horizSpacing;

                Model model = ModelLoader.loadModel("hex-" + row + "-" + col, "resources/models/hexagon/hexagon.obj",
                        scene.getTextureCache());
                scene.addModel(model);
                Entity entity = new Entity("hex-" + row + "-" + col, model.getId());

                entity.setPosition(x, 0.0f, z);
                gridEntity[row][col] = entity;
                scene.addEntity(entity);
            }
        }
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
        float move = diffTimeMillis * MOVEMENT_SPEED;
        Camera camera = scene.getCamera();

        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(move);
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(move);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(move);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(move);
        }
        if (window.isKeyPressed(GLFW_KEY_Q)) {
            camera.moveUp(move);
        } else if (window.isKeyPressed(GLFW_KEY_E)) {
            camera.moveDown(move);
        }

        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(displVec.x * MOUSE_SENSITIVITY),
                    (float) Math.toRadians(displVec.y * MOUSE_SENSITIVITY));
        }
        if (mouseInput.isMiddleButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addPosition(displVec.x * PAN_SPEED, displVec.y * PAN_SPEED);
        }

        if (mouseInput.isLeftButtonPressed()) {
            //System.out.println(selectEntity(window, scene, mouseInput.getMousePos()));

            if (selectEntity(window, scene, mouseInput.getMousePos())) {
                Vector3f pos = scene.getSelectedEntity().getPosition();
                Vector3f dir = new Vector3f(mouseInput.getDisplVec().y, 0, mouseInput.getDisplVec().x);
                dir.div(50f);
                pos.add(dir);
            }
        }

        if (inputConsumed) {
            return;
        }
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
//        rotation += 1.5f;
//        hexagonEntity.setRotation(1, 0, 0, (float) Math.toRadians(rotation));
        for (Model model: scene.getModelMap().values()) {
            for (Entity entity: model.getEntitiesList()) {
                entity.updateModelMatrix();
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

        float x = window.getMouseInput().getViewPos().x;
        float y = window.getMouseInput().getViewPos().y;
        float z = -1.0f;

        Matrix4f invProjMatrix = scene.getProjection().getInvProjMatrix();
        Vector4f mouseDir = new Vector4f(x, y, z, 1.0f);
        mouseDir.mul(invProjMatrix);
        mouseDir.z = -1.0f;
        mouseDir.w = 0.0f;

        Matrix4f invViewMatrix = scene.getCamera().getInvViewMatrix();
        mouseDir.mul(invViewMatrix);

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