package com.pieisnotpi.engine.rendering;

public class Color
{
    protected float red, green, blue, alpha, saturation, darkest, brightest;

    public Color(float red, float green, float blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 1;

        calcSaturation();
    }

    public Color(float red, float green, float blue, float alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;

        calcSaturation();
    }

    // Calculate current saturation
    private void calcSaturation()
    {
        darkest = Float.min(red, Float.min(green, blue));
        brightest = Float.max(red, Float.max(green, blue));

        saturation = brightest - darkest/brightest;
    }

    // Various getter/setter methods
    public float getRed()
    {
        if(red != brightest) return red + brightest * (1 - saturation);
        else return red;
    }

    public void setRed(float red)
    {
        this.red = red;
        calcSaturation();
    }

    public float getGreen()
    {
        if(green != brightest) return green + brightest * (1 - saturation);
        else return green;
    }

    public void setGreen(float green)
    {
        this.green = green;
        calcSaturation();
    }

    public float getBlue()
    {
        if(blue != brightest) return blue + brightest * (1 - saturation);
        else return blue;
    }

    public void setBlue(float blue)
    {
        this.blue = blue;
        calcSaturation();
    }

    public float getAlpha()
    {
        return alpha;
    }

    public void setAlpha(float alpha)
    {
        this.alpha = alpha;
    }

    public float getSaturation()
    {
        return saturation;
    }

    public void setSaturation(float value)
    {
        if(value >= 0 && value <= 1) saturation = value;
    }

    public void set(Color color)
    {
        set(color.red, color.green, color.blue, color.alpha);
        saturation = color.saturation;
    }

    public void set(float red, float green, float blue)
    {
        set(red, green, blue, 1);
    }

    public void set(float red, float green, float blue, float alpha)
    {
        saturation = 0;

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;

        calcSaturation();
    }

    public String toString()
    {
        return "r: " + getRed() + ", g: " + getGreen() + ", b: " + getBlue();
    }
}
