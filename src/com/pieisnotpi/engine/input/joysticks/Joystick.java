package com.pieisnotpi.engine.input.joysticks;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Joystick
{
    public int joystick;
    public boolean connected = false;
    public String name;

    public Joystick(int joy)
    {
        this.joystick = joy;
        name = glfwGetJoystickName(joy);
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
