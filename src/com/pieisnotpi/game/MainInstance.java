package com.pieisnotpi.game;

import com.pieisnotpi.engine.GameInstance;
import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.input.devices.Keyboard;
import com.pieisnotpi.engine.rendering.Monitor;
import com.pieisnotpi.engine.rendering.window.Window;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.game.scenes.TestScene;
import com.pieisnotpi.game.scenes.TestScene2;
import com.pieisnotpi.game.test_editor.EditorScene;

class MainInstance extends GameInstance
{
    private Scene[][] scenes = new Scene[3][2];

    public void init()
    {
        super.init();

        Window w0, w1;

        windows.add(w0 = new Window("Pi Engine", 600, 600, PiEngine.getMonitor(0)).init());

        scenes[0][0] = new TestScene().init();
        scenes[1][0] = new TestScene2().init();
        scenes[2][0] = new EditorScene().init();

        w0.setScene(scenes[1][0]);

        w0.inputManager.keybinds.add(new Keybind(Keyboard.KEY_1, (value, timeStep) -> w0.setScene(scenes[0][0]), null, null));
        w0.inputManager.keybinds.add(new Keybind(Keyboard.KEY_2, (value, timeStep) -> w0.setScene(scenes[1][0]), null, null));
        w0.inputManager.keybinds.add(new Keybind(Keyboard.KEY_3, (value, timeStep) -> w0.setScene(scenes[2][0]), null, null));
        w0.inputManager.keybinds.add(new Keybind(Keyboard.KEY_F11, (value, timeStep) -> w0.setFullscreen(!w0.isFullscreen()), null, null));

        /*windows.add(w1 = new Window("Pi Engine 2", 600, 600, PiEngine.getMonitor(0)).init());

        scenes[0][1] = new TestScene().init();
        scenes[1][1] = new TestScene2().init();
        scenes[2][1] = new EditorScene().init();

        w1.setScene(scenes[1][1]);

        w1.inputManager.keybinds.add(new Keybind(Keyboard.KEY_1, (value, timeStep) -> w1.setScene(scenes[0][1]), null, null));
        w1.inputManager.keybinds.add(new Keybind(Keyboard.KEY_2, (value, timeStep) -> w1.setScene(scenes[1][1]), null, null));
        w1.inputManager.keybinds.add(new Keybind(Keyboard.KEY_3, (value, timeStep) -> w1.setScene(scenes[2][1]), null, null));
        w1.inputManager.keybinds.add(new Keybind(Keyboard.KEY_F11, (value, timeStep) -> w1.setFullscreen(!w1.isFullscreen()), null, null));*/
    }

    public void start()
    {
        windows.forEach(Window::show);

        super.start();
    }

    public void onMonitorDisconnect(Monitor monitor)
    {
        super.onMonitorDisconnect(monitor);

        windows.stream().filter(window -> monitor.isPointInMonitor(window.getMiddle())).forEach(window ->
        {
            window.monitor = PiEngine.getMonitor(0);
            window.center();
        });
    }
}
