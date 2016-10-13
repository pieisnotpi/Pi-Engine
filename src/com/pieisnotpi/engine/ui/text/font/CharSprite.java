package com.pieisnotpi.engine.ui.text.font;

import com.pieisnotpi.engine.rendering.textures.Sprite;

public class CharSprite
{
    public Sprite sprite;

    public char c;
    public int sizeX, sizeY, offsetX, offsetY;

    public CharSprite(Sprite sprite, char c, int offsetX, int offsetY)
    {
        this.sprite = sprite;
        this.c = c;
        this.sizeX = Math.abs(sprite.x1 - sprite.x0);
        this.sizeY = Math.abs(sprite.y1 - sprite.y0);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public CharSprite(Sprite sprite, char c, int sizeX, int sizeY, int offsetX, int offsetY)
    {
        this.sprite = sprite;
        this.c = c;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
