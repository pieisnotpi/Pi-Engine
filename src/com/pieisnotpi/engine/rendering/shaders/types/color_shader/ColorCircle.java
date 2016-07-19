package com.pieisnotpi.engine.rendering.shaders.types.color_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shapes.Circle;
import com.pieisnotpi.engine.rendering.shapes.Triangle;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector3f;

public class ColorCircle extends Circle
{
    Color color;
    private static final Material m = new Material(PiEngine.S_COLOR_ID, true, false, false);

    public ColorCircle(float x, float y, float z, float radius, int sides, Color color, int matrixID, Scene scene)
    {
        super(x, y, z, radius, sides, m, matrixID, scene);
        this.color = color;

        assemble();
    }

    public Triangle assembleVertex(float x0, float y0, float z0, float x1, float y1)
    {
        return new ColorTriangle(new Vector3f(x0, y0, z0), new Vector3f(x, y, z0), new Vector3f(x1, y1, z0), color, color, color, material, matrixID, scene);
    }
}
