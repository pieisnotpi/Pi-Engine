package com.pieisnotpi.engine.rendering.shaders;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class VertexArray
{
    public int handle, programHandle;
    public Attribute[] attributes;

    public VertexArray(Attribute... attributes)
    {
        this.attributes = attributes;
    }

    public void init(int programHandle)
    {
        this.programHandle = programHandle;

        int[] handles = new int[attributes.length];
        glGenBuffers(handles);

        for(int i = 0; i < attributes.length; i++)
        {
            Attribute a = attributes[i];
            a.location = glGetAttribLocation(programHandle, a.name);
            a.handle = handles[i];
            a.bindData();
        }

        handle = glGenVertexArrays();
        glBindVertexArray(handle);

        for(Attribute attribute : attributes)
        {
            glEnableVertexAttribArray(attribute.location);
            glBindBuffer(GL_ARRAY_BUFFER, attribute.handle);
            glVertexAttribPointer(attribute.location, attribute.size, GL_FLOAT, false, 0, NULL);
        }
    }

    public void bind()
    {
        glBindVertexArray(handle);
    }

    public void bindAttribute(String name, FloatBuffer value)
    {
        for(Attribute attribute : attributes)
        {
            if(attribute.name.equals(name))
            {
                attribute.buffer = value;
                attribute.bindData();

                break;
            }
        }
    }

    public String toString()
    {
        String temp = "";

        for(int i = 0; i < attributes.length; i++) temp += "a" + i + ": (" + attributes[i] + "), ";

        return temp;
    }

    public void finalize() throws Throwable
    {
        super.finalize();

        glDeleteVertexArrays(handle);
    }
}
