package com.pieisnotpi.game;

import com.pieisnotpi.engine.GameInstance;
import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.rendering.Monitor;
import com.pieisnotpi.engine.rendering.Window;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.game.scenes.PhysicsTestScene;
import com.pieisnotpi.game.scenes.TestScene;
import com.pieisnotpi.game.scenes.TestScene2;
import com.pieisnotpi.game.test_editor.EditorScene;

import static org.lwjgl.glfw.GLFW.*;

class MainInstance extends GameInstance
{
    private Scene testScene1, testScene2, editor, physics;

    public void init()
    {
        super.init();

        windows.add(new Window("Pi Engine", 600, 600, false, 1, PiEngine.monitorPointers.get(Window.prefMonitor), 0));
        windows.forEach(Window::init);

        testScene1 = new TestScene();
        testScene2 = new TestScene2();
        editor = new EditorScene();
        physics = new PhysicsTestScene();

        testScene1.init();
        testScene2.init();
        editor.init();
        physics.init();

        windows.get(0).setScene(physics);

        for(Window w : windows)
        {
            w.inputManager.keybinds.add(new Keybind(GLFW_KEY_1, false, (value) -> w.setScene(testScene1), null));
            w.inputManager.keybinds.add(new Keybind(GLFW_KEY_2, false, (value) -> w.setScene(testScene2), null));
            w.inputManager.keybinds.add(new Keybind(GLFW_KEY_3, false, (value) -> w.setScene(editor), null));
            w.inputManager.keybinds.add(new Keybind(GLFW_KEY_4, false, (value) -> w.setScene(physics), null));
        }
    }

    public void start()
    {
        windows.forEach(Window::show);

        super.start();
    }

    public void onMonitorDisconnect(Monitor monitor)
    {
        super.onMonitorDisconnect(monitor);

        windows.stream().filter(window -> monitor.isPointInMonitor(window.middle)).forEach(window ->
        {
            window.monitor = Window.getPrefMonitor();
            window.center();
        });
    }
}
