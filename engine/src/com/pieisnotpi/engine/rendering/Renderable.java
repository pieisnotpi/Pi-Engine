package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

public class Renderable
{
    public int vertCount;
    public Vector3f[] points;
    public Vector2f[] texCoords;
    public Vector3f[] normals;
    public Color[] colors;
    public boolean enabled = true;

    public float getX() { return 0; }
    public float getY() { return 0; }
    public float getZ() { return 0; }

    public Renderable(int vertCount)
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

    public void setPoints(Vector3f... points)
    {
        if(points.length == 0) return;

        for(int a = 0, b = 0; a < this.points.length; a++, b++)
        {
            if(b >= points.length) b = 0;
            if(points[b] != null) this.points[a] = points[b];
        }

        normalize();
    }

    public void setColors(Color... colors)
    {
        initColors();

        for(int a = 0, b = 0; a < this.colors.length; a++, b++)
        {
            if(b >= colors.length) b = 0;
            if(colors[b] != null) this.colors[a] = colors[b];
        }
    }

    public void setTexCoords(Vector2f... texCoords)
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
        
        for(int i = 0; i < points.length; i++) points[i].normalize(normals[i]);
    }

    public void nullify()
    {
        points = null;
        colors = null;
        normals = null;
        texCoords = null;
    }
    
    private void initColors()
    {
        if(colors == null) colors = new Color[vertCount];
    }
    
    private void initNormals()
    {
        if(normals == null) { normals = new Vector3f[vertCount]; Arrays.fill(normals, new Vector3f());}
    }
    
    private void initTexCoords()
    {
        if(texCoords == null) texCoords = new Vector2f[vertCount];
    }

    /**
     * A method run by the shader program directly before compiling
     * @param shader The shader program currently compiling
     */

    public void preCompile(ShaderProgram shader) {}

    /**
     * A method run by the shader program directly before drawing.
     * @param shader The shader program currently drawing
     */

    public void preDraw(ShaderProgram shader) {}
}
