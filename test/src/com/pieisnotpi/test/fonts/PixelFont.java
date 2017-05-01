package com.pieisnotpi.test.fonts;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.ui.text.font.CharSprite;
import com.pieisnotpi.engine.ui.text.font.Font;

public class PixelFont extends Font
{
    private static final String name = "PixelFont";

    private PixelFont()
    {
        super(Texture.getTextureFile("text"), -1, 4, 10);

        // Register uppercase letters
        sprites.add(new CharSprite(new Sprite(texture, 1, 1, 8, 10), 'A', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 1, 17, 10), 'B', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 1, 26, 10), 'C', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 28, 1, 35, 10), 'D', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 1, 44, 10), 'E', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 1, 53, 10), 'F', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 55, 1, 62, 10), 'G', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 1, 71, 10), 'H', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 73, 1, 80, 10), 'I', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 82, 1, 89, 10), 'J', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 91, 1, 98, 10), 'K', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 100, 1, 107, 10), 'L', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 108, 1, 117, 10), 'M', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 1, 11, 8, 20), 'N', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 11, 17, 20), 'O', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 11, 26, 20), 'P', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 28, 11, 36, 20), 'Q', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 11, 44, 20), 'R', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 11, 53, 20), 'S', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 55, 11, 62, 20), 'T', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 11, 71, 20), 'U', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 73, 11, 80, 20), 'V', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 81, 11, 90, 20), 'W', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 91, 11, 98, 20), 'X', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 100, 11, 107, 20), 'Y', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 109, 11, 116, 20), 'Z', 0, 0));

        // Register lowercase letters
        sprites.add(new CharSprite(new Sprite(texture, 1, 23, 8, 30), 'a', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 21, 17, 30), 'b', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 23, 26, 30), 'c', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 28, 21, 35, 30), 'd', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 23, 44, 30), 'e', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 21, 53, 30), 'f', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 55, 21, 62, 30), 'g', 0, -2));
        sprites.add(new CharSprite(new Sprite(texture, 64, 21, 71, 30), 'h', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 75, 21, 78, 30), 'i', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 83, 21, 88, 30), 'j', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 91, 21, 97, 30), 'k', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 102, 21, 105, 30), 'l', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 109, 23, 116, 30), 'm', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 1, 33, 8, 40), 'n', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 33, 17, 40), 'o', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 31, 26, 40), 'p', 0, -2));
        sprites.add(new CharSprite(new Sprite(texture, 28, 31, 35, 40), 'q', 0, -2));
        sprites.add(new CharSprite(new Sprite(texture, 37, 33, 44, 40), 'r', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 33, 53, 40), 's', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 56, 31, 61, 40), 't', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 33, 71, 40), 'u', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 73, 33, 80, 40), 'v', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 82, 33, 89, 40), 'w', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 91, 33, 98, 40), 'x', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 100, 30, 107, 40), 'y', 0, -3));
        sprites.add(new CharSprite(new Sprite(texture, 109, 33, 116, 40), 'z', 0, 0));

        // Register numbers
        sprites.add(new CharSprite(new Sprite(texture, 2, 41, 7, 50), '1', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 41, 17, 50), '2', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 41, 26, 50), '3', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 28, 41, 35, 50), '4', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 41, 44, 50), '5', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 41, 53, 50), '6', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 55, 41, 62, 50), '7', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 41, 71, 50), '8', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 73, 41, 80, 50), '9', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 82, 41, 89, 50), '0', 0, 0));

        // Register special chars
        sprites.add(new CharSprite(new Sprite(texture, 91, 44, 98, 47), '-', 0, 3));
        sprites.add(new CharSprite(new Sprite(texture, 100, 43, 107, 48), '=', 0, 2));
        sprites.add(new CharSprite(new Sprite(texture, 109, 41, 116, 50), '?', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 3, 51, 6, 60), '!', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 9, 51, 18, 60), '@', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 52, 26, 59), '#', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 28, 51, 35, 60), '$', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 36, 51, 45, 60), '%', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 52, 53, 59), '^', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 55, 51, 63, 60), '&', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 53, 71, 59), '*', 0, 2));
        sprites.add(new CharSprite(new Sprite(texture, 74, 50, 78, 61), '(', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 78, 50, 74, 61), ')', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 82, 50, 87, 61), '{', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 87, 50, 82, 61), '}', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 92, 50, 96, 61), '[', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 96, 50, 92, 61), ']', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 91, 44, 98, 47), '_', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 62, 53, 69), '+', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 111, 59, 114, 70), '|', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 20, 63, 24, 68), ',', 0, -2));
        sprites.add(new CharSprite(new Sprite(texture, 30, 64, 33, 67), '.', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 61, 44, 70), '/', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 44, 61, 37, 70), '\\', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 57, 61, 60, 70), ':', 1, -2));
        sprites.add(new CharSprite(new Sprite(texture, 65, 61, 69, 70), ';', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 73, 62, 80, 69), '<', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 82, 62, 89, 69), '>', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 92, 63, 97, 68), '"', 0, 4));
        sprites.add(new CharSprite(new Sprite(texture, 102, 63, 105, 68), '\'', 0, 5));

        PiEngine.glInstance.registerFont(name, this);
    }

    public static PixelFont getFont()
    {
        Font f = PiEngine.glInstance.getFont(name);
        if(f != null) return (PixelFont) f;
        else
        {
            Logger.SYSTEM.debug("Creating new font: '" + name + '\'');
            return new PixelFont();
        }
    }
}
