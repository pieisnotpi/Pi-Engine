package com.pieisnotpi.engine.rendering.textures;

import com.pieisnotpi.engine.utility.Timer;

public class AnimatedSprite extends Sprite
{
    private Sprite[] sprites;
    private Timer timer;
    private int current = 0;

    /**
     * @param frequency The amount of time, in milliseconds, between frames
     * @param frames The frames for the animation
     */

    public AnimatedSprite(int frequency, Sprite... frames)
    {
        assert frames.length != 0;

        isAnimated = true;
        sprites = frames;
        timer = new Timer(false, frequency);

        set(sprites[0]);

        timer.start();
    }

    public boolean updateAnimation()
    {
        if(timer.isFinished())
        {
            if(current == sprites.length - 1) current = -1;

            set(sprites[++current]);
            return true;
        }

        return false;
    }
}
