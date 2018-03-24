package com.pieisnotpi.engine.rendering.shaders.types.color;

import com.pieisnotpi.engine.rendering.primitives.Quad;
import com.pieisnotpi.engine.utility.Color;

public class ColorQuad extends Quad
{
    public ColorQuad(float x, float y, float z, float width, float height, float depth, Color color)
    {
        this(x, y, z, width, height, depth, color, color, color, color);
    }

    public ColorQuad(float x, float y, float z, float width, float height, float depth, Color c0c, Color c1c, Color c2c, Color c3c)
    {
        super(x, y, z, width, height, depth);
        setColors(c0c, c1c, c2c, c3c);
    }
}
