package com.pieisnotpi.engine.rendering.renderable_types;

import com.pieisnotpi.engine.rendering.Color;

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
        super.setDefaults(vertCount);
        textColors = colors;
        outlineColors = new Color[vertCount];

        Arrays.fill(textColors, new Color(1, 1, 1));
        Arrays.fill(outlineColors, new Color(0, 0, 0));
    }
}
