package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.output.Logger;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class VertexArray
{
    public boolean initialized = false;
    public int handle = -1;
    public Attribute[] attributes;

    public VertexArray(Attribute... attributes)
    {
        this.attributes = attributes;
    }

    public VertexArray init()
    {
        for(Attribute a : attributes) a.init();

        handle = glGenVertexArrays();
        glBindVertexArray(handle);

        for(Attribute a : attributes)
        {
            glEnableVertexAttribArray(a.location);
            a.bind();
            glVertexAttribPointer(a.location, a.size, GL_FLOAT, false, 0, NULL);
        }

        initialized = true;

        return this;
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

    public void destroy()
    {
        initialized = false;
        glDeleteVertexArrays(handle);
        for(Attribute a : attributes) a.destroy();
    }

    public void finalize() throws Throwable
    {
        super.finalize();
        destroy();
    }

    private void checkError(String phase)
    {
        int e = glGetError();

        while(e != GL_NO_ERROR)
        {
            Logger.SHADER_PROGRAM.err(String.format("Vertex array error in phase %s: %d", phase, e));
            e = glGetError();
        }
    }
}
