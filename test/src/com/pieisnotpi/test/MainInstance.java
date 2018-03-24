package com.pieisnotpi.test;

import com.pieisnotpi.engine.GameInstance;
import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.image.Image;
import com.pieisnotpi.engine.input.keyboard.Keybind;
import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.Monitor;
import com.pieisnotpi.engine.rendering.window.Window;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.utility.ShaderReloadUtility;
import com.pieisnotpi.test.scenes.TestScene;
import com.pieisnotpi.test.scenes.TestScene2;

import java.nio.file.Paths;

class MainInstance extends GameInstance
{
    private Scene[][] scenes = new Scene[3][2];

    public void init() throws Exception
    {
        super.init();

        Image icon16 = new Image("/assets/textures/icon_16.png"), icon32 = new Image("/assets/textures/icon_32.png");

        String shaderPath = System.getProperty("test.shader_path");
        if(shaderPath != null) shaderReload = new ShaderReloadUtility(this, Paths.get(shaderPath));

        Window w0, w1;

        windows.add(w0 = new Window("Pi Engine", 600, 600, PiEngine.getMonitor(0)).init());

        scenes[0][0] = new TestScene().init();
        scenes[1][0] = new TestScene2().init();
        //scenes[2][0] = new EditorScene().init();

        w0.setScene(scenes[1][0]);

        w0.inputManager.keybinds.add(new Keybind(Keyboard.KEY_1, () -> w0.setScene(scenes[0][0]), null, null));
        w0.inputManager.keybinds.add(new Keybind(Keyboard.KEY_2, () -> w0.setScene(scenes[1][0]), null, null));
        //w0.inputManager.keybinds.add(new Keybind(Keyboard.KEY_3, () -> w0.setScene(scenes[2][0]), null, null));
        w0.inputManager.keybinds.add(new Keybind(Keyboard.KEY_F11, () -> w0.setFullscreen(!w0.isFullscreen()), null, null));
        w0.setIcon(icon16, icon32);

        /*windows.add(w1 = new Window("Pi Engine 2", 600, 600, PiEngine.getMonitor(0)).init());

        scenes[0][1] = new TestScene().init();
        scenes[1][1] = new TestScene2().init();
        //scenes[2][1] = new EditorScene().init();

        w1.setScene(scenes[1][1]);

        w1.inputManager.keybinds.add(new Keybind(Keyboard.KEY_1, () -> w1.setScene(scenes[0][1]), null, null));
        w1.inputManager.keybinds.add(new Keybind(Keyboard.KEY_2, () -> w1.setScene(scenes[1][1]), null, null));
        w1.inputManager.keybinds.add(new Keybind(Keyboard.KEY_3, () -> w1.setScene(scenes[2][1]), null, null));
        w1.inputManager.keybinds.add(new Keybind(Keyboard.KEY_F11, () -> w1.setFullscreen(!w1.isFullscreen()), null, null));
        w1.setIcon(icon16, icon32);*/
    }

    public void start() throws Exception
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
