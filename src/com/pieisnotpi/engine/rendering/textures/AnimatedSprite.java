package com.pieisnotpi.engine.rendering.textures;

import com.pieisnotpi.engine.utility.Timer;

public class AnimatedSprite extends Sprite
{
    private Sprite[] sprites;
    private Timer timer;
    private int current = 0, change = 1;
    private boolean shouldReverse;

    /**
     * @param shouldReverse Determines if the animation should reverse when it reaches the end of the sequence
     * @param frequency The amount of time, in milliseconds, between frames
     * @param frames The frames for the animation
     */

    public AnimatedSprite(boolean shouldReverse, int frequency, Sprite... frames)
    {
        assert frames.length != 0;
        this.shouldReverse = shouldReverse;

        isAnimated = true;
        sprites = frames;
        timer = new Timer(false, frequency);

        set(sprites[0]);

        timer.start();
    }

    /**
     * Initializer used to create automatic sprite animations based on texture sequences.
     * Given coordinates should encapsulate the entire sequence within the image
     * If the sequence spans both horizontally and vertically, the sequence should go from left to right, then step down
     *
     * @param shouldReverse Determines if the animation should reverse when it reaches the end of the sequence
     * @param frequency The amount of time, in milliseconds, between frames
     * @param x0 The x coordinate of the starting corner, based on the resolution of the image
     * @param y0 The y coordinate of the starting corner, based on the resolution of the image
     * @param x1 The x coordinate of the ending corner, based on the resolution of the image
     * @param y1 The y coordinate of the ending corner, based on the resolution of the image
     * @param indWidth The width of each individual sprite in the sequence
     * @param indHeight The height of each individual sprite in the sequence
     */

    public AnimatedSprite(boolean shouldReverse, int frequency, Texture texture, int x0, int y0, int x1, int y1, int indWidth, int indHeight)
    {
        int horizontalSprites = (x1 - x0)/indWidth, verticalSprites = (y1 - y0)/indHeight;

        sprites = new Sprite[Math.abs(horizontalSprites*verticalSprites)];

        int cx = x0, cy = y0;

        for(int i = 0; i < sprites.length; i++)
        {
            if(cx >= x1)
            {
                cx = x0;
                cy += indHeight;
            }
            if(cy >= y1) cy = y0;

            sprites[i] = new Sprite(texture, cx, cy, cx + indWidth, cy + indHeight);

            cx += indWidth;
        }

        this.shouldReverse = shouldReverse;
        isAnimated = true;
        timer = new Timer(false, frequency);

        set(sprites[0]);

        timer.start();
    }

    public boolean updateAnimation()
    {
        if(timer.isFinished())
        {
            if(current == sprites.length - 1)
            {
                if(!shouldReverse) current = -1;
                else change = -1;
            }
            else if(current == 0 && change < 0) change = 1;

            set(sprites[current += change]);
            return true;
        }

        return false;
    }
}
