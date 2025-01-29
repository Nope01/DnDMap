package org.lwjglb.assets;

import org.joml.Vector4f;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Texture;
import org.lwjglb.engine.graph.Model;
import org.lwjglb.engine.scene.ModelLoader;

public class Plane {
    private final String modelId;
    private final Texture texture;
    private final Material material;
    private final float[] vertices;
    private final float[] normals;
    private final int[] indices;
    private Vector4f color;

    public Plane(String modelId, Texture texture, Material material) {
        vertices = new float[] {
                -1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, -1.0f,
                -1.0f, 0.0f, -1.0f,
        };

        normals = new float[] {
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
        };

        indices = new int[]{
                0, 1, 2,
                2, 3, 0,
        };

        this.modelId = modelId;
        this.texture = texture;
        this.material = material;
    }

    public static Model createModel(Plane plane) {
       return ModelLoader.loadModel(
               plane.modelId,
               plane.vertices,
               plane.normals,
               plane.indices,
               plane.texture,
               plane.material);
    }
}


