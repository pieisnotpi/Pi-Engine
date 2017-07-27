package com.pieisnotpi.engine.utility;

public class Color
{
    public float red, green, blue, alpha;

    public Color(String hexadecimal)
    {
        int offset = 0;
        if(hexadecimal.charAt(0) == '#') offset = 1;

        red = Integer.valueOf(hexadecimal.substring(offset, offset + 2), 16)/255f;
        green = Integer.valueOf(hexadecimal.substring(offset + 2, offset + 4), 16)/255f;
        blue = Integer.valueOf(hexadecimal.substring(offset + 4, offset + 6), 16)/255f;
        alpha = 1;
    }

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
    
    public static byte valueToByte(float value)
    {
        return (byte) ((int) (value*255) & 0xff);
    }
    
    public static float byteToValue(byte value)
    {
        return Byte.toUnsignedInt(value)/255f;
    }
}
