package com.pieisnotpi.engine.input;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Joystick
{
    public int joystick;
    public boolean connected = false;
    public String name;

    private FloatBuffer axis;
    private ByteBuffer buttons;

    public Joystick(int joy)
    {
        this.joystick = joy;
        name = glfwGetJoystickName(joy);
    }

    public void retrieveValues()
    {
        axis = glfwGetJoystickAxes(joystick);
        buttons = glfwGetJoystickButtons(joystick);
    }

    public FloatBuffer getAxis()
    {
        return axis;
    }

    public ByteBuffer getButtons()
    {
        return buttons;
    }
}
