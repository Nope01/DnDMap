package org.lwjglb.assets;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Model;
import org.lwjglb.engine.graph.Texture;
import org.lwjglb.engine.scene.ModelLoader;

import static java.lang.Math.TAU;

public class Hexagon {
    private final String modelId;
    private final Texture texture;
    private final Material material;
    private final float[] vertices;
    private final float[] normals;
    private final int[] indices;
    private Vector4f color;

    public Hexagon(String modelId, Texture texture, Material material) {
        vertices = new float[7 * 3];
        Vector3f[] vecs = new Vector3f[7];

        vecs[0] = new Vector3f(0, 0, 0);
        Matrix3f rotation = new Matrix3f();

        for (int i = 0; i < 6; i++) {
            float angle = (float) (TAU/6);
            rotation.rotationZ(angle*i);
            vecs[i+1] = new Vector3f(1.0f, 0.0f, 0.0f);
            vecs[i+1].mul(rotation);
        }

        int count = 0;
        for (Vector3f vec : vecs) {
            vertices[count++] = vec.x;
            vertices[count++] = vec.y;
            vertices[count++] = vec.z;
        }

        normals = new float[] {
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
        };

        indices = new int[18];
        int k = 0;
        for (int i = 1; i <= 6; i++) {
            indices[k++] = 0;
            indices[k++] = i;
            indices[k++] = (i%6)+1;
        }

        this.modelId = modelId;
        this.texture = texture;
        this.material = material;
    }

    public static Model createModel(Hexagon hexagon) {
        return ModelLoader.loadModel(
                hexagon.modelId,
                hexagon.vertices,
                hexagon.normals,
                hexagon.indices,
                hexagon.texture,
                hexagon.material);
    }
}


