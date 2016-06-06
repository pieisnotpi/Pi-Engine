package com.pieisnotpi.game.cameras;

import com.pieisnotpi.engine.game_objects.GameObject;
import com.pieisnotpi.engine.input.Joybind;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.input.joysticks.DS4;
import com.pieisnotpi.engine.input.joysticks.Joystick;
import com.pieisnotpi.engine.input.joysticks.Xbox;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Window;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.game.scenes.PauseScene;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

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
    private Vector2f lastCursorPos = new Vector2f(0, 0);

    private int joystick;
    private float joyRotAmount, mouseRotAmount, moveAmount, mouseSensitivity = 1500, joySensitivity = 180;
    private boolean hideCursor = false, ignoreNextMovement = false;

    public ControlCamera(float localX, float localY, float localWidth, float localHeight, float fov, int joystick, Scene scene)
    {
        this(0, 0, 0, localX, localY, localWidth, localHeight, fov, joystick, scene);
    }

    public ControlCamera(float x, float y, float z, float localX, float localY, float localWidth, float localHeight, float fov, int joystick, Scene scene)
    {
        super(x, y, z, localX, localY, localWidth, localHeight, fov, scene);
        this.joystick = joystick;

        if(joystick == 0)
        {
            keybinds.add(new Keybind(GLFW_KEY_W, true, (value) -> moveZ(-moveAmount), null));
            keybinds.add(new Keybind(GLFW_KEY_S, true, (value) -> moveZ(moveAmount), null));
            keybinds.add(new Keybind(GLFW_KEY_D, true, (value) -> moveX(moveAmount), null));
            keybinds.add(new Keybind(GLFW_KEY_A, true, (value) -> moveX(-moveAmount), null));
            keybinds.add(new Keybind(GLFW_KEY_SPACE, true, (value) -> moveY(moveAmount), null));
            keybinds.add(new Keybind(GLFW_KEY_LEFT_SHIFT, true, (value) -> moveY(-moveAmount), null));
            keybinds.add(new Keybind(GLFW_KEY_ESCAPE, false, (value) ->
            {
                ignoreNextMovement = hideCursor = !hideCursor;
                if(scene.window != null) if(hideCursor)
                {
                    glfwSetCursorPos(scene.window.windowID, scene.window.res.x/2, scene.window.res.y/2);
                    glfwSetInputMode(scene.window.windowID, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                    scene.gameObjects.forEach(GameObject::onMouseExited);
                }
                else glfwSetInputMode(scene.window.windowID, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            }, null));

            registerInputs();
        }
    }

    public void update()
    {
        super.update();

        joyRotAmount = joySensitivity/scene.window.getRefreshRate();
        mouseRotAmount = mouseSensitivity/scene.window.getRefreshRate();
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
            joybinds.add(new Joybind(joystick, Xbox.AXIS_RSTICK_X, false, true, (value) -> addToXRot(joySensitivity* joyRotAmount *value), null));
            joybinds.add(new Joybind(joystick, Xbox.AXIS_RSTICK_Y, false, true, (value) -> addToYRot(joySensitivity* joyRotAmount *value), null));
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
            joybinds.add(new Joybind(joystick, DS4.AXIS_RSTICK_X, false, true, (value) -> addToXRot(joySensitivity* joyRotAmount *value), null));
            joybinds.add(new Joybind(joystick, DS4.AXIS_RSTICK_Y, false, true, (value) -> addToYRot(-joySensitivity* joyRotAmount *value), null));
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

    public void onWindowChanged(Window window)
    {
        if(scene.window != null) glfwSetInputMode(scene.window.windowID, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        if(window != null)
        {
            if(!hideCursor) glfwSetInputMode(window.windowID, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            else glfwSetInputMode(window.windowID, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        }
    }

    public void onMouseMovement(Vector2f cursorPos)
    {
        if(hideCursor && !ignoreNextMovement)
        {
            if(cursorPos.x > -0.001 && cursorPos.x < 0.001) cursorPos.x = 0;
            if(cursorPos.y > -0.001 && cursorPos.y < 0.001) cursorPos.y = 0;

            float xMovement = cursorPos.x*mouseRotAmount, yMovement = cursorPos.y*mouseRotAmount;

            addToXRot(xMovement);
            addToYRot(yMovement);

            glfwSetCursorPos(scene.window.windowID, (float) scene.window.res.x/2, (float) scene.window.res.y/2);
        }

        ignoreNextMovement = false;
    }
}
