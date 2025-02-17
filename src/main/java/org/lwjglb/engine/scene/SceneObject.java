package org.lwjglb.engine.scene;

import org.lwjglb.engine.Window;

public interface SceneObject {

    public void render();

    public void update(Window window, Scene scene, long diffTimeMillis);
    public void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed);
}
