package com.pieisnotpi.engine.utility;

public class Color
{
    public float red, green, blue, alpha;

    public Color(float red, float green, float blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 1;
    }

    public Color(float red, float green, float blue, float alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color set(Color color)
    {
        return set(color.red, color.green, color.blue, color.alpha);
    }

    public Color set(float red, float green, float blue)
    {
        return set(red, green, blue, 1);
    }

    public Color set(float red, float green, float blue, float alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;

         return this;
    }

    public String toString()
    {
        return "r: " + red + ", g: " + green + ", b: " + blue;
    }
}
