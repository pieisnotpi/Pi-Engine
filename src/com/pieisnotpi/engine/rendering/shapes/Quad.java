package com.pieisnotpi.engine.rendering.shapes;

import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.renderable_types.Renderable;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import static com.pieisnotpi.engine.utility.MathUtility.*;

public class Quad extends Renderable
{
    public float xRot = 0, yRot = 0, zRot = 0;

    public boolean registered = true;

    protected Quad() {}

    public Quad(float x, float y, float z, float width, float height, float depth, int shaderID, int matrixID, Scene scene)
    {
        setDefaults(4);
        drawMode = GL11.GL_TRIANGLE_STRIP;

        this.scene = scene;
        this.shaderID = shaderID;
        this.matrixID = matrixID;

        if(height == 0) setPoints(new Vector3f(x, y, z), new Vector3f(x + width, y, z), new Vector3f(x, y, z + depth), new Vector3f(x + width, y, z + depth));
        else setPoints(new Vector3f(x, y, z), new Vector3f(x + width, y, z + depth), new Vector3f(x, y + height, z), new Vector3f(x + width, y + height, z + depth));
    }

    public Quad(Vector3f c0, Vector3f c1, Vector3f c2, Vector3f c3, int shaderID, int matrixID, Scene scene)
    {
        setDefaults(4);
        drawMode = GL11.GL_TRIANGLE_STRIP;

        this.scene = scene;
        this.shaderID = shaderID;
        this.matrixID = matrixID;

        setPoints(c0, c1, c2, c3);
    }

    public float getX()
    {
        return points[0].x;
    }

    public void setX(float amount)
    {
        setX(amount, 0);
    }

    public void setX(float amount, int basePoint)
    {
        float dif = amount - points[basePoint].x;

        points[0].x += dif;
        points[1].x += dif;
        points[2].x += dif;
        points[3].x += dif;
    }

    public float getY()
    {
        return points[0].y;
    }

    public void setY(float amount)
    {
        setY(amount, 0);
    }

    public void setY(float amount, int basePoint)
    {
        float dif = amount - points[basePoint].y;

        points[0].y += dif;
        points[1].y += dif;
        points[2].y += dif;
        points[3].y += dif;
    }

    public float getZ()
    {
        return points[0].z;
    }

    public void setZ(float amount)
    {
        float dif = amount - getZ();

        points[0].z += dif;
        points[1].z += dif;
        points[2].z += dif;
        points[3].z += dif;
    }

    public void setXRot(float amount) { addToXRot(amount - zRot, points[0].y, points[0].z); }
    public void setXRot(float amount, float pointY, float pointZ) { addToXRot(amount - zRot, pointY, pointZ); }
    public void addToXRot(float amount) { addToXRot(amount, points[0].y, points[0].z); }
    public void addToXRot(float amount, float pointY, float pointZ)
    {
        zRot += amount;

        rotateAxisX(amount, pointY, pointZ, points);
    }

    public void setYRot(float amount) { addToYRot(amount - zRot, points[0].x, points[0].z); }
    public void setYRot(float amount, float pointX, float pointZ) { addToYRot(amount - zRot, pointX, pointZ); }
    public void addToYRot(float amount) { addToYRot(amount, points[0].x, points[0].z); }
    public void addToYRot(float amount, float pointX, float pointZ)
    {
        yRot += amount;

        rotateAxisY(amount, pointX, pointZ, points);
    }

    public void setZRot(float amount) { addToZRot(amount - zRot, points[0].x, points[0].y); }
    public void setZRot(float amount, float pointX, float pointY) { addToZRot(amount - zRot, pointX, pointY); }
    public void addToZRot(float amount) { addToZRot(amount, points[0].x, points[0].y); }
    public void addToZRot(float amount, float pointX, float pointY)
    {
        zRot += amount;

        rotateAxisZ(amount, pointX, pointY, points);
    }

    public void setQuadColors(Color color)
    {
        setColors(color, color, color, color);
    }

    public void setQuadColors(Color c0, Color c1, Color c2, Color c3)
    {
        setColors(c0, c1, c2, c3);
    }

    public void setQuadTexCoords(float x0, float y0, float x1, float y1)
    {
        texCoords[0].set(x0, y0);
        texCoords[1].set(x1, y0);
        texCoords[2].set(x0, y1);
        texCoords[3].set(x1, y1);
    }

    public void toggle()
    {
        if(registered) unregister();
        else register();
    }

    public void register()
    {
        scene.addRenderable(this);

        registered = true;
    }

    public void unregister()
    {
        scene.removeRenderable(this);

        registered = false;
    }

    public String toString()
    {
        return String.format("x0:%fy0:%fz0:%f,x1:%fy1:%fz1:%f", points[0].x, points[0].y, points[0].z, points[4].x, points[4].y, points[4].z);
    }
}
