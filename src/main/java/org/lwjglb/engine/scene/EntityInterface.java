package org.lwjglb.engine.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface EntityInterface {
    String getId();
    String getModelId();
    Matrix4f getModelMatrix();
    Vector3f getPosition();
    Quaternionf getRotation();
    float getScale();
    void setPosition(float x, float y, float z);
    void setRotation(float x, float y, float z, float angle);
    void setScale(float scale);
    void updateModelMatrix();
    boolean getLine();
}
