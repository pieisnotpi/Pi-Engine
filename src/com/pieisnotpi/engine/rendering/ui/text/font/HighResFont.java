package com.pieisnotpi.engine.rendering.ui.text.font;

import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;

public class HighResFont extends Font
{
    public HighResFont()
    {
        super(Texture.getTexture("text-hi-res"), 0.0005f, 0, 8);
    }

    protected void init()
    {
        // Register uppercase letters
        sprites.add(new CharSprite(new Sprite(texture, 2, 19, 14, 3, false, true), 'A', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 18, 19, 30, 3, false, true), 'B', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 34, 19, 46, 3, false, true), 'C', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 50, 19, 62, 3, false, true), 'D', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 66, 19, 78, 3, false, true), 'E', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 82, 19, 94, 3, false, true), 'F', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 98, 19, 110, 3, false, true), 'G', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 114, 19, 126, 3, false, true), 'H', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 130, 19, 142, 3, false, true), 'I', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 146, 19, 158, 3, false, true), 'J', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 162, 19, 174, 3, false, true), 'K', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 178, 19, 190, 3, false, true), 'L', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 192, 19, 208, 3, false, true), 'M', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 2, 41, 14, 25, false, true), 'N', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 18, 41, 30, 25, false, true), 'O', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 34, 41, 46, 25, false, true), 'P', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 50, 41, 62, 25, false, true), 'Q', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 66, 41, 78, 25, false, true), 'R', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 82, 41, 94, 25, false, true), 'S', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 98, 41, 110, 25, false, true), 'T', 0, 0));
    }
}
