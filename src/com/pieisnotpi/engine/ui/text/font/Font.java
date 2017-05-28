package com.pieisnotpi.engine.ui.text.font;

import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;

import java.util.ArrayList;
import java.util.List;

public abstract class Font
{
    protected List<CharSprite> sprites = new ArrayList<>();
    protected Texture texture;

    public boolean needsSorted = false;
    public float spaceCharSpace, newLineSpace;
    public int condensingFactor = 0;
    public CharSprite nullChar;

    protected Font() {}

    protected Font(Texture texture, float spaceCharSpace, float newLineSpace, int condensingFactor)
    {
        this.texture = texture;
        this.spaceCharSpace = spaceCharSpace;
        this.newLineSpace = newLineSpace;
        this.condensingFactor = condensingFactor;

        nullChar = new CharSprite(new Sprite(texture.image.width, texture.image.height, 0, 0, 0, 0), ' ', 0, 0, 0, 0);
    }

    public CharSprite getCharSprite(char c)
    {
        for(CharSprite cs : sprites) if(c == cs.c) return cs;
        return nullChar;
    }

    public Texture getTexture()
    {
        return texture;
    }
}
