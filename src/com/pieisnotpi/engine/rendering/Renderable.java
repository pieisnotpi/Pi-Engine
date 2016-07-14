package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

public abstract class Renderable
{
    public boolean shouldBeSorted = false, registered = false;
    public Scene scene;
    public Texture texture;
    public Vector3f[] points;
    public Vector2f[] texCoords;
    public Vector3f[] normals;
    public Color[] colors;
    public ShaderProgram shader = null;
    protected int shaderID, matrixID, vertCount, drawMode;

    public float getX() { return 0;}
    public float getY() { return 0;}
    public float getZ() { return 0;}

    public float getCenterX()
    {
        float cx = 0;
        for(Vector3f point : points) cx += point.x;
        return cx/points.length;
    }

    public float getCenterY()
    {
        float cy = 0;
        for(Vector3f point : points) cy += point.y;
        return cy/points.length;
    }

    public float getCenterZ()
    {
        float cz = 0;
        for(Vector3f point : points) cz += point.z;
        return cz/points.length;
    }

    public int getShaderID() { return shaderID; }
    public int getMatrixID() { return matrixID; }
    public int getVertCount() { return vertCount; }
    public int getDrawMode() { return drawMode; }
    public Texture getTexture() { return texture; }

    public void setX(float x, int index)
    {
        float dif = x - points[index].x;

        for(Vector3f point : points) point.x += dif;
    }

    public void setY(float y, int index)
    {
        float dif = y - points[index].y;

        for(Vector3f point : points) point.y += dif;
    }

    public void setZ(float z, int index)
    {
        float dif = z - points[index].z;

        for(Vector3f point : points) point.z += dif;
    }

    public void register()
    {
        if(scene != null)
        {
            scene.addRenderable(this);
            registered = true;
        }
    }

    public void unregister()
    {
        if(scene != null)
        {
            scene.removeRenderable(this);
            registered = false;
        }
    }

    public void setPoints(Vector3f... points)
    {
        if(points.length == 0) return;

        for(int a = 0, b = 0; a < this.points.length; a++, b++)
        {
            if(b >= points.length) b = 0;
            if(points[b] != null) this.points[a] = points[b];
        }
    }

    public void setColors(Color... colors)
    {
        if(colors.length == 0) return;

        for(int a = 0, b = 0; a < this.colors.length; a++, b++)
        {
            if(b >= colors.length) b = 0;
            if(colors[b] != null) this.colors[a] = colors[b];
        }
    }

    public void setTexCoords(Vector2f... texCoords)
    {
        for(int a = 0, b = 0; a < this.texCoords.length; a++, b++)
        {
            if(b >= texCoords.length) b = 0;
            if(texCoords[b] != null) this.texCoords[a] = texCoords[b];
        }
    }

    protected void setDefaults(int vertCount)
    {
        this.vertCount = vertCount;

        points = new Vector3f[vertCount];
        colors = new Color[vertCount];
        normals = new Vector3f[vertCount];
        texCoords = new Vector2f[vertCount];

        Arrays.fill(points, new Vector3f(0, 0, 0));
        Arrays.fill(colors, new Color(0, 0, 0));
        Arrays.fill(normals, new Vector3f(0, 0, 0));
        Arrays.fill(texCoords, new Vector2f(0, 0));
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
