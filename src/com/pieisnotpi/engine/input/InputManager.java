package com.pieisnotpi.engine.input;

import com.pieisnotpi.engine.input.devices.Mouse;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.window.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWJoystickCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager
{
    public List<Keybind> keybinds = new ArrayList<>();
    public List<Mousebind> mousebinds = new ArrayList<>();
    public List<Joybind> joybinds = new ArrayList<>();
    public Vector2d cursorPos = new Vector2d();
    public Vector2f localCursorPos = new Vector2f();

    private Window window;

    public Joystick[] joysticks = new Joystick[16];

    public InputManager(Window window)
    {
        this.window = window;

        for(int i = 0; i < 16; i++) if(glfwJoystickPresent(i))
        {
            joysticks[i] = new Joystick(i);
            Logger.SYSTEM.log("Joystick '" + joysticks[i].name + "' is connected");
        }

        glfwSetCursorPosCallback(window.handle, GLFWCursorPosCallback.create((windowID, xPos, yPos) ->
        {
            if(!window.focused || window.scene == null) return;

            Vector2i res = window.getWindowRes();

            cursorPos.x = xPos;
            cursorPos.y = res.y - yPos;

            localCursorPos.set((float) (((2*window.ratio)/res.x)*cursorPos.x) - window.ratio, (float) (((float) 2/res.y)*cursorPos.y) - 1);
            window.scene.onMouseMovementUnscaled(cursorPos);
            window.scene.onMouseMovement(localCursorPos);
        }));

        glfwSetScrollCallback(window.handle, GLFWScrollCallback.create((windowID, xOffset, yOffset) ->
        {
            if(window.focused && window.scene != null) window.scene.onScroll((float) xOffset, (float) yOffset);
        }));

        glfwSetKeyCallback(window.handle, GLFWKeyCallback.create((windowID, key, scanCode, action, mods) ->
        {
            if(!window.focused || window.scene == null) return;

            if(action == GLFW_RELEASE) window.scene.onKeyReleased(key, mods);
            else window.scene.onKeyPressed(key, mods);
        }));

        glfwSetJoystickCallback(GLFWJoystickCallback.create((joy, event) ->
        {
            if(event == GLFW_CONNECTED)
            {
                window.scene.onJoystickConnect(joysticks[joy] = new Joystick(joy));
                Logger.SYSTEM.log("Joystick '" + joysticks[joy].name + "' has been connected");
            }
            else
            {
                window.scene.onJoystickDisconnect(joysticks[joy]);
                Logger.SYSTEM.log("Joystick '" + joysticks[joy].name + "' has been disconnected");
                joysticks[joy] = null;
            }
        }));

        mousebinds.add(new Mousebind(Mouse.BUTTON_LEFT, (value, timeStep) -> window.scene.onLeftClick(), (value, timeStep) -> window.scene.onLeftHold(), (value, timeStep) -> window.scene.onLeftRelease()));
        mousebinds.add(new Mousebind(Mouse.BUTTON_RIGHT, (value, timeStep) -> window.scene.onRightClick(), (value, timeStep) -> window.scene.onRightHold(), (value, timeStep) -> window.scene.onRightRelease()));
        mousebinds.add(new Mousebind(Mouse.BUTTON_MIDDLE, (value, timeStep) -> window.scene.onMiddleClick(), (value, timeStep) -> window.scene.onMiddleHold(), (value, timeStep) -> window.scene.onMiddleRelease()));
    }

    public void pollInputs(float timeStep)
    {
        if(!window.focused || window.scene == null) return;

        try
        {
            keybinds.forEach(keybind ->
            {
                if(keybind.active)
                {
                    int temp = glfwGetKey(window.handle, keybind.key);

                    if(temp == GLFW_RELEASE) keybind.release();
                    else if(temp == GLFW_PRESS)
                    {
                        keybind.press();
                        keybind.hold(timeStep);
                    }
                }
            });
        }
        catch(ConcurrentModificationException e) {/**/}

        try
        {
            mousebinds.forEach(mousebind ->
            {
                if(mousebind.active)
                {
                    int temp = glfwGetMouseButton(window.handle, mousebind.button);

                    if(temp == GLFW_RELEASE) mousebind.release();
                    else if(temp == GLFW_PRESS)
                    {
                        mousebind.press();
                        mousebind.hold(timeStep);
                    }
                }
            });
        }
        catch(ConcurrentModificationException e) {/**/}

        for(Joystick joystick : joysticks) if(joystick != null) joystick.retrieveValues();

        try
        {
            joybinds.forEach(joybind ->
            {
                Joystick joystick = joysticks[joybind.joystick];

                if(joybind.enabled && joystick != null)
                {
                    float value;

                    if(!joybind.isButton)
                    {
                        FloatBuffer axis = joystick.getAxis();

                        if (axis != null && axis.limit() > joybind.axis) value = axis.get(joybind.axis);
                        else value = 0;

                        if (value > -0.1f && value < 0.1f) value = 0;
                    }
                    else
                    {
                        ByteBuffer buttons = joystick.getButtons();

                        if(buttons != null && buttons.limit() > joybind.axis && buttons.get(joybind.axis) == GLFW_TRUE) value = 1;
                        else value = 0;
                    }

                    if(value == 0 && joybind.lastStatus) joybind.release();
                    else if(value != 0)
                    {
                        joybind.press();
                        joybind.hold(value, timeStep);
                    }
                }
            });
        }
        catch(ConcurrentModificationException e) {/**/}
    }
}
