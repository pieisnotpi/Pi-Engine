package com.pieisnotpi.game.cameras;

import com.pieisnotpi.engine.input.Joybind;
import com.pieisnotpi.engine.input.Joystick;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.input.devices.DS4;
import com.pieisnotpi.engine.input.devices.Xbox;
import com.pieisnotpi.engine.rendering.camera.Camera;
import com.pieisnotpi.engine.rendering.window.Window;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.game.scenes.PauseScene;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A camera that automatically registers keybinds/joybinds for camera movement
 * Can be used in a scene that extends PauseScene to enable automatic pausing
 */

public class FirstPersonCamera extends Camera
{
    public List<Keybind> keybinds = new ArrayList<>();
    public List<Joybind> joybinds = new ArrayList<>();

    public int pauseSlot = -1, fullscreenSlot = -1;

    private int joystick;
    private float mouseRotAmount = 0.05f, moveAmount = 2.4f, joySensitivity = 120;
    private boolean hideCursor = false, ignoreNextMovement = false;

    public FirstPersonCamera(float fov, int joystick, Scene scene)
    {
        this(new Vector3f(0, 0, 0), fov, joystick, scene);
    }

    public FirstPersonCamera(Vector3f position, float fov, int joystick, Scene scene)
    {
        super(position, position.sub(0, 0, 10, new Vector3f()), fov, scene);
        this.joystick = joystick;

        if(joystick == 0)
        {
            keybinds.add(new Keybind(GLFW_KEY_W, null, (value, timeStep) -> moveZ(-moveAmount/scene.window.getRefreshRate()), null));
            keybinds.add(new Keybind(GLFW_KEY_S, null, (value, timeStep) -> moveZ(moveAmount/scene.window.getRefreshRate()), null));
            keybinds.add(new Keybind(GLFW_KEY_D, null, (value, timeStep) -> moveX(moveAmount/scene.window.getRefreshRate()), null));
            keybinds.add(new Keybind(GLFW_KEY_A, null, (value, timeStep) -> moveX(-moveAmount/scene.window.getRefreshRate()), null));
            keybinds.add(new Keybind(GLFW_KEY_SPACE, null, (value, timeStep) -> moveY(moveAmount/scene.window.getRefreshRate()), null));
            keybinds.add(new Keybind(GLFW_KEY_LEFT_SHIFT, null, (value, timeStep) -> moveY(-moveAmount/scene.window.getRefreshRate()), null));
            keybinds.add(new Keybind(GLFW_KEY_ESCAPE, (value, timeStep) ->
            {
                ignoreNextMovement = hideCursor = !hideCursor;
                if(scene.window != null) if(hideCursor)
                {
                    glfwSetCursorPos(scene.window.handle, scene.window.getWindowRes().x/2, scene.window.getWindowRes().y/2);
                    glfwSetInputMode(scene.window.handle, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                    scene.gameObjects.forEach(GameObject::onMouseExited);
                }
                else glfwSetInputMode(scene.window.handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            }, null, null));

            registerInputs();
        }
    }

    private void registerInputs()
    {
        keybinds.forEach(k -> scene.addKeybind(k));
        joybinds.forEach(j -> scene.addJoybind(j));
    }

    private void unregisterInputs()
    {
        keybinds.forEach(k -> scene.removeKeybind(k));
        joybinds.forEach(j -> scene.removeJoybind(j));
    }

    public void onJoystickConnect(Joystick joy)
    {
        if(joy == null || joy.joyID != joystick) return;

        if(joy.name.contains("Xbox"))
        {
            joybinds.add(new Joybind(joystick, Xbox.AXIS_RSTICK_X, false, null, (value, timeStep) -> addToYRot(joySensitivity*timeStep*value), null));
            joybinds.add(new Joybind(joystick, Xbox.AXIS_RSTICK_Y, false, null, (value, timeStep) -> addToXRot(joySensitivity*timeStep*value), null));
            joybinds.add(new Joybind(joystick, Xbox.AXIS_LSTICK_X, false, null, (value, timeStep) -> moveX(moveAmount*timeStep*value), null));
            joybinds.add(new Joybind(joystick, Xbox.AXIS_LSTICK_Y, false, null, (value, timeStep) -> moveZ(moveAmount*timeStep*-value), null));
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_A, true, null, (value, timeStep) -> moveY(moveAmount*timeStep), null));
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_B, true, null, (value, timeStep) -> moveY(-moveAmount*timeStep), null));

            if(scene.getClass().getSuperclass().equals(PauseScene.class))
            {
                PauseScene s = (PauseScene) scene;

                pauseSlot = joybinds.size();
                joybinds.add(new Joybind(joystick, Xbox.BUTTON_START, true, (value, timeStep) -> s.togglePause(), null, null));
            }

            fullscreenSlot = joybinds.size();
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_BACK, true, (value, timeStep) -> { if(scene.window != null) scene.window.setFullscreen(!scene.window.isFullscreen());}, null, null));
        }
        else
        {
            joybinds.add(new Joybind(joystick, DS4.AXIS_RSTICK_X, false, null, (value, timeStep) -> addToRot(0, joySensitivity*timeStep*value, 0), null));
            joybinds.add(new Joybind(joystick, DS4.AXIS_RSTICK_Y, false, null, (value, timeStep) -> addToRot(-joySensitivity*timeStep*value, 0, 0), null));
            joybinds.add(new Joybind(joystick, DS4.AXIS_LSTICK_X, false, null, (value, timeStep) -> moveX(moveAmount*timeStep*value), null));
            joybinds.add(new Joybind(joystick, DS4.AXIS_LSTICK_Y, false, null, (value, timeStep) -> moveZ(moveAmount*timeStep*value), null));
            joybinds.add(new Joybind(joystick, DS4.BUTTON_X, true, null, (value, timeStep) -> moveY(moveAmount*timeStep), null));
            joybinds.add(new Joybind(joystick, DS4.BUTTON_CIRCLE, true, null, (value, timeStep) -> moveY(-moveAmount*timeStep), null));

            if(scene.getClass().getSuperclass().equals(PauseScene.class))
            {
                PauseScene s = (PauseScene) scene;

                pauseSlot = joybinds.size();
                joybinds.add(new Joybind(joystick, DS4.BUTTON_OPTIONS, true, (value, timeStep) -> s.togglePause(), null, null));
            }

            fullscreenSlot = joybinds.size();
            joybinds.add(new Joybind(joystick, DS4.BUTTON_TOUCHPAD, true, (value, timeStep) -> { if(scene.window != null) scene.window.setFullscreen(!scene.window.isFullscreen());}, null, null));
        }

        registerInputs();
    }

    public void onJoystickDisconnect(Joystick joystick)
    {
        joybinds.forEach(j -> scene.removeJoybind(j));
        joybinds.clear();
    }

    public void onWindowChanged(Window oldWindow, Window newWindow)
    {
        if(oldWindow != null)
        {
            onJoystickDisconnect(oldWindow.inputManager.joysticks[joystick]);
            glfwSetInputMode(scene.window.handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }
        if(newWindow != null)
        {
            onJoystickConnect(newWindow.inputManager.joysticks[joystick]);
            if(!hideCursor) glfwSetInputMode(newWindow.handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            else glfwSetInputMode(newWindow.handle, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        }
    }

    @Override
    public void addToRot(float xr, float yr, float zr)
    {
        super.addToRot(xr, yr, zr);
        updateAL(true, false);
    }

    @Override
    public void moveX(float a)
    {
        super.moveX(a);
        updateAL(false, true);
    }

    @Override
    public void moveY(float a)
    {
        super.moveY(a);
        updateAL(false, true);
    }

    @Override
    public void moveZ(float a)
    {
        super.moveZ(a);
        updateAL(false, true);
    }

    public void onMouseMovementUnscaled(Vector2d cursorPos)
    {
        Vector2i res = scene.window.getWindowRes();
        float cx = (float) (cursorPos.x*2 - res.x), cy = (float) (cursorPos.y*2 - res.y);

        if(hideCursor && !ignoreNextMovement)
        {
            if(cx > -0.001 && cx < 0.001) cx = 0;
            if(cy > -0.001 && cy < 0.001) cy = 0;

            float nx = cy*mouseRotAmount, ny = cx*mouseRotAmount;

            addToRot(nx, ny, 0);

            glfwSetCursorPos(scene.window.handle, res.x/2f, res.y/2f);
        }

        ignoreNextMovement = false;
    }

    private void updateAL(boolean updateRot, boolean updatePos)
    {
        if(scene.cameras.size() == 1)
        {
            if(updateRot) scene.listener.setRotation(rot.x, rot.y, rot.z);
            if(updatePos) scene.listener.setPosition(pos);
        }
    }

    public void destroy()
    {
        unregisterInputs();
        super.destroy();
    }
}
