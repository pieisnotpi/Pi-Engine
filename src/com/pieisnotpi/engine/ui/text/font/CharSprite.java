package com.pieisnotpi.engine.ui.text.font;

import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;

public class CharSprite extends Sprite
{
    public int sizeX, sizeY, offsetX, offsetY;

    public CharSprite(int texWidth, int texHeight, int x0, int y0, int x1, int y1, int offsetX, int offsetY)
    {
        super(texWidth, texHeight, x0, y0, x1, y1);
        this.sizeX = Math.abs(x1 - x0);
        this.sizeY = Math.abs(y1 - y0);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public CharSprite(Texture texture, int x0, int y0, int x1, int y1, int offsetX, int offsetY)
    {
        super(texture, x0, y0, x1, y1);
        this.sizeX = Math.abs(x1 - x0);
        this.sizeY = Math.abs(y1 - y0);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
