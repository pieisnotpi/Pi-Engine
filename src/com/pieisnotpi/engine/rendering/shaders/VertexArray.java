package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class VertexArray
{
    public boolean alive = false;
    public int handle = -1;
    public Attribute[] attributes;

    public VertexArray(Attribute... attributes)
    {
        this.attributes = attributes;
        handle = glGenVertexArrays();
    }

    public VertexArray init()
    {
        glBindVertexArray(handle);

        for(Attribute a : attributes)
        {
            glEnableVertexAttribArray(a.location);
            a.bind();
            glVertexAttribPointer(a.location, a.size, GL_FLOAT, false, 0, NULL);
        }

        alive = true;

        return this;
    }

    public void bind()
    {
        if(PiEngine.glInstance.boundArray != handle) glBindVertexArray(handle);
        PiEngine.glInstance.boundArray = handle;
    }

    public String toString()
    {
        String temp = "";

        for(int i = 0; i < attributes.length; i++) temp += "a" + i + ": (" + attributes[i] + "), ";

        return temp;
    }

    public void destroy()
    {
        alive = false;
        for(Attribute a : attributes) a.destroy();
        glDeleteVertexArrays(handle);
    }

    public void finalize() throws Throwable
    {
        super.finalize();
        destroy();
    }
}
