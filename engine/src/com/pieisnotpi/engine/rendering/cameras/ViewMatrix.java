package com.pieisnotpi.engine.rendering.cameras;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class ViewMatrix extends GenericMatrix
{
    private boolean updated = true;
    
    public ViewMatrix()
    {
        matrix = new Matrix4f();
        buffer = BufferUtils.createFloatBuffer(16);
    }
    
    public ViewMatrix lookAt(Vector3f eye, Vector3f center, Vector3f up)
    {
        matrix.setLookAt(eye, center, up);
        updated = needsBuilt = true;
        return this;
    }
    
    public ViewMatrix lookAlong(Vector3f direction, Vector3f up)
    {
        matrix.setLookAlong(direction, up);
        updated = needsBuilt = true;
        return this;
    }
    
    public boolean hasUpdated()
    {
        boolean temp = updated;
        updated = false;
        return temp;
    }
    
    public void reset()
    {
        needsBuilt = true;
        matrix.set(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
    }
}
