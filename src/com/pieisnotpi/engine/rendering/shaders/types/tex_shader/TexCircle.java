package com.pieisnotpi.engine.rendering.shaders.types.tex_shader;

import com.pieisnotpi.engine.rendering.shapes.Circle;
import com.pieisnotpi.engine.rendering.shapes.Triangle;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TexCircle extends Circle
{
    Sprite sprite;

    public TexCircle(float x, float y, float z, float radius, int sides, Sprite sprite)
    {
        super(x, y, z, radius, sides);
        this.sprite = sprite;

        assemble();
    }

    public Triangle assembleVertex(float x0, float y0, float z0, float x1, float y1)
    {
        return new TexTriangle(new Vector3f(x0, z0, y0), new Vector3f(x, z0, y), new Vector3f(x1, z0, y1), new Vector2f(xToSpriteX(x0), yToSpriteY(y0)), new Vector2f(xToSpriteX(x), yToSpriteY(y)), new Vector2f(xToSpriteX(x1), yToSpriteY(y1)));
    }

    private float xToSpriteX(float x)
    {
        float t0 = (1/(radius*2))*(x - (this.x - radius)), t1 = (sprite.uvx1 - sprite.uvx0);
        return t0*t1 + sprite.uvx0;
    }

    private float yToSpriteY(float y)
    {
        float t0 = (1/(radius*2))*(y - (this.y - radius)), t1 = (sprite.uvy1 - sprite.uvy0);
        return t1*t0 + sprite.uvy0;
    }
}
