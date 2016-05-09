package com.pieisnotpi.engine.rendering;

import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetMonitorPos;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Monitor
{
    public long monitorID;
    public GLFWVidMode vidMode;
    public Vector2i position = new Vector2i(), size = new Vector2i();

    public Monitor(long monitorID)
    {
        if(monitorID == NULL) return;

        this.monitorID = monitorID;
        vidMode = glfwGetVideoMode(monitorID);

        IntBuffer bufX = BufferUtils.createIntBuffer(1), bufY = BufferUtils.createIntBuffer(1);
        glfwGetMonitorPos(monitorID, bufX, bufY);
        position.set(bufX.get(), bufY.get());
        size.set(vidMode.width(), vidMode.height());
    }


    public boolean isPointInMonitor(Vector2i point)
    {
        return (point.x > position.x && point.y > position.y && point.x < position.x + size.x && point.y < position.y + size.y);
    }

    public Vector2i getPosition()
    {
        return position;
    }

    public Vector2i getSize()
    {
        return size;
    }

    public int getRefreshRate()
    {
        return vidMode.refreshRate();
    }
}
