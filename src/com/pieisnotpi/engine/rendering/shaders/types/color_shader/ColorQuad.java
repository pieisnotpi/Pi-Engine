package com.pieisnotpi.engine.rendering.shaders.types.color_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shapes.Quad;
import com.pieisnotpi.engine.scene.Scene;

public class ColorQuad extends Quad
{
    private static final Material m = new Material(PiEngine.S_COLOR_ID, true, false, false);

    public ColorQuad(float x, float y, float z, float width, float height, float depth, Color color, int matrixID, Scene scene)
    {
        this(x, y, z, width, height, depth, color, color, color, color, matrixID, scene);
    }

    public ColorQuad(float x, float y, float z, float width, float height, float depth, Color c0c, Color c1c, Color c2c, Color c3c, int matrixID, Scene scene)
    {
        super(x, y, z, width, height, depth, m, matrixID, scene);

        if(c0c.getAlpha() < 1 || c1c.getAlpha() < 1 || c2c.getAlpha() < 1 || c3c.getAlpha() < 1) shouldBeSorted = true;

        setColors(c0c, c1c, c2c, c3c);

        register();
    }
}
