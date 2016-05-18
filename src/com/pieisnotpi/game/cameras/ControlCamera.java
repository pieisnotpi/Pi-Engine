package com.pieisnotpi.game.cameras;

import com.pieisnotpi.engine.input.Joybind;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.input.joysticks.DS4;
import com.pieisnotpi.engine.input.joysticks.Joystick;
import com.pieisnotpi.engine.input.joysticks.Xbox;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.game.scenes.PauseScene;

import java.util.ArrayList;
import java.util.List;

/**
 * A camera that automatically registers keybinds/joybinds for camera movement
 * Can be used in a scene that extends PauseScene to enable automatic pausing
 */

public class ControlCamera extends Camera
{
    public List<Keybind> keybinds = new ArrayList<>();
    public List<Joybind> joybinds = new ArrayList<>();
    public int pauseSlot = -1, fullscreenSlot = -1;
    private String lastJoyName = "";
    private int joystick;
    private float joySensitivity = 1.5f, rotAmount, moveAmount;

    public ControlCamera(float localX, float localY, float localWidth, float localHeight, float fov, int joystick, Scene scene)
    {
        this(0, 0, 0, localX, localY, localWidth, localHeight, fov, joystick, scene);
    }

    public ControlCamera(float x, float y, float z, float localX, float localY, float localWidth, float localHeight, float fov, int joystick, Scene scene)
    {
        super(x, y, z, localX, localY, localWidth, localHeight, fov, scene);
        this.joystick = joystick;
    }

    public void update()
    {
        super.update();

        rotAmount = 120f/scene.window.getRefreshRate();
        moveAmount = 2.4f/scene.window.getRefreshRate();

        if(scene.window != null)
        {
            Joystick joy = scene.window.inputManager.joysticks[joystick];
            if(joy != null && !joy.name.contentEquals(lastJoyName)) updateJoystickBindings(joy);
        }
    }

    public void registerInputs()
    {
        for(Keybind keybind : keybinds) scene.addKeybind(keybind);
        for(Joybind joybind : joybinds) scene.addJoybind(joybind);
    }

    public void unregisterInputs()
    {
        for(Keybind keybind : keybinds) scene.removeKeybind(keybind);
        for(Joybind joybind : joybinds) scene.removeJoybind(joybind);
    }

    private void updateJoystickBindings(Joystick joy)
    {
        lastJoyName = joy.name;

        unregisterInputs();
        joybinds.clear();

        if(lastJoyName.contains("Xbox"))
        {
            joybinds.add(new Joybind(joystick, Xbox.AXIS_RSTICK_X, false, true, (value) -> addToXRot(joySensitivity*rotAmount*value), null));
            joybinds.add(new Joybind(joystick, Xbox.AXIS_RSTICK_Y, false, true, (value) -> addToYRot(joySensitivity*rotAmount*value), null));
            joybinds.add(new Joybind(joystick, Xbox.AXIS_LSTICK_X, false, true, (value) -> moveX(moveAmount*value), null));
            joybinds.add(new Joybind(joystick, Xbox.AXIS_LSTICK_Y, false, true, (value) -> moveZ(moveAmount*-value), null));
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_A, true, true, (value) -> moveY(moveAmount), null));
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_B, true, true, (value) -> moveY(-moveAmount), null));

            if(scene.getClass().getSuperclass().equals(PauseScene.class))
            {
                PauseScene s = (PauseScene) scene;

                pauseSlot = joybinds.size();
                joybinds.add(new Joybind(joystick, Xbox.BUTTON_START, true, false, (value) -> s.togglePause(), null));
            }

            fullscreenSlot = joybinds.size();
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_BACK, true, false, (value) -> { if(scene.window != null) scene.window.setFullscreen(!scene.window.isFullscreen());}, null));
        }
        else
        {
            joybinds.add(new Joybind(joystick, DS4.AXIS_RSTICK_X, false, true, (value) -> addToXRot(joySensitivity*rotAmount*value), null));
            joybinds.add(new Joybind(joystick, DS4.AXIS_RSTICK_Y, false, true, (value) -> addToYRot(-joySensitivity*rotAmount*value), null));
            joybinds.add(new Joybind(joystick, DS4.AXIS_LSTICK_X, false, true, (value) -> moveX(moveAmount*value), null));
            joybinds.add(new Joybind(joystick, DS4.AXIS_LSTICK_Y, false, true, (value) -> moveZ(moveAmount*value), null));
            joybinds.add(new Joybind(joystick, DS4.BUTTON_X, true, true, (value) -> moveY(moveAmount), null));
            joybinds.add(new Joybind(joystick, DS4.BUTTON_CIRCLE, true, true, (value) -> moveY(-moveAmount), null));

            if(scene.getClass().getSuperclass().equals(PauseScene.class))
            {
                PauseScene s = (PauseScene) scene;

                pauseSlot = joybinds.size();
                joybinds.add(new Joybind(joystick, DS4.BUTTON_OPTIONS, true, false, (value) -> s.togglePause(), null));
            }

            fullscreenSlot = joybinds.size();
            joybinds.add(new Joybind(joystick, DS4.BUTTON_TOUCHPAD, true, false, (value) -> { if(scene.window != null) scene.window.setFullscreen(!scene.window.isFullscreen());}, null));
        }

        registerInputs();
    }
}
