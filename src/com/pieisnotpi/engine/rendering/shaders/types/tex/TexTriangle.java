package com.pieisnotpi.engine.rendering.shaders.types.tex;

import com.pieisnotpi.engine.rendering.primitives.Triangle;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TexTriangle extends Triangle
{
    public TexTriangle(Vector3f c0, Vector3f c1, Vector3f c2, Vector2f c0tx, Vector2f c1tx, Vector2f c2tx)
    {
        super(c0, c1, c2, c0tx, c1tx, c2tx);
    }

    public String toString()
    {
        return super.toString() + ", c0tx: " + texCoords[0] + "c1tx: " + texCoords[1] + ", c2tx: " + texCoords[2];
    }
}
