package com.pieisnotpi.engine.rendering.textures;

public class Sprite
{
    // Exact coordinates for the sprite
    public int x0, x1, y0, y1, texWidth, texHeight;

    // Coordinates converted to a 0-1 scale
    public float uvx0, uvy0, uvx1, uvy1;

    public boolean isAnimated = false;

    protected Sprite() {}

    public Sprite(Texture texture, int x0, int y0, int x1, int y1)
    {
        this(texture.width, texture.height, x0, y0, x1, y1, true);
    }

    public Sprite(Texture texture, int x0, int y0, int x1, int y1, boolean flipY)
    {
        this(texture.width, texture.height, x0, y0, x1, y1, flipY);
    }

    public Sprite(int texWidth, int texHeight, int x0, int y0, int x1, int y1)
    {
        this(texWidth, texHeight, x0, y0, x1, y1, true);
    }

    public Sprite(int texWidth, int texHeight, int x0, int y0, int x1, int y1, boolean flipY)
    {
        float xMult = 1f/texWidth, yMult = 1f/texHeight;

        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.texWidth = texWidth;
        this.texHeight = texHeight;

        uvx0 = x0*xMult;
        uvx1 = x1*xMult;

        uvy0 = y0*yMult;
        uvy1 = y1*yMult;

        if(flipY)
        {
            float t = uvy0;
            uvy0 = uvy1;
            uvy1 = t;

            uvy0 -= 0.0001f;
            uvy1 += 0.0001f;
        }
        else
        {
            uvy0 += 0.0001f;
            uvy1 -= 0.0001f;
        }
    }

    public Sprite(Texture texture, float x0, float y0, float x1, float y1)
    {
        this(texture.width, texture.height, x0, y0, x1, y1, true);
    }

    public Sprite(Texture texture, float x0, float y0, float x1, float y1, boolean flipY)
    {
        this(texture.width, texture.height, x0, y0, x1, y1, flipY);
    }

    public Sprite(int texWidth, int texHeight, float x0, float y0, float x1, float y1)
    {
        this(texWidth, texHeight, x0, y0, x1, y1, true);
    }

    public Sprite(int texWidth, int texHeight, float uvx0, float uvy0, float uvx1, float uvy1, boolean flipY)
    {
        float xMult = (float) 1/texWidth, yMult = (float) 1/texHeight;

        x0 = (int) (uvx0/xMult);
        x1 = (int) (uvx1/xMult);
        y0 = (int) (uvy0/yMult);
        y1 = (int) (uvy1/yMult);

        this.uvx0 = uvx0;
        this.uvx1 = uvx1;
        this.uvy0 = uvy0;
        this.uvy1 = uvy1;
        this.texWidth = texWidth;
        this.texHeight = texHeight;

        if(flipY)
        {
            float t = this.uvy0;
            this.uvy0 = this.uvy1;
            this.uvy1 = t;

            this.uvy0 -= 0.0001f;
            this.uvy1 += 0.0001f;
        }
        else
        {
            this.uvy0 += 0.0001f;
            this.uvy1 -= 0.0001f;
        }
    }

    /**
     * A method used for animated sprites
     * @return Should return true if the animation successfully updated
     */

    public boolean updateAnimation() { return false; }

    public void set(Sprite sprite)
    {
        x0 = sprite.x0;
        x1 = sprite.x1;
        y0 = sprite.y0;
        y1 = sprite.y1;
        uvx0 = sprite.uvx0;
        uvx1 = sprite.uvx1;
        uvy0 = sprite.uvy0;
        uvy1 = sprite.uvy1;
    }
}
