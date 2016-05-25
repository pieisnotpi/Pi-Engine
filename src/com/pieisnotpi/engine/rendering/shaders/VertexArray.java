package com.pieisnotpi.engine.rendering.shaders;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class VertexArray
{
    public int handle, programHandle;
    public List<Attribute> attributes;

    public VertexArray(Attribute... attributesA)
    {
        attributes = new ArrayList<>(Arrays.asList(attributesA));
    }

    public void init(int programHandle)
    {
        this.programHandle = programHandle;

        for(Attribute attribute : attributes)
        {
            attribute.location = glGetAttribLocation(programHandle, attribute.name);
            attribute.handle = glGenBuffers();
        }

        attributes.forEach(Attribute::bindData);

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

        for (int i = 0; i < attributes.size(); i++)
        {
            Attribute a = attributes.get(i);

            temp += "a" + i + ": (" + a + "), ";
        }

        return temp;
    }

    public void finalize() throws Throwable
    {
        super.finalize();

        glDeleteVertexArrays(handle);
    }
}
