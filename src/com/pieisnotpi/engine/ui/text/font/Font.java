package com.pieisnotpi.engine.ui.text.font;

import com.pieisnotpi.engine.rendering.textures.Texture;

import java.util.HashMap;
import java.util.Map;

public abstract class Font
{
    protected Map<Character, CharSprite> sprites = new HashMap<>();
    protected Texture texture;

    protected boolean needsSorted = false;
    public float spaceCharSpace, newLineSpace;
    public int condensingFactor = 0;

    protected Font() {}

    protected Font(Texture texture, float spaceCharSpace, float newLineSpace, int condensingFactor)
    {
        this.texture = texture;
        this.spaceCharSpace = spaceCharSpace;
        this.newLineSpace = newLineSpace;
        this.condensingFactor = condensingFactor;
    }

    public CharSprite getCharSprite(char c)
    {
        return sprites.get(c);
    }

    public Texture getTexture()
    {
        return texture;
    }

    public boolean depthSort()
    {
        return needsSorted;
    }

    public float spaceCharSpace()
    {
        return spaceCharSpace;
    }

    public float newLineSpace()
    {
        return newLineSpace;
    }

    public int condensingFactor()
    {
        return condensingFactor;
    }
}
