package com.pieisnotpi.test.test_editor;

import com.pieisnotpi.engine.rendering.textures.Sprite;

public class SpriteSet
{
    public Sprite[] sprites;
    public int current = 0;

    public SpriteSet(Sprite[] sprites)
    {
        this.sprites = sprites;
    }

    /*public SpriteSet(int startX, int startY, int numSpritesX, int numSpritesY, int spriteRes, int texW, int texH, boolean cutMiddle)
    {
        int l = numSpritesX*numSpritesY - 1;

        sprites = new Sprite[l];

        int x = 0, y = 0;

        for(int i = 0; i < l; i++)
        {
            sprites[i] = new Sprite(texW, texH, startX + x*spriteRes, startY, );

            x++;
            if(y == 0)
            {
                if(x == numSpritesX)
                {
                    y++;
                    x--;
                }
                else
                {

                }
            }
            else if(y == numSpritesY - 1)
            {
                if(x == 0)
                {

                }
                else x--;
            }
        }

        sprites[0] = new Sprite(texW, texH, startX, startY, startX + spriteRes, startY + spriteRes);
        sprites[1] = new Sprite(texW, texH, startX + spriteRes, startY, startX + spriteRes*2, startY + spriteRes);
        sprites[2] = new Sprite(texW, texH, startX + spriteRes*2, startY, startX + spriteRes*3, startY + spriteRes);
        sprites[3] = new Sprite(texW, texH, startX + spriteRes*2, startY + spriteRes, startX + spriteRes*3, startY + spriteRes*2);
        sprites[4] = new Sprite(texW, texH, startX + spriteRes*2, startY + spriteRes*2, startX + spriteRes*3, startY + spriteRes*3);
        sprites[5] = new Sprite(texW, texH, startX + spriteRes, startY + spriteRes*2, startX + spriteRes*2, startY + spriteRes*3);
        sprites[6] = new Sprite(texW, texH, startX, startY + spriteRes*2, startX + spriteRes, startY + spriteRes*3);
        sprites[7] = new Sprite(texW, texH, startX, startY + spriteRes, startX + spriteRes, startY + spriteRes*2);
        if(!cutMiddle)
            sprites[8] = new Sprite(texW, texH, startX + spriteRes, startY + spriteRes, startX + spriteRes*2, startY + spriteRes*2);
    }*/

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
