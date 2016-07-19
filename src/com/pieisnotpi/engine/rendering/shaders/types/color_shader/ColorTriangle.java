package com.pieisnotpi.engine.rendering.shaders.types.color_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shapes.Triangle;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector3f;

public class ColorTriangle extends Triangle
{
    private static final Material m = new Material(PiEngine.S_COLOR_ID, true, false, false);

    public ColorTriangle(Vector3f c0, Vector3f c1, Vector3f c2, Color c0c, Color c1c, Color c2c, int matrixID, Scene scene)
    {
        this(c0, c1, c2, c0c, c1c, c2c, m, matrixID, scene);
    }

    public ColorTriangle(Vector3f c0, Vector3f c1, Vector3f c2, Color c0c, Color c1c, Color c2c, Material material, int matrixID, Scene scene)
    {
        super(c0, c1, c2, c0c, c1c, c2c, material, matrixID, scene);

        register();
    }
}
