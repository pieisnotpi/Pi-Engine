package com.pieisnotpi.game;

import com.pieisnotpi.engine.GameInstance;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.rendering.Window;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.game.scenes.OptionsScene;
import com.pieisnotpi.game.scenes.PhysicsTestScene;
import com.pieisnotpi.game.scenes.TestScene;
import com.pieisnotpi.game.scenes.TestScene2;
import com.pieisnotpi.game.test_editor.EditorScene;

import static org.lwjgl.glfw.GLFW.*;

class MainInstance extends GameInstance
{
    private Window window;
    private Scene testScene1, testScene2, editor, options, physics;

    public void init()
    {
        window = new Window("Pi Engine", 600, 600, false, 0);
        window.init();

        windows.add(window);

        testScene1 = new TestScene();
        testScene2 = new TestScene2();
        editor = new EditorScene();
        options = new OptionsScene();
        physics = new PhysicsTestScene();

        testScene1.init();
        testScene2.init();
        editor.init();
        options.init();
        physics.init();

        window.setScene(physics);

        for(Window w : windows)
        {
            w.inputManager.keybinds.add(new Keybind(GLFW_KEY_1, false, (value) -> w.setScene(testScene1), null));
            w.inputManager.keybinds.add(new Keybind(GLFW_KEY_2, false, (value) -> w.setScene(testScene2), null));
            w.inputManager.keybinds.add(new Keybind(GLFW_KEY_3, false, (value) -> w.setScene(editor), null));
            w.inputManager.keybinds.add(new Keybind(GLFW_KEY_4, false, (value) -> w.setScene(physics), null));
            w.inputManager.keybinds.add(new Keybind(GLFW_KEY_O, false, (value) -> w.setScene(options), null));
        }

        super.init();
    }

    public void start()
    {
        window.show();

        super.start();
    }
}
