package com.pieisnotpi.engine.rendering.primitives;

import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

public class Primitive
{
    public int vertCount;
    public Vector3f[] points;
    public Vector2f[] texCoords;
    public Vector3f[] normals;
    public Color[] colors;
    public boolean enabled = true;

    public float getX() { return points[0].x; }
    public float getY() { return points[0].y; }
    public float getZ() { return points[0].z; }

    public Primitive(int vertCount)
    {
        this.vertCount = vertCount;
        points = new Vector3f[vertCount];
    }
    
    public void setX(float x, int index)
    {
        float dif = x - points[index].x;

        for(Vector3f point : points) point.x += dif;
        normalize();
    }

    public void setY(float y, int index)
    {
        float dif = y - points[index].y;

        for(Vector3f point : points) point.y += dif;
        normalize();
    }

    public void setZ(float z, int index)
    {
        float dif = z - points[index].z;

        for(Vector3f point : points) point.z += dif;
        normalize();
    }

    protected void setPoints(Vector3f... points)
    {
        if(points.length == 0) return;

        for(int a = 0, b = 0; a < this.points.length; a++, b++)
        {
            if(b >= points.length) b = 0;
            if(points[b] != null) this.points[a] = points[b];
        }

        normalize();
    }

    protected void setColors(Color... colors)
    {
        initColors();

        for(int a = 0, b = 0; a < this.colors.length; a++, b++)
        {
            if(b >= colors.length) b = 0;
            if(colors[b] != null) this.colors[a] = colors[b];
        }
    }

    protected void setTexCoords(Vector2f... texCoords)
    {
        initTexCoords();

        for(int a = 0, b = 0; a < this.texCoords.length; a++, b++)
        {
            if(b >= texCoords.length) b = 0;
            if(texCoords[b] != null) this.texCoords[a] = texCoords[b];
        }
    }

    public void normalize()
    {
        initNormals();
        Vector3f v1 = points[1].sub(points[0], new Vector3f()), v2 = points[2].sub(points[0], new Vector3f());
        Vector3f normal = v1.cross(v2, new Vector3f()).normalize();
        Arrays.fill(normals, normal);
    }

    public void nullify()
    {
        points = null;
        colors = null;
        normals = null;
        texCoords = null;
    }
    
    protected void initColors()
    {
        if(colors == null) colors = new Color[vertCount];
    }

    protected void initNormals()
    {
        if(normals == null) { normals = new Vector3f[vertCount]; Arrays.fill(normals, new Vector3f());}
    }

    protected void initTexCoords()
    {
        if(texCoords == null) { texCoords = new Vector2f[vertCount]; Arrays.fill(texCoords, new Vector2f()); }
    }
}
