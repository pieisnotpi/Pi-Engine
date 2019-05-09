package com.pieisnotpi.engine.rendering.cameras;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class CameraMatrix
{
    public Matrix4f matrix, projection, view;
    private FloatBuffer buffer;
    
    public CameraMatrix(Matrix4f view)
    {
        this.view = view;
        
        matrix = new Matrix4f();
        projection = new Matrix4f();
        buffer = BufferUtils.createFloatBuffer(16);
    }

    public CameraMatrix ortho(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        projection.setOrtho(left, right, bottom, top, zNear, zFar);
        return this;
    }

    public CameraMatrix ortho2D(float left, float right, float bottom, float top)
    {
        projection.setOrtho2D(left, right, bottom, top);
        return this;
    }

    public CameraMatrix perspective(float fov, float aspect, float zNear, float zFar)
    {
        projection.setPerspective(fov, aspect, zNear, zFar);
        return this;
    }

    //TODO fix this
    public Matrix4f getMatrix()
    {
        matrix.set(projection);
        if(view != null) matrix.mul(view);

        return matrix;
    }

    public FloatBuffer getBuffer()
    {
        return buffer;
    }
    
    public void compile()
    {
        matrix.set(projection);
        if(view != null) matrix.mul(view);
        matrix.get(buffer);
    }
}
