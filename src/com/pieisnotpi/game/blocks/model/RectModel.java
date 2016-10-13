package com.pieisnotpi.game.blocks.model;

import com.pieisnotpi.engine.rendering.textures.Sprite;

public class RectModel
{
    public float x0, y0, z0, x1, y1, z1;
    public Sprite sprite;

    public RectModel(float x0, float y0, float z0, float x1, float y1, float z1, Sprite sprite)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;

        this.sprite = sprite;
    }
}
