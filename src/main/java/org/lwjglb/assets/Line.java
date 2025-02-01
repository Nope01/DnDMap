package org.lwjglb.assets;

import org.joml.Vector4f;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Model;
import org.lwjglb.engine.graph.Texture;
import org.lwjglb.engine.scene.ModelLoader;

public class Line {
    private final String modelId;
    private final Texture texture;
    private final Material material;
    private final float[] vertices;
    private final float[] normals;
    private final int[] indices;
    private Vector4f color;

    public Line(String modelId, Texture texture, Material material) {
        vertices = new float[] {
                0, 0, 0,
                0, 1, 0,
        };

        normals = new float[] {
                0, 1, 0,
                0, 1, 0,
        };

        indices = new int[] {
                0, 1, 0,
        };

        this.modelId = modelId;
        this.texture = texture;
        this.material = material;
    }

    public static Model createModel(Line line) {
        return ModelLoader.loadModel(
                line.modelId,
                line.vertices,
                line.normals,
                line.indices,
                line.texture,
                line.material);
    }
}
