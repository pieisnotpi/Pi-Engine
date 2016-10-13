package com.pieisnotpi.engine.rendering.shaders.buffers;

import java.nio.Buffer;

import static org.lwjgl.opengl.GL15.*;

public abstract class GLBuffer<E extends Buffer>
{
    protected int bufferID, bufferType, usage;
    public boolean clearMemory;
    public E buffer;

    protected GLBuffer(int bufferType, int usage, boolean clearMemory)
    {
        this.bufferType = bufferType;
        this.usage = usage;
        this.clearMemory = clearMemory;

        bufferID = glGenBuffers();
        createBuffer(0);
    }

    public abstract GLBuffer bufferDataCall();
    public abstract GLBuffer createBuffer(int size);

    public GLBuffer bind()
    {
        glBindBuffer(bufferType, bufferID);
        return this;
    }

    public GLBuffer bindData()
    {
        bind();
        bufferDataCall();
        if(clearMemory) buffer.limit(0);
        return this;
    }

    public GLBuffer bindData(E buffer)
    {
        this.buffer = buffer;
        bindData();
        return this;
    }

    public GLBuffer setBufferSize(int size)
    {
        if(buffer != null && buffer.limit() >= size) buffer.position(0);
        else createBuffer(size);

        return this;
    }

    public void destroy()
    {
        glDeleteBuffers(bufferID);
    }
}
