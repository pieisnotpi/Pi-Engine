package com.pieisnotpi.engine.rendering.shaders.buffers;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

public class IndexBuffer extends GLBuffer<IntBuffer>
{
    public int count;

    public IndexBuffer(boolean clearMemory)
    {
        super(GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW, clearMemory);
    }

    @Override
    public IndexBuffer createBuffer(int size)
    {
        buffer = BufferUtils.createIntBuffer(size);
        return this;
    }

    @Override
    public IndexBuffer bufferDataCall()
    {
        glBufferData(bufferType, buffer, usage);
        return this;
    }

    @Override
    public GLBuffer bindData()
    {
        buffer.flip();
        count = buffer.limit();
        bind();
        bufferDataCall();
        if(clearMemory) buffer.limit(0);
        return this;
    }

    @Override
    public int size()
    {
        return count;
    }
}
