package com.pieisnotpi.engine.rendering.cameras;

public class ProjectionMatrix extends GenericMatrix
{
    private boolean updated = true;
    
    public ProjectionMatrix()
    {
        super();
    }
    
    public ProjectionMatrix ortho(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        matrix.setOrtho(left, right, bottom, top, zNear, zFar);
        updated = needsBuilt = true;
        return this;
    }
    
    public ProjectionMatrix ortho2D(float left, float right, float bottom, float top)
    {
        matrix.setOrtho2D(left, right, bottom, top);
        updated = needsBuilt = true;
        return this;
    }
    
    public ProjectionMatrix perspective(float fov, float aspect, float zNear, float zFar)
    {
        matrix.setPerspective(fov, aspect, zNear, zFar);
        updated = needsBuilt = true;
        return this;
    }
    
    public boolean hasUpdated()
    {
        boolean temp = updated;
        updated = false;
        return temp;
    }
}
