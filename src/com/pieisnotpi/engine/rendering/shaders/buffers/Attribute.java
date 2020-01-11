package com.pieisnotpi.engine.rendering.shaders.buffers;

import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBufferData;

public class Attribute extends GLBuffer<FloatBuffer>
{
    public int location, size;

    public Attribute(String name, ShaderProgram shader, int size, int usage, boolean clearMemory)
    {
        super(GL_ARRAY_BUFFER, usage, clearMemory);

        this.size = size;
        this.location = shader.getAttribLocation(name);
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
