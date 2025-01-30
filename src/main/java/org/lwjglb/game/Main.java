package org.lwjglb.game;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjglb.assets.Hexagon;
import org.lwjglb.assets.Plane;
import org.lwjglb.engine.*;
import org.lwjglb.engine.graph.*;
import org.lwjglb.engine.scene.Camera;
import org.lwjglb.engine.scene.Entity;
import org.lwjglb.engine.scene.ModelLoader;
import org.lwjglb.engine.scene.Scene;
import org.lwjglb.engine.scene.lights.PointLight;
import org.lwjglb.engine.scene.lights.SceneLights;
import org.lwjglb.engine.scene.lights.SpotLight;
import org.lwjglb.game.UI.Gui;
import org.lwjglb.game.UI.LightControls;
import org.lwjglb.game.UI.MouseDisplay;

import static org.lwjgl.glfw.GLFW.*;

public class Main implements IAppLogic, IGuiInstance {

    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.005f;

    private Entity cubeEntity;
    private Entity treeEntity;
    private Entity planeEntity;
    private Entity hexEntity;

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
        camera.setPosition(0.0f, 1.0f, 3.0f);

        //Cube
        Model cubeModel = ModelLoader.loadModel("cube-model", "resources/models/cube/cube.obj",
                scene.getTextureCache());
        scene.addModel(cubeModel);

        cubeEntity = new Entity("cube-entity", cubeModel.getId());
        cubeEntity.setPosition(0, 1, 0);
        cubeEntity.updateModelMatrix();
        //scene.addEntity(cubeEntity);

        //Tree
        Model treeModel = ModelLoader.loadModel("tree-model", "resources/models/tree/tree.obj",
                scene.getTextureCache());
        scene.addModel(treeModel);

        treeEntity = new Entity("tree-entity", treeModel.getId());
        treeEntity.setPosition(1, 0, 1);
        treeEntity.setScale(0.1f);
        treeEntity.updateModelMatrix();
        //scene.addEntity(treeEntity);

        //Plane
        Vector4f color = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);
        Texture planeTexture = scene.getTextureCache().createTexture("resources/models/default/default_texture.png");
        Material planeMaterial = new Material(color);
        Model planeModel = Plane.createModel(new Plane("plane-model", planeTexture, planeMaterial));
        scene.addModel(planeModel);

        planeEntity = new Entity("plane-entity", planeModel.getId());
        planeEntity.setPosition(0, -1, 0);
        planeEntity.setScale(10.0f);
        planeEntity.updateModelMatrix();
        scene.addEntity(planeEntity);

        //Hexagon
        color = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
        Texture hexTexture = scene.getTextureCache().createTexture("resources/models/default/default_texture.png");
        Material hexMaterial = new Material(color);
        Model hexModel = Hexagon.createModel(new Hexagon("hex-model", hexTexture, hexMaterial));
        scene.addModel(hexModel);

        hexEntity = new Entity("hex-entity", hexModel.getId());
        hexEntity.setPosition(1, 1, 0);
        hexEntity.updateModelMatrix();
        scene.addEntity(hexEntity);

        //Lights
        SceneLights sceneLights = new SceneLights();
        sceneLights.getAmbientLight().setIntensity(0.3f);
        scene.setSceneLights(sceneLights);
        sceneLights.getPointLights().add(new PointLight(new Vector3f(1, 1, 1),
                new Vector3f(0, 0, -1.4f), 1.0f));

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
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            camera.moveUp(move);
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.moveDown(move);
        }

        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY),
                    (float) Math.toRadians(-displVec.y * MOUSE_SENSITIVITY));
        }

        if (mouseInput.isLeftButtonPressed()) {
        }

        if (inputConsumed) {
            return;
        }
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        hexEntity.updateModelMatrix();
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
}