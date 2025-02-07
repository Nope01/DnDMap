package org.lwjglb.game;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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
    private Entity[][] terrainEntities;

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
        camera.setPosition(0.0f, 0.5f, 4.0f);

//        String quadModelId = "quad-model";
//        Model quadModel = ModelLoader.loadModel("quad-model", "resources/models/quad/quad.obj",
//                scene.getTextureCache());
//        scene.addModel(quadModel);
//
//        int numRows = NUM_CHUNKS * 2 + 1;
//        int numCols = numRows;
//        terrainEntities = new Entity[numRows][numCols];
//        for (int j = 0; j < numRows; j++) {
//            for (int i = 0; i < numCols; i++) {
//                Entity entity = new Entity("TERRAIN_" + j + "_" + i, quadModelId);
//                terrainEntities[j][i] = entity;
//                scene.addEntity(entity);
//            }
//        }

        //Hexagon
//        Model hexagonModel = ModelLoader.loadModel("hexagon-model", "resources/models/hexagon/hexagon.obj",
//                scene.getTextureCache());
//        scene.addModel(hexagonModel);
//
//        hexagonEntity = new Entity("hexagon-entity", hexagonModel.getId());
//        hexagonEntity.setPosition(0.0f, 1.0f, 0.0f);
//        scene.addEntity(hexagonEntity);

        //Cubes
        Model cubeModel = ModelLoader.loadModel("cube-model", "resources/models/cube/cube.obj",
                scene.getTextureCache());
        scene.addModel(cubeModel);
        cubeEntity1 = new Entity("cube-entity-1", cubeModel.getId());
        cubeEntity1.setPosition(0, 2, -1);
        scene.addEntity(cubeEntity1);

        cubeEntity2 = new Entity("cube-entity-2", cubeModel.getId());
        cubeEntity2.setPosition(-2, 2, -1);
        scene.addEntity(cubeEntity2);

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

        updateTerrain(scene);

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
            selectEntity(window, scene, mouseInput.getMousePos());
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
            }
        }
        updateTerrain(scene);
    }

    public void updateTerrain(Scene scene) {
//        int cellSize = 10;
//        Camera camera = scene.getCamera();
//        Vector3f cameraPos = camera.getPosition();
//        int cellCol = (int) (cameraPos.x / cellSize);
//        int cellRow = (int) (cameraPos.z / cellSize);
//
//        int numRows = NUM_CHUNKS * 2 + 1;
//        int numCols = numRows;
//        int zOffset = -NUM_CHUNKS;
//        float scale = cellSize / 2.0f;
//        for (int j = 0; j < numRows; j++) {
//            int xOffset = -NUM_CHUNKS;
//            for (int i = 0; i < numCols; i++) {
//                Entity entity = terrainEntities[j][i];
//                entity.setScale(scale);
//                entity.setPosition((cellCol + xOffset) * 2.0f, 0, (cellRow + zOffset) * 2.0f);
//                entity.getModelMatrix().identity().scale(scale).translate(entity.getPosition());
//                xOffset++;
//            }
//            zOffset++;
//        }
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

    private void selectEntity(Window window, Scene scene, Vector2f mousePos) {
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
    }
}