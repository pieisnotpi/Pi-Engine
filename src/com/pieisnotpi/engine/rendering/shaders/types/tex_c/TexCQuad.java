package com.pieisnotpi.engine.rendering.shaders.types.tex_c;

import com.pieisnotpi.engine.rendering.shaders.types.tex.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector3f;

public class TexCQuad extends TexQuad
{
    protected Sprite sprite;

    public TexCQuad(float x, float y, float z, float width, float height, float depth, Sprite sprite, Color color)
    {
        super(x, y, z, width, height, depth, sprite);
        this.sprite = sprite;

        setColors(color, color, color, color);
    }

    public TexCQuad(Vector3f c0, Vector3f c1, Vector3f c2, Vector3f c3, Sprite sprite, Color color)
    {
        super(c0, c1, c2, c3, sprite);
        this.sprite = sprite;

        setColors(color, color, color, color);
    }
}
