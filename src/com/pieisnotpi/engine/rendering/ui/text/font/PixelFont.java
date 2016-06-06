package com.pieisnotpi.engine.rendering.ui.text.font;

import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;

public class PixelFont extends Font
{
    public PixelFont()
    {
        super(Texture.getTexture("text"), 0.001f, -1, 4, 10);
    }

    protected void init()
    {
        // Register uppercase letters
        sprites.add(new CharSprite(new Sprite(texture, 1, 10, 8, 1), 'A', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 10, 17, 1), 'B', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 10, 26, 1), 'C', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 28, 10, 35, 1), 'D', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 10, 44, 1), 'E', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 10, 53, 1), 'F', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 55, 10, 62, 1), 'G', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 10, 71, 1), 'H', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 73, 10, 80, 1), 'I', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 82, 10, 89, 1), 'J', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 91, 10, 98, 1), 'K', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 100, 10, 107, 1), 'L', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 108, 10, 117, 1), 'M', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 1, 20, 8, 11), 'N', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 20, 17, 11), 'O', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 20, 26, 11), 'P', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 28, 20, 36, 11), 'Q', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 20, 44, 11), 'R', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 20, 53, 11), 'S', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 55, 20, 62, 11), 'T', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 20, 71, 11), 'U', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 73, 20, 80, 11), 'V', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 81, 20, 90, 11), 'W', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 91, 20, 98, 11), 'X', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 100, 20, 107, 11), 'Y', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 109, 20, 116, 11), 'Z', 0, 0));

        // Register lowercase letters
        sprites.add(new CharSprite(new Sprite(texture, 1, 30, 8, 23), 'a', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 30, 17, 21), 'b', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 30, 26, 23), 'c', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 28, 30, 35, 21), 'd', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 30, 44, 23), 'e', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 30, 53, 21), 'f', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 55, 30, 62, 21), 'g', 0, -2));
        sprites.add(new CharSprite(new Sprite(texture, 64, 30, 71, 21), 'h', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 75, 30, 78, 21), 'i', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 83, 30, 88, 21), 'j', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 91, 30, 97, 21), 'k', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 102, 30, 105, 21), 'l', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 109, 30, 116, 23), 'm', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 1, 40, 8, 33), 'n', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 40, 17, 33), 'o', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 40, 26, 31), 'p', 0, -2));
        sprites.add(new CharSprite(new Sprite(texture, 28, 40, 35, 31), 'q', 0, -2));
        sprites.add(new CharSprite(new Sprite(texture, 37, 40, 44, 33), 'r', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 40, 53, 33), 's', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 56, 40, 61, 31), 't', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 40, 71, 33), 'u', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 73, 40, 80, 33), 'v', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 82, 40, 89, 33), 'w', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 91, 40, 98, 33), 'x', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 100, 40, 107, 30), 'y', 0, -3));
        sprites.add(new CharSprite(new Sprite(texture, 109, 40, 116, 33), 'z', 0, 0));

        // Register numbers
        sprites.add(new CharSprite(new Sprite(texture, 2, 50, 7, 41), '1', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 10, 50, 17, 41), '2', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 50, 26, 41), '3', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 28, 50, 35, 41), '4', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 50, 44, 41), '5', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 50, 53, 41), '6', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 55, 50, 62, 41), '7', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 50, 71, 41), '8', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 73, 50, 80, 41), '9', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 82, 50, 89, 41), '0', 0, 0));

        // Register special chars
        sprites.add(new CharSprite(new Sprite(texture, 91, 47, 98, 44), '-', 0, 3));
        sprites.add(new CharSprite(new Sprite(texture, 100, 48, 107, 43), '=', 0, 2));
        sprites.add(new CharSprite(new Sprite(texture, 109, 50, 116, 41), '?', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 3, 60, 6, 51), '!', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 9, 60, 18, 51), '@', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 19, 59, 26, 52), '#', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 28, 60, 35, 51), '$', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 36, 60, 45, 51), '%', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 59, 53, 52), '^', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 55, 60, 63, 51), '&', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 64, 59, 71, 53), '*', 0, 2));
        sprites.add(new CharSprite(new Sprite(texture, 74, 61, 78, 50), '(', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 78, 61, 74, 50), ')', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 82, 61, 87, 50), '{', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 87, 61, 82, 50), '}', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 92, 61, 96, 50), '[', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 96, 61, 92, 50), ']', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 91, 47, 98, 44), '_', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 46, 69, 53, 62), '+', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 111, 70, 114, 59), '|', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 20, 68, 24, 63), ',', 0, -2));
        sprites.add(new CharSprite(new Sprite(texture, 30, 67, 33, 64), '.', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 37, 70, 44, 61), '/', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 44, 70, 37, 61), '\\', 0, 0));
        sprites.add(new CharSprite(new Sprite(texture, 57, 70, 60, 61), ':', 1, -2));
        sprites.add(new CharSprite(new Sprite(texture, 65, 70, 69, 61), ';', 0, -1));
        sprites.add(new CharSprite(new Sprite(texture, 73, 69, 80, 62), '<', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 82, 69, 89, 62), '>', 0, 1));
        sprites.add(new CharSprite(new Sprite(texture, 92, 68, 97, 63), '"', 0, 4));
        sprites.add(new CharSprite(new Sprite(texture, 102, 68, 105, 63), '\'', 0, 5));
    }
}
