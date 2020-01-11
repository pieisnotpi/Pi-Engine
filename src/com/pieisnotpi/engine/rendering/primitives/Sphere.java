package com.pieisnotpi.engine.rendering.primitives;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class Sphere<T extends Triangle>
{
    protected int hStrides, vStrides;
    protected List<T> triangles;
    
    private final double twoPI = 2*Math.PI;
    
    protected Sphere(int strides)
    {
        vStrides = (hStrides = even(strides))/2;
        double hAngle = twoPI/hStrides, vAngle = twoPI/vStrides;
        
        triangles = new ArrayList<>();
    
        Vector3f lastC1 = new Vector3f(), lastC2 = new Vector3f();
        Vector3f c1 = new Vector3f(), c2 = new Vector3f(), c3 = new Vector3f();
        
        for(int dy = 0; dy < vStrides; dy++)
        {
            double yAngle0 = dy*vAngle, yAngle1 = (dy + 1)*vAngle;
            float y0 = (float) Math.sin(yAngle0), y1 = (float) Math.sin(yAngle1);
            
            for(int dx = 0; dx < hStrides; dx++)
            {
                double xAngle0 = dx*hAngle, xAngle1 = (dx + 1)*hAngle;
                
                float x0 = (float) Math.cos(xAngle0), x1 = (float) Math.cos(xAngle1);
                float z0 = (float) Math.sin(xAngle0), z1 = (float) Math.sin(xAngle1);
                
                if(dy*dx % 2 == 0)
                {
                    c1.set(x0, y0, z0);
                    //c2.set()
                }
            }
        }
    }
    
    private int even(int number)
    {
        if(number % 2 != 0) return number + 1;
        else return number;
    }
}
