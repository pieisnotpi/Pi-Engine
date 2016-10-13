package com.pieisnotpi.engine.rendering.shaders.buffers;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBufferData;

public class Attribute extends GLBuffer<FloatBuffer>
{
    public String name;
    public int location = -1, size;

    public Attribute(String name, int size, int location, int usage, boolean clearMemory)
    {
        super(GL_ARRAY_BUFFER, usage, clearMemory);

        this.name = name;
        this.size = size;
        this.location = location;
        this.clearMemory = clearMemory;
    }

    @Override
    public Attribute bufferDataCall()
    {
        glBufferData(bufferType, buffer, usage);
        return this;
    }

    @Override
    public Attribute createBuffer(int size)
    {
        buffer = BufferUtils.createFloatBuffer(size);
        return this;
    }
}
