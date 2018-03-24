package com.pieisnotpi.engine.rendering.cameras;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

abstract class GenericMatrix
{
    Matrix4f matrix;
    FloatBuffer buffer;
    boolean needsBuilt = true;
    
    protected GenericMatrix()
    {
        matrix = new Matrix4f();
        buffer = BufferUtils.createFloatBuffer(16);
    }
    
    protected void build()
    {
        getMatrix().get(buffer);
        needsBuilt = false;
    }
    
    public Matrix4f getMatrix()
    {
        return matrix;
    }
    
    public FloatBuffer getBuffer()
    {
        if(needsBuilt) build();
        return buffer;
    }
}
