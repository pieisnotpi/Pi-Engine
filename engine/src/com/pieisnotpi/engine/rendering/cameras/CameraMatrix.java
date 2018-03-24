package com.pieisnotpi.engine.rendering.cameras;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class CameraMatrix
{
    private ViewMatrix view;
    public Matrix4f matrix, projection;
    private FloatBuffer buffer;
    private boolean projectionUpdated = true, needsBuilt = true;

    public CameraMatrix(ViewMatrix view)
    {
        this.view = view;
        
        matrix = new Matrix4f();
        projection = new Matrix4f();
        buffer = BufferUtils.createFloatBuffer(16);
    }

    public CameraMatrix ortho(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        projection.setOrtho(left, right, bottom, top, zNear, zFar);
        projectionUpdated = true;
        needsBuilt = true;
        return this;
    }

    public CameraMatrix ortho2D(float left, float right, float bottom, float top)
    {
        projection.setOrtho2D(left, right, bottom, top);
        projectionUpdated = true;
        needsBuilt = true;
        return this;
    }

    public CameraMatrix perspective(float fov, float aspect, float zNear, float zFar)
    {
        projection.setPerspective(fov, aspect, zNear, zFar);
        projectionUpdated = true;
        needsBuilt = true;
        return this;
    }

    public Matrix4f getMatrix()
    {
        if(projectionUpdated || (view != null && view.hasUpdated()))
        {
            reset();
            
            matrix.mul(projection);
            if(view != null) matrix.mul(view.getMatrix());
            
            needsBuilt = true;
        }
        
        projectionUpdated = false;
        
        return matrix;
    }

    public FloatBuffer getBuffer()
    {
        if(needsBuilt) build();
        return buffer;
    }
    
    private void build()
    {
        getMatrix().get(buffer);
        needsBuilt = false;
    }
    
    private void reset()
    {
        matrix.set(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
    }
}
