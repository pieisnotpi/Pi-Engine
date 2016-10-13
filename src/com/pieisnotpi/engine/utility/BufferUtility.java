package com.pieisnotpi.engine.utility;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;

public class BufferUtility
{
    public static void putColors(FloatBuffer buffer, Color... colors)
    {
        for(Color color : colors)
        {
            buffer.put(color.red);
            buffer.put(color.green);
            buffer.put(color.blue);
            buffer.put(color.alpha);
        }
    }

    public static void putVec3s(FloatBuffer buffer, Vector3f... vectors)
    {
        for(Vector3f vector : vectors)
        {
            buffer.put(vector.x);
            buffer.put(vector.y);
            buffer.put(vector.z);
        }
    }

    public static void putVec2s(FloatBuffer buffer, Vector2f... vectors)
    {
        for(Vector2f vector : vectors)
        {
            buffer.put(vector.x);
            buffer.put(vector.y);
        }
    }
}
