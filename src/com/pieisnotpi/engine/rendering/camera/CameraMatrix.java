package com.pieisnotpi.engine.rendering.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class CameraMatrix
{
    private Matrix4f matrix;
    private FloatBuffer buffer;
    private boolean needsBuilt = true;

    public CameraMatrix()
    {
        matrix = new Matrix4f();
        buffer = BufferUtils.createFloatBuffer(16);
    }

    public CameraMatrix start()
    {
        matrix.set(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
        return this;
    }

    public CameraMatrix ortho(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        matrix.ortho(left, right, bottom, top, zNear, zFar);
        return this;
    }

    public CameraMatrix ortho2D(float left, float right, float bottom, float top)
    {
        matrix.ortho2D(left, right, bottom, top);
        return this;
    }

    public CameraMatrix perspective(float fov, float aspect, float zNear, float zFar)
    {
        matrix.perspective(fov, aspect, zNear, zFar);
        return this;
    }

    public CameraMatrix lookAt(Vector3f eye, Vector3f center, Vector3f up)
    {
        matrix.lookAt(eye, center, up);
        return this;
    }

    public CameraMatrix end()
    {
        needsBuilt = true;
        return this;
    }

    public Matrix4f getMatrix()
    {
        return matrix;
    }

    public FloatBuffer getBuffer()
    {
        if(needsBuilt) { needsBuilt = false; return matrix.get(buffer); }
        else return buffer;
    }
}
