package org.lwjglb.engine.scene;

import org.joml.Vector2i;
import org.lwjglb.engine.graph.Model;

public class Grid {

    private int columns;
    private int rows;
    private Entity[][] grid;

    public Grid(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public Entity[][] makeGrid(Scene scene) {
        Entity[][] grid = new Entity[rows][columns];
        float width = 2*Hexagon.SIZE;
        float height = (float) (width * Math.sqrt(3) / 2);

        float horizSpacing = 0.75f * width;
        float vertSpacing = height;

        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                float x, z;

                if (col % 2 == 0) {
                    z = row * vertSpacing;
                }
                else {
                    z = row * vertSpacing + (height/2);
                }
                x = col * horizSpacing;

                Model model = ModelLoader.loadModel("hex-" + row + "-" + col, "resources/models/hexagon/hexagon.obj",
                        scene.getTextureCache());
                scene.addModel(model);
                Hexagon hexagon = new Hexagon("hex-" + row + "-" + col, model.getId(), new Vector2i(col, row));

                hexagon.setPosition(x, 0.0f, z);
                grid[row][col] = hexagon;
                scene.addEntity(hexagon);
            }
        }
        this.grid = grid;
        return grid;
    }

    public void redraw(Scene scene, int columns, int rows) {
        cleanup(scene);
        this.columns = columns;
        this.rows = rows;
        makeGrid(scene);
    }

    public void cleanup(Scene scene) {
        for (Entity[] row : grid) {
            for (Entity entity : row) {
                scene.removeEntity(entity);
            }
        }
    }
}
