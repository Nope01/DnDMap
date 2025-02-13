package org.lwjglb.engine.scene;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Hexagon extends Entity {

    private Vector2i offsetCoords;
    private Vector3i axialCoords;
    private Vector3i[] cubeDirectionVectors;

    public Hexagon(String id, String modelId) {
        super(id, modelId);
    }

    public Hexagon(String id, String modelId, Vector2i offsetPos) {
        super(id, modelId);
        offsetCoords = offsetPos;
        cubeDirectionVectors = new Vector3i[]{
                new Vector3i(1, 0, -1), //SE
                new Vector3i(1, -1, 0), //NE
                new Vector3i(0, -1, 1), //N
                new Vector3i(-1, 0, 1), //NW
                new Vector3i(-1, 1, 0), //SW
                new Vector3i(0, 1, -1), //S
        };
    }

    public Vector2i getOffset() {
        return offsetCoords;
    }

    public Vector2i cubeToAxialCoords(Vector3i cube) {
        int q = cube.x;
        int r = cube.y;
        return new Vector2i(q, r);
    }

    public Vector3i axialToCubeCoords(Vector2i axial) {
        int q = axial.x;
        int r = axial.y;
        int s = -q-r;
        return new Vector3i(q, r, s);
    }

    public Vector2i cubeToOffsetCoords(Vector3i cube) {
        int col = cube.x;
        int row = cube.y + (cube.x - (cube.x&1))/2;
        return new Vector2i(col, row);
    }

    public Vector3i offsetToCubeCoords(Vector2i offset) {
        int q = offset.x;
        int r = offset.y - (offset.x - (offset.x&1))/2;
        int s = -q-r;
        return new Vector3i(q, r, s);
    }

    public Vector3i cubeDirection(int direction) {
        return cubeDirectionVectors[direction];
    }

    public Vector3i cubeAddDirection(Vector3i hex, Vector3i vec ) {
        return new Vector3i(hex.x + vec.x, hex.y + vec.y, hex.z + vec.z);
    }

    public Vector3i getCubeNeighbour(Vector3i hex, int direction) {
        return cubeAddDirection(hex, cubeDirection(direction));
    }
}
