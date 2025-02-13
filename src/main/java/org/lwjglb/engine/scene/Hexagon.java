package org.lwjglb.engine.scene;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Hexagon extends Entity {

    private Vector2f offsetCoords;
    private Vector3f axialCoords;

    public Hexagon(String id, String modelId) {
        super(id, modelId);
    }

    public Hexagon(String id, String modelId, Vector2f offsetPos) {
        super(id, modelId);
        offsetCoords = offsetPos;
    }

    public Vector2f getOffset() {
        return offsetCoords;
    }
}
