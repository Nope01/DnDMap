package org.lwjglb.engine.scene;

import org.joml.*;
import org.lwjglb.engine.Window;

import java.lang.Math;

public class Hexagon extends Entity implements SceneObject{

    public static final int N = 0;
    public static final int NE = 1;
    public static final int SE = 2;
    public static final int S = 3;
    public static final int SW = 4;
    public static final int NW = 5;
    public static final float SIZE = 1.0f;


    private Vector2i offsetCoords;
    private Vector3i axialCoords;
    private Vector3i[] cubeDirectionVectors;
    private float rotation;

    public Hexagon(String id, String modelId) {
        super(id, modelId);
    }

    public Hexagon(String id, String modelId, Vector2i offsetPos) {
        super(id, modelId);
        offsetCoords = offsetPos;
        cubeDirectionVectors = new Vector3i[]{
                new Vector3i(0, -1, 1), //N
                new Vector3i(1, -1, 0), //NE
                new Vector3i(1, 0, -1), //SE
                new Vector3i(0, 1, -1), //S
                new Vector3i(-1, 1, 0), //SW
                new Vector3i(-1, 0, 1), //NW
                /*
               5  0  1
                ↖ ↑ ↗
                  ·
                ↙ ↓ ↘
               4  3  2
                 */
        };
    }

    @Override
    public void render() {

    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) {


    }

    public Vector2i getOffset() {
        return offsetCoords;
    }

    //Below methods convert between coordinate types, namely cube and offset coords
    //Axial coords is the same as cube coords, but hides the S coord
    //Might as well keep all the data?
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

    //Converts direction int value to a vector for the given direction
    public Vector3i cubeDirection(int direction) {
        return cubeDirectionVectors[direction];
    }

    //Adds the directional vec to a target hexagon, returning the target neighbour hex
    public Vector3i cubeAddDirection(Vector3i hex, Vector3i vec ) {
        return new Vector3i(hex.x + vec.x, hex.y + vec.y, hex.z + vec.z);
    }

    //Given a hex and directional value, returns the coords for the neighbour in that direction
    public Vector3i getCubeNeighbour(Vector3i hex, int direction) {
        return cubeAddDirection(hex, cubeDirection(direction));
    }
}
