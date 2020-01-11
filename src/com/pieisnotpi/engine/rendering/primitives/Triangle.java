package com.pieisnotpi.engine.rendering.primitives;

import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Triangle extends Primitive
{
    public Triangle(Vector3f c0, Vector3f c1, Vector3f c2)
    {
        this(c0, c1, c2, null, null, null, null, null, null);
    }

    public Triangle(Vector3f c0, Vector3f c1, Vector3f c2, Color c0c, Color c1c, Color c2c)
    {
        this(c0, c1, c2, c0c, c1c, c2c, null, null, null);
    }

    public Triangle(Vector3f c0, Vector3f c1, Vector3f c2, Vector2f c0t, Vector2f c1t, Vector2f c2t)
    {
        this(c0, c1, c2, null, null, null, c0t, c1t, c2t);
    }

    public Triangle(Vector3f c0, Vector3f c1, Vector3f c2, Color c0c, Color c1c, Color c2c, Vector2f c0t, Vector2f c1t, Vector2f c2t)
    {
        super(3);

        setPoints(c0, c1, c2);
        setColors(c0c, c1c, c2c);
        setTexCoords(c0t, c1t, c2t);
    }

    public void setColor(Color color)
    {
        setColors(color, color, color);
    }

    public void setColors(Color c0, Color c1, Color c2)
    {
        initColors();

        colors[0] = (c0);
        colors[1] = (c1);
        colors[2] = (c2);
    }

    public String toString()
    {
        return "c0: " + points[0] + ", c1: " + points[1] + ", c2: " + points[2];
    }
}
