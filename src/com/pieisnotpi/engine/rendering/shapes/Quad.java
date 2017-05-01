package com.pieisnotpi.engine.rendering.shapes;

import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector3f;

public class Quad extends Renderable
{
    public Quad(float x, float y, float z, float width, float height, float depth)
    {
        super(4);

        if(height == 0) setPoints(new Vector3f(x, y, z), new Vector3f(x + width, y, z), new Vector3f(x, y, z + depth), new Vector3f(x + width, y, z + depth));
        else setPoints(new Vector3f(x, y, z), new Vector3f(x + width, y, z + depth), new Vector3f(x, y + height, z), new Vector3f(x + width, y + height, z + depth));
    }

    public Quad(Vector3f c0, Vector3f c1, Vector3f c2, Vector3f c3)
    {
        super(4);
        setPoints(c0, c1, c2, c3);
    }

    public void setPos(float x, float y, float z)
    {
        float xDif = x - getX(), yDif = y - getY(), zDif = z - getZ();

        points[0].add(xDif, yDif, zDif);
        points[1].add(xDif, yDif, zDif);
        points[2].add(xDif, yDif, zDif);
        points[3].add(xDif, yDif, zDif);
    }
    
    public void setSize(float w, float h, float d)
    {
        points[1].x = points[0].x + w;
        points[2].y = points[0].y + h;
        points[3].x = points[1].x;
        points[3].y = points[2].y;
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

    public String toString()
    {
        return String.format("x0:%fy0:%fz0:%f,x1:%fy1:%fz1:%f", points[0].x, points[0].y, points[0].z, points[4].x, points[4].y, points[4].z);
    }
}
