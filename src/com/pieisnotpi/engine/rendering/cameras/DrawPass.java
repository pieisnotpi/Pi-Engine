package com.pieisnotpi.engine.rendering.cameras;

import com.pieisnotpi.engine.rendering.Renderable;
import org.joml.Vector2i;

import java.util.List;

public abstract class DrawPass
{
    public int pass;
    public Camera camera;
    
    public DrawPass(int pass, Camera camera)
    {
        this.pass = pass;
        this.camera = camera;
    }
    
    abstract void draw(List<Renderable> renderables);
    
    public Vector2i getBufferRes()
    {
        if(camera == null || camera.getScene() == null) return null;
        return camera.getScene().window.getBufferRes();
    }
}
