package com.pieisnotpi.game.test_editor;

import com.pieisnotpi.engine.rendering.textures.Sprite;

public class SpriteSet
{
    public Sprite[] sprites;
    public int current = 0;

    public SpriteSet(Sprite[] sprites)
    {
        this.sprites = sprites;
    }

    public void addToCurrent(float amount)
    {
        current += amount;

        if(current >= sprites.length) current = 0;
        else if(current < 0) current = sprites.length - 1;
    }

    public Sprite getCurrent()
    {
        return sprites[current];
    }
}
