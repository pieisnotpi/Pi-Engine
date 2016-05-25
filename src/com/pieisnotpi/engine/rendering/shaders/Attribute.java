package com.pieisnotpi.engine.rendering.shaders;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;

public class Attribute
{
    public String name;
    public FloatBuffer buffer;
    public int location, handle, size;

    public Attribute(String name, int location, int size)
    {
        this.name = name;
        this.buffer = BufferUtils.createFloatBuffer(0);
        this.location = location;
        this.size = size;
    }

    public Attribute(String name, FloatBuffer value, int location, int size)
    {
        this.name = name;
        this.buffer = value;
        this.location = location;
        this.size = size;
    }

    public void bindData()
    {
        glBindBuffer(GL_ARRAY_BUFFER, handle);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);
    }

    public void bindData(FloatBuffer value)
    {
        this.buffer = value;
        bindData();
    }

    public String toString()
    {
        return "Name: " + name + ", Handle: " + handle + ", Location: " + location;
    }

    public void finalize() throws Throwable
    {
        super.finalize();
        glDeleteBuffers(handle);
    }
}
