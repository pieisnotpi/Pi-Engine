package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.output.Logger;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL15.*;

public class Attribute
{
    public String name;
    public FloatBuffer buffer;
    public int location = -1, bufferID, size;

    public Attribute(String name, int size, int location)
    {
        this.name = name;
        this.buffer = BufferUtils.createFloatBuffer(0);
        this.size = size;
        this.location = location;
    }

    public Attribute init()
    {
        bufferID = glGenBuffers();
        return this;
    }

    public void bind()
    {
        glBindBuffer(GL_ARRAY_BUFFER, bufferID);
    }

    public void bindData()
    {
        glBindBuffer(GL_ARRAY_BUFFER, bufferID);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    public void bindData(FloatBuffer value)
    {
        this.buffer = value;
        bindData();
    }

    public void destroy()
    {
        glDeleteBuffers(bufferID);
    }

    private void checkError()
    {
        int e = glGetError();

        while(e != GL_NO_ERROR)
        {
            Logger.SHADER_PROGRAM.err("Vertex Array Error Code: " + e);
            e = glGetError();
        }
    }
}
