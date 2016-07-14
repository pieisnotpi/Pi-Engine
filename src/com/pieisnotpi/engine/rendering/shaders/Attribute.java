package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.output.Logger;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;

public class Attribute
{
    public String name;
    public FloatBuffer buffer;
    public int location, handle, size;

    public Attribute(String name, int size)
    {
        this.name = name;
        this.buffer = BufferUtils.createFloatBuffer(0);
        this.size = size;
    }

    public Attribute(String name, FloatBuffer value, int location, int size)
    {
        this.name = name;
        this.buffer = value;
        this.location = location;
        this.size = size;
    }

    public void init(int programID)
    {
        location = glGetAttribLocation(programID, name);
        if(location == -1) Logger.SHADER_PROGRAM.err("Attribute not found '" + name + "' in program " + programID);
        handle = glGenBuffers();
        bindData();
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
