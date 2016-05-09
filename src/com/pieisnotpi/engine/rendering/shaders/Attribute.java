package com.pieisnotpi.engine.rendering.shaders;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;

public class Attribute
{
    public String name;
    public FloatBuffer value;
    public int location, handle, size;

    public Attribute(String name, FloatBuffer value, int location, int size)
    {
        this.name = name;
        this.value = value;
        this.location = location;
        this.size = size;
    }

    public void bindData()
    {
        glBindBuffer(GL_ARRAY_BUFFER, handle);
        glBufferData(GL_ARRAY_BUFFER, value, GL_DYNAMIC_DRAW);
    }

    public void bindData(FloatBuffer value)
    {
        this.value = value;

        glBindBuffer(GL_ARRAY_BUFFER, handle);
        glBufferData(GL_ARRAY_BUFFER, value, GL_DYNAMIC_DRAW);
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
