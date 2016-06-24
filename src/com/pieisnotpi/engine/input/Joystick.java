package com.pieisnotpi.engine.input;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Joystick
{
    public int joyID;
    public boolean connected = false;
    public String name;

    private FloatBuffer axis;
    private ByteBuffer buttons;

    public Joystick(int joy)
    {
        this.joyID = joy;
        name = glfwGetJoystickName(joy);
    }

    public void retrieveValues()
    {
        axis = glfwGetJoystickAxes(joyID);
        buttons = glfwGetJoystickButtons(joyID);
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
