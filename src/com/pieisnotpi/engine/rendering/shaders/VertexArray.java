package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class VertexArray
{
    private boolean alive = false;
    private int handle;
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
        glBindVertexArray(handle);
    }

    public String toString()
    {
        StringBuilder temp = new StringBuilder();

        for(int i = 0; i < attributes.length; i++) temp.append(String.format("a %d: (%s), ", i, attributes[i]));

        return temp.toString();
    }

    public boolean isAlive()
    {
        return alive;
    }

    public int getHandle()
    {
        return handle;
    }

    public void destroy()
    {
        alive = false;
        for(Attribute a : attributes) a.destroy();
        glDeleteVertexArrays(handle);
    }
}
