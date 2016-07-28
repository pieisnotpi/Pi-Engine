package com.pieisnotpi.engine.rendering.ui.text;

import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.Renderable;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

public class TextRenderable extends Renderable
{
    public Color[] textColors, outlineColors;

    public void setTextColors(Color... textColors)
    {
        for(int i = 0; i < textColors.length && i < this.textColors.length; i++) if(textColors[i] != null) this.textColors[i].set(textColors[i]);
    }

    public void setOutlineColors(Color... outlineColors)
    {
        for(int i = 0; i < outlineColors.length && i < this.outlineColors.length; i++) if(outlineColors[i] != null) this.outlineColors[i].set(outlineColors[i]);
    }

    protected void setDefaults(int vertCount)
    {
        points = new Vector3f[vertCount];
        texCoords = new Vector2f[vertCount];
        textColors = new Color[vertCount];
        outlineColors = new Color[vertCount];

        Arrays.fill(points, new Vector3f());
        Arrays.fill(texCoords, new Vector2f());
        Arrays.fill(textColors, new Color(1, 1, 1));
        Arrays.fill(outlineColors, new Color(0, 0, 0));
    }
}