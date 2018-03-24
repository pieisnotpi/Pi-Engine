package com.pieisnotpi.engine.input;

import com.pieisnotpi.engine.input.joystick.Joybind;
import com.pieisnotpi.engine.input.joystick.Joystick;
import com.pieisnotpi.engine.input.keyboard.Keybind;
import com.pieisnotpi.engine.input.mouse.Mouse;
import com.pieisnotpi.engine.input.mouse.Mousebind;
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
    public Vector2i cursorPos = new Vector2i();
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
            
            long t = System.nanoTime();

            Vector2i res = window.getWindowRes();

            cursorPos.set((int) xPos, (int) (res.y - yPos));

            localCursorPos.set((2*window.ratio/res.x*cursorPos.x) - window.ratio, (2f/res.y*cursorPos.y) - 1);
            
            window.scene.onMouseMovement(localCursorPos, cursorPos);
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
                if(window.scene != null) window.scene.onJoystickConnect(joysticks[joy] = new Joystick(joy));
                Logger.SYSTEM.log("Joystick '" + joysticks[joy].name + "' has been connected");
            }
            else
            {
                if(window.scene != null) window.scene.onJoystickDisconnect(joysticks[joy]);
                Logger.SYSTEM.log("Joystick '" + joysticks[joy].name + "' has been disconnected");
                joysticks[joy] = null;
            }
        }));

        mousebinds.add(new Mousebind(Mouse.BUTTON_LEFT, (xPos, yPos) -> window.scene.onLeftClick(), (xPos, yPos, timeStep) -> window.scene.onLeftHold(), (xPos, yPos) -> window.scene.onLeftRelease()));
        mousebinds.add(new Mousebind(Mouse.BUTTON_RIGHT, (xPos, yPos) -> window.scene.onRightClick(), (xPos, yPos, timeStep) -> window.scene.onRightHold(), (xPos, yPos) -> window.scene.onRightRelease()));
        mousebinds.add(new Mousebind(Mouse.BUTTON_MIDDLE, (xPos, yPos) -> window.scene.onMiddleClick(), (xPos, yPos, timeStep) -> window.scene.onMiddleHold(), (xPos, yPos) -> window.scene.onMiddleRelease()));
    }
    
    public boolean getKey(int key)
    {
        return glfwGetKey(window.handle, key) > 0;
    }
    
    public boolean getMouseButton(int button)
    {
        return glfwGetMouseButton(window.handle, button) == 1;
    }
    
    public float getJoystickAxis(int joystick, int axis)
    {
        if(joysticks[joystick] == null) return 0;
        FloatBuffer values = joysticks[joystick].getAxis();
        return values.limit() > axis ? values.get(axis) : 0;
    }
    
    public boolean getJoystickButton(int joystick, int button)
    {
        if(joysticks[joystick] == null) return false;
        ByteBuffer values = joysticks[joystick].getButtons();
        return values.limit() > button && values.get(button) == 1;
    }

    public void pollInputs(float timeStep)
    {
        if(!window.focused || window.scene == null) return;
    
        for(Joystick joystick : joysticks) if(joystick != null) joystick.retrieveValues();

        try
        {
            keybinds.forEach(keybind ->
            {
                if(keybind.active)
                {
                    if(getKey(keybind.key))
                    {
                        keybind.press();
                        keybind.hold(timeStep);
                    }
                    else keybind.release();
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
                    if(getMouseButton(mousebind.button))
                    {
                        mousebind.press((int) cursorPos.x, (int) cursorPos.y);
                        mousebind.hold((int) cursorPos.x, (int) cursorPos.y, timeStep);
                    }
                    else mousebind.release((int) cursorPos.x, (int) cursorPos.y);
                }
            });
        }
        catch(ConcurrentModificationException e) {/**/}

        try
        {
            joybinds.forEach(joybind ->
            {
                Joystick joystick = joysticks[joybind.joystick];

                if(joybind.enabled && joystick != null)
                {
                    float value;

                    if(joybind.isButton) value = getJoystickButton(joybind.joystick, joybind.axis) ? 1 : 0;
                    else value = getJoystickAxis(joybind.joystick, joybind.axis);

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
