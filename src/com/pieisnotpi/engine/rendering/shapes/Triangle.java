package com.pieisnotpi.engine.rendering.shapes;

import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class Triangle extends Renderable
{
    public Triangle(Vector3f c0, Vector3f c1, Vector3f c2, Material material, int matrixID, Scene scene)
    {
        this(c0, c1, c2, null, null, null, null, null, null, null, material, matrixID, scene);
    }

    public Triangle(Vector3f c0, Vector3f c1, Vector3f c2, Color c0c, Color c1c, Color c2c, Material material, int matrixID, Scene scene)
    {
        this(c0, c1, c2, c0c, c1c, c2c, null, null, null, null, material, matrixID, scene);
    }

    public Triangle(Vector3f c0, Vector3f c1, Vector3f c2, Vector2f c0t, Vector2f c1t, Vector2f c2t, Texture texture, Material material, int matrixID, Scene scene)
    {
        this(c0, c1, c2, null, null, null, c0t, c1t, c2t, texture, material, matrixID, scene);
    }

    public Triangle(Vector3f c0, Vector3f c1, Vector3f c2, Color c0c, Color c1c, Color c2c, Vector2f c0t, Vector2f c1t, Vector2f c2t, Texture texture, Material material, int matrixID, Scene scene)
    {
        setDefaults(3, GL11.GL_TRIANGLES, material);

        setPoints(c0, c1, c2);
        setColors(c0c, c1c, c2c);
        setTexCoords(c0t, c1t, c2t);

        this.texture = texture;
        this.matrixID = matrixID;
        this.scene = scene;
    }

    public String toString()
    {
        return "c0: " + points[0] + ", c1: " + points[1] + ", c2: " + points[2];
    }
}
