package com.pieisnotpi.engine.ui.text.effects;

import com.pieisnotpi.engine.rendering.shaders.types.text_shader.TextQuad;
import com.pieisnotpi.engine.ui.text.Text;

import java.util.List;

public class WaveEffect implements TextEffect
{
    private int x;
    private float speed, heightDif, scale;
    private Text text;

    public WaveEffect(float speed, float heightDif, float scale)
    {
        this.speed = speed;
        this.heightDif = heightDif;
        this.scale = scale;
    }

    public void setText(Text text)
    {
        this.text = text;
    }

    public void process()
    {
        List<TextQuad> chars = text.chars;

        int prevLine = 0, t = 0;

        for(TextQuad c : chars)
        {
            if (c.line != prevLine) t = 0;

            float offset = -text.newlineSpace * c.line + c.cSprite.offsetY, sine = (float) (scale*Math.sin(Math.toRadians((x + heightDif * t) * speed)));

            c.setY(offset + text.getY() + sine);

            prevLine = c.line;
            t++;
        }

        x++;
    }
}
