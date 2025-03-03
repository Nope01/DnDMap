package org.lwjglb.engine.scene;

import org.joml.*;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    private String id;
    private String modelId;
    private Matrix4f localMatrix; //Relative to parent
    private Matrix4f modelMatrix; //Relative to world
    private Vector3f position;
    private Quaternionf rotation;
    private float scale;
    private boolean line;
    private Entity parent;
    private List<Entity> children;

    public Entity(String id, String modelId) {
        this.id = id;
        this.modelId = modelId;
        localMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = 1;
        this.line = false;
        parent = null;
        children = new ArrayList<>();
    }

    public Entity(String id, String modelId, boolean line) {
        this.id = id;
        this.modelId = modelId;
        modelMatrix = new Matrix4f();
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = 1;
        this.line = line;
    }

    public String getId() {
        return id;
    }

    public String getModelId() {
        return modelId;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setRotation(float x, float y, float z, float angle) {
        this.rotation.fromAxisAngleRad(x, y, z, angle);
    }

    public void setRotation(Vector3f rot, float angle) {
        this.rotation.fromAxisAngleRad(rot.x, rot.y, rot.z, angle);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void updateModelMatrix() {
        modelMatrix.translationRotateScale(position, rotation, scale);

        if (parent != null) {
            modelMatrix.set(parent.getModelMatrix()).mul(localMatrix);
        }
        else {
            modelMatrix.set(localMatrix);
        }

        for (Entity child : children) {
            child.updateModelMatrix();
        }
    }

    public boolean getLine() {
        return line;
    }

    public void setLine(boolean line) {
        this.line = line;
    }

    public void setParent(Entity parent) {
        //Remove from old parent
        if (this.parent != null) {
            this.parent.children.remove(this);
        }
        //Add to new parent
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
    }

    public Entity getParent() {
        return parent;
    }
    public void addChild(Entity child) {
        child.setParent(this);
    }

    public List<Entity> getChildren() {
        return children;
    }

    public List<Entity> getAllEntities() {
        List<Entity> allEntities = new ArrayList<>();
        allEntities.add(this);
        for (Entity child : children) {
            allEntities.addAll(child.getAllEntities());
        }
        return allEntities;
    }

}