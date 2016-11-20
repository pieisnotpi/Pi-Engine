package com.pieisnotpi.engine.rendering.shaders.buffers;

import com.pieisnotpi.engine.PiEngine;
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
    public IndexBuffer bind()
    {
        if(PiEngine.glInstance.boundIndices != handle) super.bind();
        PiEngine.glInstance.boundIndices = handle;

        return this;
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
        count = buffer.limit();
        return super.bindData();
    }
}
