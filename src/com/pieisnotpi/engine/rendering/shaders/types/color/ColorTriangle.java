package com.pieisnotpi.engine.rendering.shaders.types.color;

import com.pieisnotpi.engine.rendering.shapes.Triangle;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector3f;

public class ColorTriangle extends Triangle
{
    public ColorTriangle(Vector3f c0, Vector3f c1, Vector3f c2, Color c0c, Color c1c, Color c2c)
    {
        super(c0, c1, c2, c0c, c1c, c2c);
    }
}
