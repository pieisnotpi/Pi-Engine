package com.pieisnotpi.engine.input;

import com.pieisnotpi.engine.input.joysticks.Joystick;
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
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager
{
    public List<Keybind> keybinds = new ArrayList<>();
    public List<Mousebind> mousebinds = new ArrayList<>();
    public List<Joybind> joybinds = new ArrayList<>();
    public Vector2d cursorPos = new Vector2d();
    public Vector2f localCursorPos = new Vector2f();

    public float mouseSensitivity = 18;

    private Window window;
    protected GLFWKeyCallback keyCallback;
    protected GLFWJoystickCallback joyCallback;
    protected GLFWScrollCallback scrollCallback;
    protected GLFWCursorPosCallback cursorPosCallback;

    public boolean hideCursor = false, cursorStatus = true;
    public Joystick[] joysticks = new Joystick[16];
    private float ROT_AMOUNT, MOVE_AMOUNT;

    public InputManager(Window window)
    {
        this.window = window;

        ROT_AMOUNT = 120f/window.getRefreshRate();
        MOVE_AMOUNT = 2.4f/window.getRefreshRate();

        for(int i = 0; i < 16; i++) if(glfwJoystickPresent(i))
        {
            joysticks[i] = new Joystick(i);
            Logger.SYSTEM.log("Joystick '" + joysticks[i].name + "' is connected");
        }

        glfwSetCursorPosCallback(window.windowID, cursorPosCallback = GLFWCursorPosCallback.create((windowID, xPos, yPos) ->
        {
            if(!window.focused) return;

            cursorPos.x = xPos;
            cursorPos.y = window.res.y - yPos;

            localCursorPos.set((float) (((2*window.ratio)/window.res.x)*cursorPos.x) - window.ratio, (float) (((float) 2/window.res.y)*cursorPos.y) - 1);

            if(!hideCursor) window.scene.onMouseMovement(localCursorPos);
        }));

        glfwSetScrollCallback(window.windowID, scrollCallback = GLFWScrollCallback.create((windowID, xOffset, yOffset) ->
        {
            if(window.focused) window.scene.onScroll((float) xOffset, (float) yOffset);
        }));

        glfwSetKeyCallback(window.windowID, keyCallback = GLFWKeyCallback.create((windowID, key, scanCode, action, mods) ->
        {
            if(!window.focused) return;

            if(action == GLFW_RELEASE) window.scene.onKeyReleased(key, mods);
            else window.scene.onKeyPressed(key, mods);
        }));

        glfwSetJoystickCallback(joyCallback = GLFWJoystickCallback.create((joy, event) ->
        {
            if(event == GLFW_CONNECTED)
            {
                joysticks[joy] = new Joystick(joy);
                Logger.SYSTEM.log("Joystick '" + joysticks[joy].name + "' has been connected");
            }
            else
            {
                Logger.SYSTEM.log("Joystick '" + joysticks[joy].name + "' has been disconnected");
                joysticks[joy] = null;
            }
        }));

        mousebinds.add(new Mousebind(GLFW_MOUSE_BUTTON_1, false, (value) -> window.scene.onLeftClick(), (value) -> window.scene.onLeftRelease()));
        mousebinds.add(new Mousebind(GLFW_MOUSE_BUTTON_2, false, (value) -> window.scene.onRightClick(), (value) -> window.scene.onRightRelease()));
        mousebinds.add(new Mousebind(GLFW_MOUSE_BUTTON_3, false, (value) -> window.scene.onMiddleClick(), (value) -> window.scene.onMiddleRelease()));

        keybinds.add(new Keybind(GLFW_KEY_W, true, (value) -> { if(hideCursor) window.scene.cameras.get(0).moveZ(-MOVE_AMOUNT); }, null));
        keybinds.add(new Keybind(GLFW_KEY_S, true, (value) -> { if(hideCursor) window.scene.cameras.get(0).moveZ(MOVE_AMOUNT); }, null));
        keybinds.add(new Keybind(GLFW_KEY_D, true, (value) -> { if(hideCursor) window.scene.cameras.get(0).moveX(MOVE_AMOUNT); }, null));
        keybinds.add(new Keybind(GLFW_KEY_A, true, (value) -> { if(hideCursor) window.scene.cameras.get(0).moveX(-MOVE_AMOUNT); }, null));
        keybinds.add(new Keybind(GLFW_KEY_SPACE, true, (value) -> { if(hideCursor) window.scene.cameras.get(0).moveY(MOVE_AMOUNT); }, null));
        keybinds.add(new Keybind(GLFW_KEY_LEFT_SHIFT, true, (value) -> { if(hideCursor) window.scene.cameras.get(0).moveY(-MOVE_AMOUNT); }, null));
        keybinds.add(new Keybind(GLFW_KEY_ESCAPE, false, (value) -> { hideCursor = !hideCursor; glfwSetCursorPos(window.windowID, window.res.x/2, window.res.y/2); }, null));
        keybinds.add(new Keybind(GLFW_KEY_F3, false, (value) -> window.scene.fps.toggle(), null));
    }

    public void pollInputs()
    {
        if(!window.focused) return;

        for(int i = 0; i < keybinds.size(); i++)
        {
            Keybind keybind = keybinds.get(i);

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
        }

        for(int i = 0; i < mousebinds.size(); i++)
        {
            Mousebind mousebind = mousebinds.get(i);

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
        }

        for(int i = 0; i < joybinds.size(); i++)
        {
            Joybind joybind = joybinds.get(i);
            Joystick joystick = joysticks[joybind.joystick];

            if(joybind.enabled && joystick != null)
            {
                float value;

                if(!joybind.isButton)
                {
                    FloatBuffer axis = joystick.getAxis();

                    if(axis != null && axis.limit() > joybind.axis) value = axis.get(joybind.axis);
                    else value = 0;

                    if(value > -0.15f && value < 0.15f) value = 0;
                }
                else
                {
                    ByteBuffer buttons = joystick.getButtons();

                    if(buttons != null && buttons.limit() > joybind.axis && buttons.get(joybind.axis) == GLFW_TRUE) value = 1;
                    else value = 0;
                }

                if(value == 0 && joybind.lastValue != 0)
                {
                    joybind.lastValue = 0;
                    joybind.release();
                }
                else if(value != 0 && (joybind.lastValue != value || joybind.allowHolding))
                {
                    joybind.lastValue = value;
                    joybind.press(value);
                }
            }
        }

        if(hideCursor)
        {
            if(localCursorPos.x > -0.001 && localCursorPos.x < 0.001) localCursorPos.x = 0;
            if(localCursorPos.y > -0.001 && localCursorPos.y < 0.001) localCursorPos.y = 0;

            float xMovement = localCursorPos.x*ROT_AMOUNT*mouseSensitivity, yMovement = localCursorPos.y*ROT_AMOUNT*mouseSensitivity;

            window.scene.cameras.get(0).rot.x += xMovement;
            window.scene.cameras.get(0).rot.y += yMovement;

            if(!cursorStatus)
            {
                glfwSetInputMode(window.windowID, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                cursorStatus = true;
            }

            glfwSetCursorPos(window.windowID, (float) window.res.x/2, (float) window.res.y/2);
        }
        else if(cursorStatus)
        {
            glfwSetInputMode(window.windowID, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            cursorStatus = false;
        }
    }
}
