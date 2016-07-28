package com.pieisnotpi.engine.rendering.shaders;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

public class IndexBuffer
{
    public int bufferID;
    public IntBuffer buffer;

    public IndexBuffer() {}

    public IndexBuffer init()
    {
        bufferID = glGenBuffers();
        return this;
    }

    public IndexBuffer bind()
    {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferID);
        return this;
    }

    public IndexBuffer bindData(IntBuffer indices)
    {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_DYNAMIC_DRAW);
        return this;
    }
}
