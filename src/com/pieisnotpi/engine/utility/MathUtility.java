package com.pieisnotpi.engine.utility;

import org.joml.Vector3f;

public class MathUtility
{
    public static final float toRads = (float) (Math.PI/180), toDegs = (float) (180/Math.PI);

    /**
     * Rounds a float value to a specified decimal place
     * Mostly supports negative place values (rounding to places left of the decimal)
     * but may suffer from floating point imprecision issues
     * @param value Value to be rounded
     * @param place Decimal place to round to (can be negative)
     * @return Rounded float
     */
    public static float roundToDecimal(float value, int place)
    {
        if (place == 0) return Math.round(value);

        float mul = (float) Math.pow(10, place);
        return Math.round(value*mul)/mul;
    }

    /**
     * @param floats A series of floats to be compared
     * @return The smallest of the given floats
     */
    public static float smallestFloat(float... floats)
    {
        float smallest = Float.MAX_VALUE;

        for(float f : floats) if(f < smallest) smallest = f;

        return smallest;
    }

    /**
     * @param floats A series of floats to be compared
     * @return The largest of the given floats
     */
    public static float largestFloat(float... floats)
    {
        float largest = Float.MIN_VALUE;

        for(float f : floats) if(f > largest) largest = f;

        return largest;
    }

    /**
     * @param vectors A series of vectors to be averaged
     * @return The average of the given vectors
     */
    public static Vector3f averageVector(Vector3f... vectors)
    {
        float x = 0, y = 0, z = 0;

        for(Vector3f vector : vectors)
        {
            x += vector.x;
            y += vector.y;
            z += vector.z;
        }

        x /= vectors.length;
        y /= vectors.length;
        z /= vectors.length;

        return new Vector3f(x, y, z);
    }

    /**
     * @param angle The given angle (in degrees) for rotation
     * @param cy The y point of rotation
     * @param cz The z point of rotation
     * @param points The points to be rotated
     */
    public static void rotateAxisX(float angle, float cy, float cz, Vector3f... points)
    {
        double a = Math.toRadians(angle), cos = Math.cos(a), sin = Math.sin(a);

        for(Vector3f point : points)
        {
            float y = point.y - cy, z = point.z - cz;

            point.y = (float) (cy + y*cos - z*sin);
            point.z = (float) (cz + y*sin + z*cos);
        }
    }

    /**
     * @param angle The given angle (in degrees) for rotation
     * @param cx The x point of rotation
     * @param cz The z point of rotation
     * @param points The points to be rotated
     */
    public static void rotateAxisY(float angle, float cx, float cz, Vector3f... points)
    {
        double a = Math.toRadians(angle), cos = Math.cos(a), sin = Math.sin(a);

        for(Vector3f point : points)
        {
            float x = point.x - cx, z = point.z - cz;

            point.x = (float) (cx + x*cos - z*sin);
            point.z = (float) (cz + x*sin + z*cos);
        }
    }

    /**
     * @param angle The given angle (in degrees) for rotation
     * @param cx The x point of rotation
     * @param cy The y point of rotation
     * @param points The points to be rotated
     */
    public static void rotateAxisZ(float angle, float cx, float cy, Vector3f... points)
    {
        double a = Math.toRadians(angle), cos = Math.cos(a), sin = Math.sin(a);

        for(Vector3f point : points)
        {
            float x = point.x - cx, y = point.y - cy;

            point.x = (float) (cx + x*cos - y*sin);
            point.y = (float) (cy + x*sin + y*cos);
        }
    }
    
    public static byte intToByte(int value)
    {
        if(value > 255) return -1;
        return (byte) (value & 0xff);
    }
}
