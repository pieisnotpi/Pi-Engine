package com.pieisnotpi.engine.rendering.shapes;

import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public abstract class Circle
{
    protected float x, y, z, radius;
    protected int sides, matrixID;
    protected Material material;
    protected Scene scene;
    public List<Triangle> vertices;

    protected Circle(float x, float y, float z, float radius, int sides, Material material, int matrixID, Scene scene)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.sides = sides;
        this.material = material;
        this.matrixID = matrixID;
        this.scene = scene;
    }

    protected void assemble()
    {
        double angle = (Math.PI*2)/sides;
        vertices = new ArrayList<>(sides);

        for(int i = 0; i <= sides; i++)
        {
            double a0 = angle*i, a1 = angle*(i+1);
            float x0 = x + (float) (radius*Math.cos(a0)), y0 = y + (float) (radius*Math.sin(a0)), x1 = x + (float) (radius*Math.cos(a1)), y1 = y + (float) (radius*Math.sin(a1));

            vertices.add(assembleVertex(x0, y0, z, x1, y1));
        }
    }

    public Triangle assembleVertex(float x0, float y0, float z0, float x1, float y1)
    {
        return null;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public float getRadius()
    {
        return radius;
    }

    public void setX(float x)
    {
        float dif = this.x - x;

        this.x = x;

        for(Triangle triangle : vertices) triangle.setX(triangle.getX() + dif, 1);
    }

    public void setY(float y)
    {
        float dif = this.y - y;

        System.out.println(dif);

        this.y = y;

        for(Triangle triangle : vertices) triangle.setY(triangle.getY() + dif, 1);
    }
}
