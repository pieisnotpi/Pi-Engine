package com.pieisnotpi.engine.rendering.ui.text.font;

import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;

public class PixelFont extends Font
{
    public PixelFont()
    {
        super(Texture.getTexture("text"), 0.001f, -1, 4);
    }

    protected void init()
    {
        // Register uppercase letters
        sprites.add(new CharSprite(new Sprite(texture, 1, 10, 8, 1, false, true), 'A', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 10, 17, 1, false, true), 'B', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 10, 26, 1, false, true), 'C', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 28, 10, 35, 1, false, true), 'D', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 10, 44, 1, false, true), 'E', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 10, 53, 1, false, true), 'F', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 55, 10, 62, 1, false, true), 'G', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 10, 71, 1, false, true), 'H', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 73, 10, 80, 1, false, true), 'I', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 82, 10, 89, 1, false, true), 'J', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 91, 10, 98, 1, false, true), 'K', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 100, 10, 107, 1, false, true), 'L', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 108, 10, 117, 1, false, true), 'M', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 1, 20, 8, 11, false, true), 'N', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 20, 17, 11, false, true), 'O', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 20, 26, 11, false, true), 'P', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 28, 20, 36, 11, false, true), 'Q', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 20, 44, 11, false, true), 'R', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 20, 53, 11, false, true), 'S', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 55, 20, 62, 11, false, true), 'T', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 20, 71, 11, false, true), 'U', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 73, 20, 80, 11, false, true), 'V', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 81, 20, 90, 11, false, true), 'W', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 91, 20, 98, 11, false, true), 'X', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 100, 20, 107, 11, false, true), 'Y', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 109, 20, 116, 11, false, true), 'Z', 0, 0));

        // Register lowercase letters
        sprites.add(new CharSprite(new Sprite(texture, 1, 30, 8, 23, false, true), 'a', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 30, 17, 21, false, true), 'b', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 30, 26, 23, false, true), 'c', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 28, 30, 35, 21, false, true), 'd', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 30, 44, 23, false, true), 'e', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 30, 53, 21, false, true), 'f', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 55, 30, 62, 21, false, true), 'g', 0, -2));
        sprites.add(new CharSprite(new Sprite(texture, 64, 30, 71, 21, false, true), 'h', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 75, 30, 78, 21, false, true), 'i', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 83, 30, 88, 21, false, true), 'j', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 91, 30, 97, 21, false, true), 'k', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 102, 30, 105, 21, false, true), 'l', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 109, 30, 116, 23, false, true), 'm', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 1, 40, 8, 33, false, true), 'n', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 40, 17, 33, false, true), 'o', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 40, 26, 31, false, true), 'p', 0, -2));
        sprites.add(new CharSprite(new Sprite(texture, 28, 40, 35, 31, false, true), 'q', 0, -2));
        sprites.add(new CharSprite(new Sprite(texture, 37, 40, 44, 33, false, true), 'r', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 40, 53, 33, false, true), 's', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 56, 40, 61, 31, false, true), 't', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 40, 71, 33, false, true), 'u', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 73, 40, 80, 33, false, true), 'v', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 82, 40, 89, 33, false, true), 'w', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 91, 40, 98, 33, false, true), 'x', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 100, 40, 107, 30, false, true), 'y', 0, -3));
        sprites.add(new CharSprite(new Sprite(texture, 109, 40, 116, 33, false, true), 'z', 0, 0));

        // Register numbers
        sprites.add(new CharSprite(new Sprite(texture, 2, 50, 7, 41, false, true), '1', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 50, 17, 41, false, true), '2', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 50, 26, 41, false, true), '3', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 28, 50, 35, 41, false, true), '4', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 50, 44, 41, false, true), '5', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 50, 53, 41, false, true), '6', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 55, 50, 62, 41, false, true), '7', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 50, 71, 41, false, true), '8', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 73, 50, 80, 41, false, true), '9', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 82, 50, 89, 41, false, true), '0', 0, 0));

        // Register special chars
        sprites.add(new CharSprite(new Sprite(texture, 91, 47, 98, 44, false, true), '-', 0, 3));
        sprites.add(new CharSprite(new Sprite(texture, 100, 48, 107, 43, false, true), '=', 0, 2));
        sprites.add(new CharSprite(new Sprite(texture, 109, 50, 116, 41, false, true), '?', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 3, 60, 6, 51, false, true), '!', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 9, 60, 18, 51, false, true), '@', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 59, 26, 52, false, true), '#', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 28, 60, 35, 51, false, true), '$', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 36, 60, 45, 51, false, true), '%', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 59, 53, 52, false, true), '^', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 55, 60, 63, 51, false, true), '&', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 59, 71, 53, false, true), '*', 0, 2));
        sprites.add(new CharSprite(new Sprite(texture, 74, 61, 78, 50, false, true), '(', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 78, 61, 74, 50, false, true), ')', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 82, 61, 87, 50, false, true), '{', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 87, 61, 82, 50, false, true), '}', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 92, 61, 96, 50, false, true), '[', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 96, 61, 92, 50, false, true), ']', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 91, 47, 98, 44, false, true), '_', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 69, 53, 62, false, true), '+', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 111, 70, 114, 59, false, true), '|', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 20, 68, 24, 63, false, true), ',', 0, -2));
        sprites.add(new CharSprite(new Sprite(texture, 30, 67, 33, 64, false, true), '.', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 70, 44, 61, false, true), '/', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 44, 70, 37, 61, false, true), '\\', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 57, 70, 60, 61, false, true), ':', 1, -2));
        sprites.add(new CharSprite(new Sprite(texture, 65, 70, 69, 61, false, true), ';', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 73, 69, 80, 62, false, true), '<', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 82, 69, 89, 62, false, true), '>', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 92, 68, 97, 63, false, true), '"', 0, 4));
        sprites.add(new CharSprite(new Sprite(texture, 102, 68, 105, 63, false, true), '\'', 0, 5));
    }
}
