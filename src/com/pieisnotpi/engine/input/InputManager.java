package com.pieisnotpi.engine.input;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;
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
    /*protected GLFWJoystickCallback joyCallback;
    protected GLFWScrollCallback scrollCallback;
    protected GLFWCursorPosCallback cursorPosCallback;*/

    public Joystick[] joysticks = new Joystick[16];

    public InputManager(Window window)
    {
        this.window = window;

        for(int i = 0; i < 16; i++) if(glfwJoystickPresent(i))
        {
            joysticks[i] = new Joystick(i);
            Logger.SYSTEM.log("Joystick '" + joysticks[i].name + "' is connected");
        }

        glfwSetCursorPosCallback(window.windowID, GLFWCursorPosCallback.create((windowID, xPos, yPos) ->
        {
            if(!window.focused) return;

            cursorPos.x = xPos;
            cursorPos.y = window.res.y - yPos;

            localCursorPos.set((float) (((2*window.ratio)/window.res.x)*cursorPos.x) - window.ratio, (float) (((float) 2/window.res.y)*cursorPos.y) - 1);
            window.scene.onMouseMovementUnscaled(cursorPos);
            window.scene.onMouseMovement(localCursorPos);
        }));

        glfwSetScrollCallback(window.windowID, GLFWScrollCallback.create((windowID, xOffset, yOffset) ->
        {
            if(window.focused) window.scene.onScroll((float) xOffset, (float) yOffset);
        }));

        glfwSetKeyCallback(window.windowID, GLFWKeyCallback.create((windowID, key, scanCode, action, mods) ->
        {
            if(!window.focused) return;

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

        mousebinds.add(new Mousebind(GLFW_MOUSE_BUTTON_1, false, (value) -> window.scene.onLeftClick(), (value) -> window.scene.onLeftRelease()));
        mousebinds.add(new Mousebind(GLFW_MOUSE_BUTTON_2, false, (value) -> window.scene.onRightClick(), (value) -> window.scene.onRightRelease()));
        mousebinds.add(new Mousebind(GLFW_MOUSE_BUTTON_3, false, (value) -> window.scene.onMiddleClick(), (value) -> window.scene.onMiddleRelease()));

        keybinds.add(new Keybind(GLFW_KEY_F3, false, (value) -> window.scene.fps.toggle(), null));
    }

    public void pollInputs()
    {
        if(!window.focused) return;

        glfwPollEvents();

        try
        {
            keybinds.forEach(keybind ->
            {
                if(keybind.active)
                {
                    int temp = glfwGetKey(window.windowID, keybind.key);

                    if(temp == GLFW_RELEASE)
                    {
                        keybind.prevStatus = false;
                        keybind.release();
                    }
                    else if(temp == GLFW_PRESS && (!keybind.prevStatus || keybind.allowHolding))
                    {
                        keybind.prevStatus = true;
                        keybind.press();
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
                    int temp = glfwGetMouseButton(window.windowID, mousebind.button);

                    if(temp == GLFW_RELEASE)
                    {
                        mousebind.prevStatus = false;
                        mousebind.release();
                    }
                    else if(temp == GLFW_PRESS && (!mousebind.prevStatus || mousebind.allowHolding))
                    {
                        mousebind.prevStatus = true;
                        mousebind.press();
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

                if (joybind.enabled && joystick != null)
                {
                    float value;

                    if (!joybind.isButton)
                    {
                        FloatBuffer axis = joystick.getAxis();

                        if (axis != null && axis.limit() > joybind.axis) value = axis.get(joybind.axis);
                        else value = 0;

                        if (value > -0.1f && value < 0.1f) value = 0;
                    }
                    else
                    {
                        ByteBuffer buttons = joystick.getButtons();

                        if (buttons != null && buttons.limit() > joybind.axis && buttons.get(joybind.axis) == GLFW_TRUE)
                            value = 1;
                        else value = 0;
                    }

                    if (value == 0 && joybind.lastValue != 0)
                    {
                        joybind.lastValue = 0;
                        joybind.release();
                    }
                    else if (value != 0 && (joybind.lastValue != value || joybind.allowHolding))
                    {
                        joybind.lastValue = value;
                        joybind.press(value);
                    }
                }
            });
        }
        catch(ConcurrentModificationException e) {/**/}
    }
}
