package org.lwjglb.engine.scene;

import org.lwjglb.engine.graph.Model;
import org.lwjglb.engine.graph.TextureCache;

public class Skybox {
    private Entity skyboxEntity;
    private Model skyboxModel;

    public Skybox(String skyboxModelPath, TextureCache textureCache) {
        skyboxModel = ModelLoader.loadModel("skybox-model", skyboxModelPath, textureCache);
        skyboxEntity = new Entity("skybox-entity", skyboxModel.getId());
    }

    public Entity getSkyboxEntity() {
        return skyboxEntity;
    }

    public Model getSkyboxModel() {
        return skyboxModel;
    }
}
