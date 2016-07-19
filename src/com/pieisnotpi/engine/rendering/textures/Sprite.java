package com.pieisnotpi.engine.rendering.textures;

public class Sprite
{
    // Exact coordinates for the sprite
    public int x0, x1, y0, y1;

    // Coordinates converted to a 0-1 scale
    public float uvx0, uvy0, uvx1, uvy1;

    public Texture texture;
    public boolean isAnimated = false;

    protected Sprite() {}

    public Sprite(Texture texture, int x0, int y0, int x1, int y1)
    {
        this(texture, x0, y0, x1, y1, true);
    }

    public Sprite(Texture texture, int x0, int y0, int x1, int y1, boolean flipY)
    {
        float xMult = (float) 1/texture.width, yMult = (float) 1/texture.height;

        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;

        uvx0 = x0*xMult;
        uvx1 = x1*xMult;

        uvy0 = y0*yMult;
        uvy1 = y1*yMult;

        if(flipY)
        {
            float t = uvy0;
            uvy0 = uvy1;
            uvy1 = t;
        }

        uvy0 -= 0.0001f;
        uvy1 += 0.0001f;

        this.texture = texture;
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
        texture = sprite.texture;
    }

    public boolean equals(Object obj)
    {
        if(super.equals(obj)) return true;

        if(obj == null || !obj.getClass().equals(getClass())) return false;

        Sprite temp = (Sprite) obj;
        return !(texture == null || !texture.equals(temp.texture)) && temp.uvx0 == uvx0 && temp.uvy0 == uvy0 && temp.uvx1 == uvx1 && temp.uvy1 == uvy1;
    }
}
