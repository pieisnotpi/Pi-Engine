package com.pieisnotpi.engine.rendering.ui.text.font;

import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;

import java.util.ArrayList;
import java.util.List;

public class Font
{
    protected List<CharSprite> sprites = new ArrayList<>();
    protected Texture texture;

    public float pixelScale, letterSpace, spaceCharSpace, newLineSpace;
    public CharSprite nullChar;

    protected Font() {}

    protected Font(Texture texture, float pixelScale, float letterSpace, float spaceCharSpace, float newLineSpace)
    {
        this.texture = texture;
        this.pixelScale = pixelScale;
        this.letterSpace = letterSpace;
        this.spaceCharSpace = spaceCharSpace;
        this.newLineSpace = newLineSpace;

        nullChar = new CharSprite(new Sprite(texture, 0, 0, 0, 0), ' ', 0, 0, 0, 0);

        init();
    }

    protected void init() {}

    public CharSprite getCharSprite(char c)
    {
        for(CharSprite cs : sprites) if(c == cs.c) return cs;
        return nullChar;
    }
}
