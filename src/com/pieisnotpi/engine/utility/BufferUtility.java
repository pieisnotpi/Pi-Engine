package com.pieisnotpi.engine.utility;

import com.pieisnotpi.engine.rendering.Color;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class BufferUtility
{
    public static FloatBuffer floatToFloatBuffer(float... values)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);

        buffer.put(values);
        buffer.flip();

        return buffer;
    }

    public static FloatBuffer mat4ToFloatBuffer(Matrix4f mat4)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

        mat4.get(buffer);

        return buffer;
    }

    public static FloatBuffer vec2ToFloatBuffer(Vector2f... vectors)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vectors.length*2);

        for(Vector2f vec3 : vectors)
        {
            buffer.put(vec3.x);
            buffer.put(vec3.y);
        }

        buffer.flip();

        return buffer;
    }

    public static FloatBuffer vec3ToFloatBuffer(Vector3f... vectors)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vectors.length*3);

        for(Vector3f vec3 : vectors)
        {
            buffer.put(vec3.x);
            buffer.put(vec3.y);
            buffer.put(vec3.z);
        }

        buffer.flip();

        return buffer;
    }

    public static FloatBuffer colorToFloatBuffer(Color... colors)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(colors.length*4);

        for(Color color : colors)
        {
            buffer.put(color.getRed());
            buffer.put(color.getGreen());
            buffer.put(color.getBlue());
            buffer.put(color.getAlpha());
        }

        buffer.flip();

        return buffer;
    }

    public static void putColors(FloatBuffer buffer, Color... colors)
    {
        for(Color color : colors)
        {
            buffer.put(color.getRed());
            buffer.put(color.getGreen());
            buffer.put(color.getBlue());
            buffer.put(color.getAlpha());
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
