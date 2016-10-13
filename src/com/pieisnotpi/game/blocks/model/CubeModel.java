package com.pieisnotpi.game.blocks.model;

import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexCube;
import com.pieisnotpi.engine.rendering.textures.Sprite;

public class CubeModel
{
    public float x0, y0, z0, x1, y1, z1;
    public Sprite topSprite, sideSprite, bottomSprite;

    public CubeModel(float x0, float y0, float z0, float x1, float y1, float z1, Sprite topSprite, Sprite sideSprite, Sprite bottomSprite)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;

        this.topSprite = topSprite;
        this.sideSprite = sideSprite;
        this.bottomSprite = bottomSprite;
    }

    public String toString()
    {
        return "x0: " + x0 + ", y0: " + y0 + ", z0: " + z0 + ", x1: " + x1 + ", y1: " + y1 + ", z1: " + z1;
    }

    public TexCube toTexCube(float x, float y, float z, float scale)
    {
        return new TexCube(x + (x0 * scale), y + (y0 * scale), z + (z0 * scale), (x1 - x0)*scale, (y1 - y0)*scale, (z1 - z0)*scale, topSprite, sideSprite, bottomSprite);
    }
}
