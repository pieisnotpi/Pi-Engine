package com.pieisnotpi.engine.input;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;

public class Joystick
{
    public int joystick;
    public boolean connected = false;

    public Joystick(int joy)
    {
        this.joystick = joy;
    }

    public FloatBuffer getAxis()
    {
        return glfwGetJoystickAxes(joystick);
    }

    public ByteBuffer getButtons()
    {
        return glfwGetJoystickButtons(joystick);
    }
}
