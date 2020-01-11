package com.pieisnotpi.engine.rendering.shaders.types.tex_c;

import com.pieisnotpi.engine.rendering.primitives.Triangle;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TexCTriangle extends Triangle
{
    public TexCTriangle(Vector3f c0, Vector3f c1, Vector3f c2, Color c0c, Color c1c, Color c2c, Vector2f c0tx, Vector2f c1tx, Vector2f c2tx)
    {
        super(c0, c1, c2);

        colors[0] = c0c;
        colors[1] = c1c;
        colors[2] = c2c;

        texCoords[0] = c0tx;
        texCoords[1] = c1tx;
        texCoords[2] = c2tx;
    }

    public void setTexCoords(Vector2f c0, Vector2f c1, Vector2f c2)
    {
        texCoords[0].set(c0);
        texCoords[1].set(c1);
        texCoords[2].set(c2);
    }

    public void setColors(Color c0, Color c1, Color c2)
    {
        colors[0] = c0;
        colors[1] = c1;
        colors[2] = c2;
    }
}
