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
    public float letterSpace, spaceCharSpace, newLineSpace;
    public CharSprite nullChar;

    protected Font() {}

    protected Font(Texture texture, float letterSpace, float spaceCharSpace, float newLineSpace)
    {
        this.texture = texture;
        this.letterSpace = letterSpace;
        this.spaceCharSpace = spaceCharSpace;
        this.newLineSpace = newLineSpace;

        nullChar = new CharSprite(new Sprite(texture.width, texture.height, 0, 0, 0, 0), ' ', 0, 0, 0, 0);
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
